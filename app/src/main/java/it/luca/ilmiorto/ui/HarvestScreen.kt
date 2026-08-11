package it.luca.ilmiorto.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.GardenState
import it.luca.ilmiorto.data.Harvest
import it.luca.ilmiorto.data.isIsoDate
import it.luca.ilmiorto.data.parseHarvestSpeech
import it.luca.ilmiorto.data.parseSpokenDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private enum class HarvestVoiceMode { DATE, ITEM }

@Composable
fun HarvestScreen(
    state: GardenState,
    onAddHarvest: (Harvest) -> Unit,
    onRemoveHarvest: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var sessionDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var manualProduct by remember { mutableStateOf("") }
    var manualGrams by remember { mutableStateOf("") }
    var voiceMode by remember { mutableStateOf(HarvestVoiceMode.ITEM) }
    var pendingVoiceMode by remember { mutableStateOf<HarvestVoiceMode?>(null) }
    var statusMessage by remember {
        mutableStateOf("Esempio: “pomodori 850 grammi”. La data si imposta una sola volta.")
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val spoken = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            .orEmpty()
        if (spoken.isBlank()) {
            statusMessage = "Non ho ricevuto alcun testo dal riconoscimento vocale."
            return@rememberLauncherForActivityResult
        }
        when (voiceMode) {
            HarvestVoiceMode.DATE -> {
                val parsed = parseSpokenDate(spoken)
                if (parsed == null) {
                    statusMessage = "Data non riconosciuta: “$spoken”. Prova, per esempio, “4 agosto 2026”."
                } else {
                    sessionDate = parsed.toString()
                    statusMessage = "Data impostata: ${formatDate(parsed.toString())}."
                }
            }

            HarvestVoiceMode.ITEM -> {
                val parsed = parseHarvestSpeech(spoken)
                if (parsed == null || !isIsoDate(sessionDate) || sessionDate.isBlank()) {
                    statusMessage = "Comando non riconosciuto: “$spoken”. Dì, per esempio, “zucchine 420 grammi”."
                } else {
                    onAddHarvest(
                        Harvest(
                            date = sessionDate,
                            cropName = parsed.product,
                            weightGrams = parsed.grams,
                        )
                    )
                    statusMessage = "Aggiunti ${parsed.product}: ${parsed.grams} g, data ${formatDate(sessionDate)}."
                }
            }
        }
    }

    fun launchVoiceRecognition(mode: HarvestVoiceMode) {
        voiceMode = mode
        val prompt = if (mode == HarvestVoiceMode.DATE) {
            "Dì la data, per esempio 4 agosto 2026"
        } else {
            "Dì il prodotto e il peso in grammi, per esempio pomodori 850 grammi"
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT")
            putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        runCatching { speechLauncher.launch(intent) }
            .onFailure {
                statusMessage = "Riconoscimento vocale non disponibile sul telefono. Usa i campi manuali."
            }
    }

    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        val pending = pendingVoiceMode
        pendingVoiceMode = null
        if (granted && pending != null) {
            launchVoiceRecognition(pending)
        } else {
            statusMessage = "Permesso microfono non concesso. Puoi abilitarlo nelle impostazioni dell’app oppure usare i campi manuali."
        }
    }

    fun startVoiceRecognition(mode: HarvestVoiceMode) {
        if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            launchVoiceRecognition(mode)
        } else {
            pendingVoiceMode = mode
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun addManualHarvest() {
        val grams = manualGrams.toIntOrNull()
        if (manualProduct.isBlank() || grams == null || grams <= 0 || !isIsoDate(sessionDate) || sessionDate.isBlank()) {
            statusMessage = "Inserisci nome del prodotto, peso in grammi e una data valida."
            return
        }
        onAddHarvest(
            Harvest(
                date = sessionDate,
                cropName = manualProduct.trim(),
                weightGrams = grams,
            )
        )
        statusMessage = "Aggiunti ${manualProduct.trim()}: $grams g."
        manualProduct = ""
        manualGrams = ""
    }

    val totalGrams = state.harvests.sumOf { it.weightGrams }
    val currentMonth = LocalDate.now().toString().take(7)
    val monthGrams = state.harvests.filter { it.date.startsWith(currentMonth) }.sumOf { it.weightGrams }
    val totalsByCrop = state.harvests
        .groupBy { it.cropName }
        .mapValues { (_, values) -> values.sumOf { it.weightGrams } }
        .toList()
        .sortedByDescending { it.second }
    val topCrop = totalsByCrop.firstOrNull()?.first ?: "—"

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            SectionTitle(
                title = "Raccolti",
                subtitle = "Imposta la data una sola volta, poi pronuncia soltanto prodotto e peso in grammi.",
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.42f),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text("Registrazione rapida", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedTextField(
                            value = sessionDate,
                            onValueChange = { sessionDate = it },
                            label = { Text("Data") },
                            supportingText = { Text("AAAA-MM-GG") },
                            isError = sessionDate.isNotBlank() && !isIsoDate(sessionDate),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                        )
                        OutlinedButton(
                            onClick = { startVoiceRecognition(HarvestVoiceMode.DATE) },
                            modifier = Modifier.padding(top = 8.dp),
                        ) { Text("🎙️ Dì data") }
                    }
                    Button(
                        onClick = { startVoiceRecognition(HarvestVoiceMode.ITEM) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("🎙️ Dì prodotto e peso")
                    }
                    Text(
                        statusMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedTextField(
                            value = manualProduct,
                            onValueChange = { manualProduct = it },
                            label = { Text("Prodotto") },
                            singleLine = true,
                            modifier = Modifier.weight(1.45f),
                        )
                        OutlinedTextField(
                            value = manualGrams,
                            onValueChange = { manualGrams = it.filter(Char::isDigit) },
                            label = { Text("Grammi") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(0.75f),
                        )
                    }
                    OutlinedButton(
                        onClick = ::addManualHarvest,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Aggiungi senza voce") }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard("$totalGrams g", "totale", Modifier.weight(1f))
                StatCard("$monthGrams g", "questo mese", Modifier.weight(1f))
                StatCard(topCrop, "più raccolto", Modifier.weight(1f))
            }
        }

        if (totalsByCrop.isNotEmpty()) {
            item {
                Card {
                    Column(Modifier.padding(16.dp)) {
                        Text("Produzione per prodotto", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(10.dp))
                        totalsByCrop.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(item.first, style = MaterialTheme.typography.bodyMedium)
                                Text("${item.second} g", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            }
                            if (index < totalsByCrop.lastIndex) {
                                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }

        if (state.harvests.isEmpty()) {
            item {
                EmptyState(
                    emoji = "🧺",
                    title = "Nessun raccolto registrato",
                    message = "Imposta la data e pronuncia, per esempio, “pomodori 850 grammi”.",
                )
            }
        } else {
            items(state.harvests.sortedWith(compareByDescending<Harvest> { it.date }.thenBy { it.cropName }), key = { it.id }) { harvest ->
                HarvestCard(
                    harvest = harvest,
                    onRemove = { onRemoveHarvest(harvest.id) },
                )
            }
        }
        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
private fun HarvestCard(
    harvest: Harvest,
    onRemove: () -> Unit,
) {
    Card {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(harvest.cropName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        formatDate(harvest.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text("${harvest.weightGrams} g", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }
            TextButton(onClick = onRemove, modifier = Modifier.fillMaxWidth()) {
                Text("Elimina registrazione")
            }
        }
    }
}

private fun formatDate(value: String): String = runCatching {
    LocalDate.parse(value).format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ITALIAN))
}.getOrDefault(value)
