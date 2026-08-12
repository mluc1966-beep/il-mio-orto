package it.luca.ilmiorto.ui

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.BuildConfig
import it.luca.ilmiorto.R
import it.luca.ilmiorto.data.GardenCloudSync
import it.luca.ilmiorto.data.GardenRepository
import it.luca.ilmiorto.data.GardenStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

private val APP_VERSION = BuildConfig.VERSION_NAME

private enum class AppSection(val title: String, val emoji: String) {
    MAP("Mappa", "🗺️"),
    CALENDAR("Calendario", "📅"),
    ACTIVITIES("Attività", "✅"),
    HARVEST("Raccolti", "🧺"),
    CROPS("Colture", "🌿"),
    ARCHIVE("Archivio", "🗄️"),
}

private enum class DialogType { CROP, TASK }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenApp() {
    val context = LocalContext.current
    val activity = context as? Activity
    val store = remember { GardenStore(GardenRepository(context.applicationContext)) }
    val cloud = remember {
        GardenCloudSync(
            context = context.applicationContext,
            localState = { store.state },
            onRemoteState = store::replaceFromCloud,
        )
    }
    val cloudUi = cloud.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    DisposableEffect(cloud) {
        store.onLocalChange = cloud::queueLocalState
        cloud.start()
        onDispose {
            store.onLocalChange = null
            cloud.stop()
        }
    }

    var showCover by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2600)
        showCover = false
    }
    if (showCover) {
        CoverScreen(onEnter = { showCover = false })
        return
    }

    var sectionName by rememberSaveable { mutableStateOf(AppSection.MAP.name) }
    val section = AppSection.valueOf(sectionName)
    var dialogType by remember { mutableStateOf<DialogType?>(null) }
    var initialCropZone by remember { mutableStateOf("") }
    var mapDetailZone by rememberSaveable { mutableStateOf("") }
    var showCloudPanel by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    fun message(text: String) {
        scope.launch { snackbarHostState.showSnackbar(text) }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        if (uri != null) {
            val result = runCatching {
                context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { writer ->
                    writer.write(store.exportJson())
                } ?: error("Impossibile aprire il file")
            }
            message(if (result.isSuccess) "Backup esportato" else "Esportazione non riuscita")
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            val result = runCatching {
                val text = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
                    reader.readText().also {
                        require(it.length <= 5_000_000) { "File troppo grande" }
                    }
                } ?: error("Impossibile leggere il file")
                store.importJson(text)
            }
            message(
                result.fold(
                    onSuccess = { "Backup importato e messo in coda per la sincronizzazione" },
                    onFailure = { "Backup non valido: ${it.message ?: "errore sconosciuto"}" },
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🌱 Il mio Orto · ${store.state.seasonYear}")
                        Text(
                            "v$APP_VERSION",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { showCloudPanel = true }) {
                        Text(cloudLabel(cloudUi))
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                AppSection.entries.forEach { item ->
                    NavigationBarItem(
                        selected = item == section,
                        onClick = {
                            sectionName = item.name
                            if (item != AppSection.MAP) mapDetailZone = ""
                        },
                        icon = { Text(item.emoji) },
                        label = { Text(item.title, maxLines = 1) },
                    )
                }
            }
        },
        floatingActionButton = {
            when (section) {
                AppSection.MAP -> {
                    if (mapDetailZone.isNotBlank()) {
                        FloatingActionButton(
                            onClick = {
                                initialCropZone = mapDetailZone
                                dialogType = DialogType.CROP
                            },
                        ) { Text("+") }
                    }
                }

                AppSection.CALENDAR, AppSection.ACTIVITIES -> FloatingActionButton(
                    onClick = { dialogType = DialogType.TASK },
                ) { Text("+") }

                AppSection.HARVEST, AppSection.CROPS, AppSection.ARCHIVE -> Unit
            }
        },
    ) { innerPadding ->
        when (section) {
            AppSection.MAP -> MapScreen(
                state = store.state,
                onAddCrop = { zoneId ->
                    initialCropZone = zoneId
                    dialogType = DialogType.CROP
                },
                onRemoveCrop = {
                    store.removeCrop(it)
                    message("Coltura eliminata")
                },
                onUpdateCrop = {
                    store.updateCrop(it)
                    message("Coltura aggiornata")
                },
                onAutoArrangeCrop = {
                    val applied = store.autoArrangeCrop(it)
                    message(
                        if (applied) "Progetto applicato: colture raggruppate e vincoli rispettati"
                        else "Combinazione non collocabile: modifica quantità o distanze"
                    )
                },
                onRemovePlant = { cropId, plantId ->
                    store.removePlant(cropId, plantId)
                    message("Pianta rimossa")
                },
                onMovePlant = store::movePlant,
                onDetailZoneChanged = { mapDetailZone = it },
                modifier = Modifier.padding(innerPadding),
            )

            AppSection.CALENDAR -> CalendarScreen(
                state = store.state,
                modifier = Modifier.padding(innerPadding),
            )

            AppSection.ACTIVITIES -> ActivitiesScreen(
                state = store.state,
                onToggleTask = store::toggleTask,
                onRemoveTask = {
                    store.removeTask(it)
                    message("Attività eliminata")
                },
                modifier = Modifier.padding(innerPadding),
            )

            AppSection.HARVEST -> HarvestScreen(
                state = store.state,
                onAddHarvest = {
                    store.addHarvest(it)
                    message("Raccolto registrato")
                },
                onRemoveHarvest = {
                    store.removeHarvest(it)
                    message("Raccolto eliminato")
                },
                modifier = Modifier.padding(innerPadding),
            )

            AppSection.CROPS -> CropGuideScreen(
                state = store.state,
                onAddHistory = {
                    store.addHistory(it)
                    message("Rotazione registrata")
                },
                onRemoveHistory = {
                    store.removeHistory(it)
                    message("Rotazione eliminata")
                },
                onApplyDistances = {
                    store.applyGuideDistances(it)
                    message("Distanze applicate alle colture presenti")
                },
                onAddTask = {
                    store.addTask(it)
                    message("Attività aggiunta")
                },
                modifier = Modifier.padding(innerPadding),
            )

            AppSection.ARCHIVE -> ArchiveScreen(
                state = store.state,
                onExport = {
                    exportLauncher.launch("il_mio_orto_${store.state.seasonYear}_${LocalDate.now()}.json")
                },
                onImport = { importLauncher.launch(arrayOf("application/json", "text/plain")) },
                onReset = {
                    store.reset()
                    message("Dati cancellati")
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }

    when (dialogType) {
        DialogType.CROP -> AddCropDialog(
            state = store.state,
            initialZoneId = initialCropZone,
            onDismiss = { dialogType = null },
            onConfirm = {
                store.addCrop(it)
                dialogType = null
                message("Coltura aggiunta")
            },
        )

        DialogType.TASK -> AddTaskDialog(
            state = store.state,
            onDismiss = { dialogType = null },
            onConfirm = {
                store.addTask(it)
                dialogType = null
                message("Attività aggiunta")
            },
        )

        null -> Unit
    }

    if (showCloudPanel) {
        val signedIn = cloudUi.email.isNotBlank()
        AlertDialog(
            onDismissRequest = { showCloudPanel = false },
            title = { Text("☁️ Orto condiviso") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (signedIn) {
                        Text(cloudUi.displayName.ifBlank { "Account collegato" }, fontWeight = FontWeight.SemiBold)
                        Text(cloudUi.email)
                    } else {
                        Text("Questa copia sta lavorando con i dati locali. Accedi con Google per usare lo stesso orto condiviso della PWA.")
                    }
                    Text(cloudUi.message)
                    if (cloudUi.lastSync.isNotBlank()) Text("Ultimo allineamento: ${cloudUi.lastSync}")
                }
            },
            confirmButton = {
                if (signedIn) {
                    TextButton(onClick = { cloud.syncNow() }) { Text("Sincronizza ora") }
                } else {
                    Button(
                        enabled = activity != null,
                        onClick = {
                            if (activity != null) {
                                scope.launch { cloud.signIn(activity) }
                            }
                        },
                    ) { Text("Accedi con Google") }
                }
            },
            dismissButton = {
                if (signedIn) {
                    TextButton(onClick = { showLogoutConfirm = true }) { Text("Esci") }
                } else {
                    TextButton(onClick = { showCloudPanel = false }) { Text("Chiudi") }
                }
            },
        )
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Disconnettere l'orto condiviso?") },
            text = { Text("I dati già salvati sul telefono restano disponibili. Le nuove modifiche non verranno condivise finché non accedi di nuovo.") },
            confirmButton = {
                Button(onClick = {
                    showLogoutConfirm = false
                    showCloudPanel = false
                    scope.launch { cloud.signOut() }
                }) { Text("Disconnetti") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) { Text("Annulla") }
            },
        )
    }
}

@Composable
private fun CoverScreen(onEnter: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.copertina),
            contentDescription = "Copertina Il mio Orto 2026",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0x55F4F6E9))
                .padding(horizontal = 28.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Button(onClick = onEnter) {
                Text("Entra nell'orto")
            }
        }
    }
}

private fun cloudLabel(state: GardenCloudSync.UiState): String = when {
    state.email.isBlank() -> "☁ Locale"
    state.mode == GardenCloudSync.Mode.SYNCING -> "☁ Sync…"
    state.mode == GardenCloudSync.Mode.OFFLINE -> "☁ Offline"
    state.mode == GardenCloudSync.Mode.ERROR -> "☁ Errore"
    else -> "☁ Sincronizzato"
}
