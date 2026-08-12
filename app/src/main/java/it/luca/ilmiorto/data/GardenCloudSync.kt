package it.luca.ilmiorto.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Source
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Sincronizza l'app Android con la stessa struttura Firestore usata dalla PWA:
 * gardens/main + sottoraccolte crops, tasks, harvests, history.
 *
 * Il formato cloud usa i nomi della PWA (zone, count, spacingCm, x/y, ...),
 * ma conserva anche i campi nativi che la PWA lascia intatti, così i due client
 * possono lavorare sullo stesso orto.
 */
class GardenCloudSync(
    context: Context,
    private val localState: () -> GardenState,
    private val onRemoteState: (GardenState) -> Unit,
) {
    enum class Mode { LOCAL, SYNCING, SYNCED, OFFLINE, ERROR }

    data class UiState(
        val mode: Mode = Mode.LOCAL,
        val email: String = "",
        val displayName: String = "",
        val message: String = "Dati salvati solo sul dispositivo",
        val lastSync: String = "",
    )

    var uiState by mutableStateOf(UiState())
        private set

    private val appContext = context.applicationContext
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val gardenRef = db.collection("gardens").document("main")
    private val collectionNames = listOf("crops", "tasks", "harvests", "history")
    private val registrations = mutableListOf<ListenerRegistration>()
    private val remoteDocs = collectionNames.associateWith { linkedMapOf<String, Map<String, Any?>>() }.toMutableMap()
    private val ready = linkedSetOf<String>()
    private var meta: Map<String, Any?> = emptyMap()
    private var metaExists = false
    private var preserveLocalIfRemoteEmpty = false
    private var started = false
    private var syncing = false
    private var queuedState: GardenState? = null
    private val handler = Handler(Looper.getMainLooper())
    private var pendingPush: Runnable? = null

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        clearListeners()
        if (user == null) {
            uiState = UiState()
        } else {
            updateUser(user, Mode.SYNCING, "Collegamento all'orto condiviso…")
            ensureGardenThenListen()
        }
    }

    fun start() {
        if (started) return
        started = true
        auth.addAuthStateListener(authListener)
    }

    fun stop() {
        if (!started) return
        started = false
        auth.removeAuthStateListener(authListener)
        clearListeners()
        pendingPush?.let(handler::removeCallbacks)
        pendingPush = null
    }

    fun queueLocalState(state: GardenState) {
        if (auth.currentUser == null) return
        queuedState = state
        pendingPush?.let(handler::removeCallbacks)
        val runnable = Runnable {
            pendingPush = null
            val next = queuedState
            queuedState = null
            if (next != null) pushState(next)
        }
        pendingPush = runnable
        handler.postDelayed(runnable, 650)
    }

    fun syncNow() {
        if (auth.currentUser == null) return
        pendingPush?.let(handler::removeCallbacks)
        pendingPush = null
        queuedState = null
        pushState(localState())
    }

    fun signIn(email: String, password: String) {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank() || password.isBlank()) {
            uiState = uiState.copy(
                mode = Mode.ERROR,
                message = "Inserisci email e password",
            )
            return
        }

        uiState = uiState.copy(mode = Mode.SYNCING, message = "Accesso all'orto condiviso…")
        auth.signInWithEmailAndPassword(cleanEmail, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    updateUser(it, Mode.SYNCING, "Collegamento all'orto condiviso…")
                }
            }
            .addOnFailureListener { error ->
                uiState = uiState.copy(
                    mode = Mode.ERROR,
                    message = "Accesso non riuscito: ${friendlyAuthError(error)}",
                )
            }
    }

    fun createAccount(email: String, password: String) {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank() || password.isBlank()) {
            uiState = uiState.copy(
                mode = Mode.ERROR,
                message = "Inserisci email e password",
            )
            return
        }
        if (password.length < 6) {
            uiState = uiState.copy(
                mode = Mode.ERROR,
                message = "La password deve avere almeno 6 caratteri",
            )
            return
        }

        uiState = uiState.copy(mode = Mode.SYNCING, message = "Creo l'utenza…")
        auth.createUserWithEmailAndPassword(cleanEmail, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    updateUser(it, Mode.SYNCING, "Collegamento all'orto condiviso…")
                }
            }
            .addOnFailureListener { error ->
                uiState = uiState.copy(
                    mode = Mode.ERROR,
                    message = "Creazione utenza non riuscita: ${friendlyAuthError(error)}",
                )
            }
    }

    suspend fun signOut() {
        clearListeners()
        auth.signOut()
        uiState = UiState()
    }

    private fun friendlyAuthError(error: Exception): String {
        val raw = error.localizedMessage ?: error.javaClass.simpleName
        return when {
            raw.contains("password", ignoreCase = true) && raw.contains("invalid", ignoreCase = true) ->
                "email o password non corretti"
            raw.contains("malformed", ignoreCase = true) || raw.contains("badly formatted", ignoreCase = true) ->
                "indirizzo email non valido"
            raw.contains("already in use", ignoreCase = true) || raw.contains("already exists", ignoreCase = true) ->
                "questa utenza esiste già: usa Accedi"
            raw.contains("network", ignoreCase = true) ->
                "problema di connessione"
            else -> raw
        }
    }

    private fun ensureGardenThenListen() {
        preserveLocalIfRemoteEmpty = false
        gardenRef.get(Source.SERVER)
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    attachListeners()
                } else {
                    uiState = uiState.copy(mode = Mode.SYNCING, message = "Creo il primo orto condiviso…")
                    seedFromLocal(localState()) { success ->
                        if (success) attachListeners()
                    }
                }
            }
            .addOnFailureListener {
                // Firestore Android mantiene la cache offline. Se il server non è raggiungibile,
                // ascoltiamo la cache ma non cancelliamo lo stato locale se essa è vuota.
                preserveLocalIfRemoteEmpty = true
                uiState = uiState.copy(mode = Mode.OFFLINE, message = "Offline: uso i dati disponibili sul telefono")
                attachListeners()
            }
    }

    private fun attachListeners() {
        clearListeners()
        ready.clear()
        collectionNames.forEach { remoteDocs[it]?.clear() }

        registrations += gardenRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
            if (error != null) {
                fail("Sincronizzazione orto", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                metaExists = snapshot.exists()
                meta = snapshot.data ?: emptyMap()
                ready += "meta"
                updateFromMetadata(snapshot.metadata.isFromCache, snapshot.metadata.hasPendingWrites())
                applyRemoteIfReady()
            }
        }

        collectionNames.forEach { name ->
            registrations += gardenRef.collection(name)
                .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                    if (error != null) {
                        fail("Sincronizzazione $name", error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val target = remoteDocs.getValue(name)
                        target.clear()
                        snapshot.documents.forEach { doc ->
                            target[doc.id] = (doc.data ?: emptyMap()).mapValues { it.value }
                        }
                        ready += name
                        updateFromMetadata(snapshot.metadata.isFromCache, snapshot.metadata.hasPendingWrites())
                        applyRemoteIfReady()
                    }
                }
        }
    }

    private fun applyRemoteIfReady() {
        if (ready.size < collectionNames.size + 1) return
        val remoteIsEmpty = !metaExists && collectionNames.all { remoteDocs.getValue(it).isEmpty() }
        if (preserveLocalIfRemoteEmpty && remoteIsEmpty) return

        runCatching { cloudToGardenState() }
            .onSuccess(onRemoteState)
            .onFailure { fail("Dati cloud non compatibili", it) }
    }

    private fun updateFromMetadata(fromCache: Boolean, pendingWrites: Boolean) {
        val mode = when {
            pendingWrites -> Mode.SYNCING
            fromCache -> Mode.OFFLINE
            else -> Mode.SYNCED
        }
        val message = when (mode) {
            Mode.SYNCING -> "Sincronizzazione in corso…"
            Mode.OFFLINE -> "Offline: modifiche conservate sul telefono"
            Mode.SYNCED -> "Orto condiviso sincronizzato"
            else -> uiState.message
        }
        uiState = uiState.copy(
            mode = mode,
            message = message,
            lastSync = if (!fromCache && !pendingWrites) nowLabel() else uiState.lastSync,
        )
    }

    private fun seedFromLocal(state: GardenState, done: (Boolean) -> Unit) {
        writeState(state, deleteMissing = false, done = done)
    }

    private fun pushState(state: GardenState) {
        if (auth.currentUser == null) return
        if (syncing) {
            queuedState = state
            return
        }
        syncing = true
        uiState = uiState.copy(mode = Mode.SYNCING, message = "Sincronizzazione in corso…")
        writeState(state, deleteMissing = true) { success ->
            syncing = false
            if (success) {
                uiState = uiState.copy(
                    mode = Mode.SYNCED,
                    message = "Orto condiviso sincronizzato",
                    lastSync = nowLabel(),
                )
            }
            val next = queuedState
            queuedState = null
            if (next != null) queueLocalState(next)
        }
    }

    private fun writeState(state: GardenState, deleteMissing: Boolean, done: (Boolean) -> Unit) {
        val user = auth.currentUser ?: run {
            done(false)
            return
        }

        val batch = db.batch()
        batch.set(
            gardenRef,
            mapOf(
                "year" to state.seasonYear,
                "updatedAt" to FieldValue.serverTimestamp(),
                "updatedBy" to user.uid,
                "updatedByEmail" to (user.email ?: ""),
            ),
            com.google.firebase.firestore.SetOptions.merge(),
        )

        val outgoing = mapOf(
            "crops" to state.crops.associate { it.id to it.toCloud() },
            "tasks" to state.tasks.associate { it.id to it.toCloud() },
            "harvests" to state.harvests.associate { it.id to it.toCloud() },
            "history" to state.history.associate { it.id to it.toCloud() },
        )

        outgoing.forEach { (name, docs) ->
            docs.forEach { (id, value) ->
                batch.set(gardenRef.collection(name).document(id), value)
            }
            if (deleteMissing) {
                remoteDocs.getValue(name).keys
                    .filterNot(docs::containsKey)
                    .forEach { id -> batch.delete(gardenRef.collection(name).document(id)) }
            }
        }

        batch.commit()
            .addOnSuccessListener { done(true) }
            .addOnFailureListener { error ->
                fail("Scrittura Firestore", error)
                done(false)
            }
    }

    private fun cloudToGardenState(): GardenState {
        val year = meta.number("year")?.toInt() ?: LocalDate.now().year
        return GardenState(
            seasonYear = year,
            crops = remoteDocs.getValue("crops").map { (id, map) -> map.toCrop(id) },
            tasks = remoteDocs.getValue("tasks").map { (id, map) -> map.toTask(id) },
            harvests = remoteDocs.getValue("harvests").map { (id, map) -> map.toHarvest(id) },
            history = remoteDocs.getValue("history").map { (id, map) -> map.toHistory(id) },
        )
    }

    private fun updateUser(user: FirebaseUser, mode: Mode, message: String) {
        uiState = uiState.copy(
            mode = mode,
            email = user.email ?: "",
            displayName = user.displayName ?: "",
            message = message,
        )
    }

    private fun fail(prefix: String, error: Throwable) {
        uiState = uiState.copy(
            mode = Mode.ERROR,
            message = "$prefix: ${error.localizedMessage ?: "errore sconosciuto"}",
        )
    }

    private fun clearListeners() {
        registrations.forEach { runCatching { it.remove() } }
        registrations.clear()
        ready.clear()
    }

    private fun nowLabel(): String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

    // --- formato cloud compatibile con la PWA ---

    private fun Crop.toCloud(): Map<String, Any?> = mapOf(
        "id" to id,
        "zone" to zoneId,
        "name" to name,
        "variety" to variety,
        "count" to plantCount,
        "sowingDate" to sowingDate,
        "transplantDate" to transplantDate,
        "expectedHarvestDate" to expectedHarvestDate,
        "notes" to notes,
        "catalogId" to catalogId,
        "iconColor" to iconColorHex,
        "iconDiameterCm" to iconDiameterCm,
        "spacingCm" to plantSpacingCm,
        "rowSpacingCm" to rowSpacingCm,
        "plants" to plants.map { mapOf("id" to it.id, "x" to it.xFraction, "y" to it.yFraction) },
    )

    private fun GardenTask.toCloud(): Map<String, Any?> = mapOf(
        "id" to id,
        "date" to date,
        "title" to title,
        "category" to category,
        "zone" to zoneId,
        "cropId" to cropId,
        "notes" to notes,
        "done" to completed,
    )

    private fun Harvest.toCloud(): Map<String, Any?> = mapOf(
        "id" to id,
        "date" to date,
        "name" to cropName,
        "weight" to weightGrams,
    )

    private fun CropHistoryEntry.toCloud(): Map<String, Any?> = mapOf(
        "id" to id,
        "year" to year,
        "zone" to zoneId,
        "speciesId" to speciesId,
        "speciesName" to speciesName,
    )

    private fun Map<String, Any?>.toCrop(documentId: String): Crop {
        val name = string("name").ifBlank { "Pianta" }
        val inferred = inferCatalog(name)
        val count = (number("count") ?: number("plantCount"))?.toInt()?.coerceIn(0, 100) ?: 0
        val positions = list("plants").mapNotNull { raw ->
            val p = raw as? Map<*, *> ?: return@mapNotNull null
            PlantPosition(
                id = p.stringAny("id").ifBlank { java.util.UUID.randomUUID().toString() },
                xFraction = (p.numberAny("x") ?: p.numberAny("xFraction") ?: 0.5).coerceIn(0.0, 1.0),
                yFraction = (p.numberAny("y") ?: p.numberAny("yFraction") ?: 0.5).coerceIn(0.0, 1.0),
            )
        }.let { if (it.isEmpty() && count > 0) initialPlantPositions(count) else it.take(count) }

        return Crop(
            id = string("id").ifBlank { documentId },
            zoneId = string("zone").ifBlank { string("zoneId").ifBlank { "proda_1" } },
            name = name,
            variety = string("variety"),
            plantCount = count,
            sowingDate = string("sowingDate"),
            transplantDate = string("transplantDate"),
            expectedHarvestDate = string("expectedHarvestDate"),
            notes = string("notes"),
            catalogId = string("catalogId").ifBlank { inferred?.id ?: "custom" },
            iconColorHex = string("iconColor").ifBlank { string("iconColorHex").ifBlank { inferred?.colorHex ?: "#C9E2BD" } },
            iconDiameterCm = (number("iconDiameterCm") ?: inferred?.iconDiameterCm ?: 20.0).coerceIn(5.0, 100.0),
            plantSpacingCm = (number("spacingCm") ?: number("plantSpacingCm") ?: inferred?.plantSpacingCm ?: 30.0).coerceIn(5.0, 300.0),
            rowSpacingCm = (number("rowSpacingCm") ?: inferred?.rowSpacingCm ?: 40.0).coerceIn(5.0, 300.0),
            plants = positions,
        )
    }

    private fun Map<String, Any?>.toTask(documentId: String) = GardenTask(
        id = string("id").ifBlank { documentId },
        date = string("date").ifBlank { LocalDate.now().toString() },
        title = string("title").ifBlank { "Attività" },
        category = string("category").ifBlank { "Altro" },
        zoneId = string("zone").ifBlank { string("zoneId") },
        cropId = string("cropId"),
        notes = string("notes"),
        completed = boolean("done") ?: boolean("completed") ?: false,
    )

    private fun Map<String, Any?>.toHarvest(documentId: String) = Harvest(
        id = string("id").ifBlank { documentId },
        date = string("date").ifBlank { LocalDate.now().toString() },
        cropName = string("name").ifBlank { string("cropName").ifBlank { "Prodotto" } },
        weightGrams = ((number("weight") ?: number("weightGrams"))?.toInt() ?: 0).coerceAtLeast(0),
    )

    private fun Map<String, Any?>.toHistory(documentId: String) = CropHistoryEntry(
        id = string("id").ifBlank { documentId },
        year = number("year")?.toInt() ?: (LocalDate.now().year - 1),
        zoneId = string("zone").ifBlank { string("zoneId").ifBlank { "proda_1" } },
        speciesId = string("speciesId"),
        speciesName = string("speciesName"),
    )

    private fun Map<String, Any?>.string(key: String): String = this[key]?.toString() ?: ""
    private fun Map<String, Any?>.number(key: String): Double? = (this[key] as? Number)?.toDouble()
    private fun Map<String, Any?>.boolean(key: String): Boolean? = this[key] as? Boolean
    private fun Map<String, Any?>.list(key: String): List<*> = this[key] as? List<*> ?: emptyList<Any?>()

    private fun Map<*, *>.stringAny(key: String): String = this[key]?.toString() ?: ""
    private fun Map<*, *>.numberAny(key: String): Double? = (this[key] as? Number)?.toDouble()
}
