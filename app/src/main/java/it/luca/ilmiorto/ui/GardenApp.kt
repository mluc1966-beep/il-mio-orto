package it.luca.ilmiorto.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import it.luca.ilmiorto.data.GardenRepository
import it.luca.ilmiorto.data.GardenStore
import kotlinx.coroutines.launch
import java.time.LocalDate

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
    val store = remember { GardenStore(GardenRepository(context.applicationContext)) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var sectionName by rememberSaveable { mutableStateOf(AppSection.MAP.name) }
    val section = AppSection.valueOf(sectionName)
    var dialogType by remember { mutableStateOf<DialogType?>(null) }
    var initialCropZone by remember { mutableStateOf("") }
    var mapDetailZone by rememberSaveable { mutableStateOf("") }

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
                    onSuccess = { "Backup importato" },
                    onFailure = { "Backup non valido: ${it.message ?: "errore sconosciuto"}" },
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("🌱 Il mio Orto · ${store.state.seasonYear}")
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
}
