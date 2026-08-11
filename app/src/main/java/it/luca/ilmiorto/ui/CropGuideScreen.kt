package it.luca.ilmiorto.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.CROP_GUIDE
import it.luca.ilmiorto.data.CROP_GUIDE_SOURCE
import it.luca.ilmiorto.data.CROP_GUIDE_SOURCE_NOTE
import it.luca.ilmiorto.data.CONSOCIATION_ENTITIES
import it.luca.ilmiorto.data.ConsociationEntity
import it.luca.ilmiorto.data.ConsociationMatrixRelation
import it.luca.ilmiorto.data.consociationEntity
import it.luca.ilmiorto.data.consociationRelationsFor
import it.luca.ilmiorto.data.otherEntityId
import it.luca.ilmiorto.data.CropGuideItem
import it.luca.ilmiorto.data.CropHistoryEntry
import it.luca.ilmiorto.data.DEFAULT_ZONES
import it.luca.ilmiorto.data.GardenState
import it.luca.ilmiorto.data.GardenTask
import it.luca.ilmiorto.data.cropGuideForName
import it.luca.ilmiorto.data.cropGuideItem
import java.time.LocalDate

private enum class CropGuideSection(val label: String, val emoji: String) {
    COMPANIONS("Consociazioni", "🌿"),
    ROTATIONS("Rotazioni", "🔄"),
    CARE("Cura", "💧"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropGuideScreen(
    state: GardenState,
    onAddHistory: (CropHistoryEntry) -> Unit,
    onRemoveHistory: (String) -> Unit,
    onApplyDistances: (String) -> Unit,
    onAddTask: (GardenTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    var sectionName by rememberSaveable { mutableStateOf(CropGuideSection.COMPANIONS.name) }
    val section = CropGuideSection.valueOf(sectionName)
    var selectedSpeciesId by rememberSaveable { mutableStateOf("pomodoro") }
    var selectedConsociationEntityId by rememberSaveable { mutableStateOf("tomatoes") }
    var relationFilter by rememberSaveable { mutableStateOf("ALL") }
    var selectedZoneId by rememberSaveable { mutableStateOf(DEFAULT_ZONES.first().id) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    val guide = cropGuideItem(selectedSpeciesId) ?: CROP_GUIDE.first()
    val consociation = consociationEntity(selectedConsociationEntityId) ?: CONSOCIATION_ENTITIES.first()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("Colture", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Consociazioni normalizzate dall’Excel, rotazioni e indicazioni di cura.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        item {
            ScrollableTabRow(selectedTabIndex = section.ordinal) {
                CropGuideSection.entries.forEach { item ->
                    Tab(
                        selected = item == section,
                        onClick = { sectionName = item.name },
                        text = { Text("${item.emoji} ${item.label}") },
                    )
                }
            }
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (section == CropGuideSection.COMPANIONS) {
                    MatrixEntitySelector(
                        selected = consociation,
                        onSelected = { selectedConsociationEntityId = it.id },
                    )
                } else {
                    SpeciesSelector(
                        selected = guide,
                        onSelected = { selectedSpeciesId = it.id },
                    )
                }
                Spacer(Modifier.height(8.dp))
                SourceCard(section)
            }
        }

        when (section) {
            CropGuideSection.COMPANIONS -> companionItems(consociation, relationFilter) { relationFilter = it }
            CropGuideSection.ROTATIONS -> rotationItems(
                guide = guide,
                state = state,
                selectedZoneId = selectedZoneId,
                onZoneSelected = { selectedZoneId = it },
                onAddHistory = { showHistoryDialog = true },
                onRemoveHistory = onRemoveHistory,
            )
            CropGuideSection.CARE -> careItems(
                guide = guide,
                state = state,
                selectedZoneId = selectedZoneId,
                onZoneSelected = { selectedZoneId = it },
                onApplyDistances = onApplyDistances,
                onAddTask = onAddTask,
            )
        }
        item { Spacer(Modifier.height(90.dp)) }
    }

    if (showHistoryDialog) {
        AddHistoryDialog(
            defaultYear = state.seasonYear - 1,
            defaultZoneId = selectedZoneId,
            onDismiss = { showHistoryDialog = false },
            onConfirm = {
                onAddHistory(it)
                selectedZoneId = it.zoneId
                showHistoryDialog = false
            },
        )
    }
}

@Composable
private fun MatrixEntitySelector(
    selected: ConsociationEntity,
    onSelected: (ConsociationEntity) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Text("Pianta o gruppo botanico", style = MaterialTheme.typography.labelLarge)
    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
        Text("${selected.displayName}${if (selected.level == "Gruppo/famiglia") " · gruppo" else ""}")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        CONSOCIATION_ENTITIES.sortedBy { it.displayName }.forEach { item ->
            DropdownMenuItem(
                text = {
                    Text("${item.displayName}${if (item.level == "Gruppo/famiglia") " · gruppo" else ""}")
                },
                onClick = {
                    expanded = false
                    onSelected(item)
                },
            )
        }
    }
}

@Composable
private fun SpeciesSelector(selected: CropGuideItem, onSelected: (CropGuideItem) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Text("Pianta", style = MaterialTheme.typography.labelLarge)
    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
        Text("${selected.emoji} ${selected.name}")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        CROP_GUIDE.forEach { item ->
            DropdownMenuItem(
                text = { Text("${item.emoji} ${item.name}") },
                onClick = {
                    expanded = false
                    onSelected(item)
                },
            )
        }
    }
}

@Composable
private fun ZoneSelector(selectedZoneId: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val zone = DEFAULT_ZONES.firstOrNull { it.id == selectedZoneId } ?: DEFAULT_ZONES.first()
    Text("Proda o terreno", style = MaterialTheme.typography.labelLarge)
    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
        Text("${zone.name} · ${zone.dimensionsLabel}")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DEFAULT_ZONES.forEach { item ->
            DropdownMenuItem(
                text = { Text("${item.name} · ${item.dimensionsLabel}") },
                onClick = {
                    expanded = false
                    onSelected(item.id)
                },
            )
        }
    }
}

@Composable
private fun SourceCard(section: CropGuideSection) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            if (section == CropGuideSection.COMPANIONS) {
                Text("Database consociazioni normalizzato", style = MaterialTheme.typography.titleMedium)
                Text("50 entità · 224 relazioni")
                Text(
                    "Matrice unificata del file Excel: B favorevole, C da evitare, M fonti discordanti. " +
                        "Le celle vuote significano soltanto nessuna relazione documentata. " +
                        "La matrice è simmetrica e non conserva la direzione originaria Helps/Helped by.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text("Fonte pratica per Cura e Rotazioni", style = MaterialTheme.typography.titleMedium)
                Text(CROP_GUIDE_SOURCE)
                Text(CROP_GUIDE_SOURCE_NOTE, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.companionItems(
    entity: ConsociationEntity,
    filter: String,
    onFilterChanged: (String) -> Unit,
) {
    val allRelations = consociationRelationsFor(entity.id)
        .sortedWith(compareBy<ConsociationMatrixRelation>({ when (it.code) { "B" -> 0; "C" -> 1; else -> 2 } }, {
            consociationEntity(it.otherEntityId(entity.id))?.displayName.orEmpty()
        }))
    val relations = if (filter == "ALL") allRelations else allRelations.filter { it.code == filter }
    val good = allRelations.count { it.code == "B" }
    val bad = allRelations.count { it.code == "C" }
    val mixed = allRelations.count { it.code == "M" }

    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(entity.displayName, style = MaterialTheme.typography.titleLarge)
                Text("${entity.level} · ${allRelations.size} relazioni documentate")
                Text("$good favorevoli · $bad da evitare · $mixed discordanti")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("ALL" to "Tutte", "B" to "B", "C" to "C", "M" to "M").forEach { option ->
                        TextButton(onClick = { onFilterChanged(option.first) }) {
                            Text(if (filter == option.first) "✓ ${option.second}" else option.second)
                        }
                    }
                }
            }
        }
    }
    if (relations.isEmpty()) {
        item { Text("Nessuna relazione nel filtro selezionato.", modifier = Modifier.padding(16.dp)) }
    } else {
        items(relations.size) { index ->
            val relation = relations[index]
            val other = consociationEntity(relation.otherEntityId(entity.id))
            Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(other?.displayName ?: relation.otherEntityId(entity.id), style = MaterialTheme.typography.titleMedium)
                    Text("${relation.code} · ${relation.label}", style = MaterialTheme.typography.labelLarge)
                    Text(
                        when (relation.code) {
                            "B" -> "Preferenza agronomica: non è un obbligo rigido."
                            "C" -> "Avviso forte: non è un divieto assoluto, ma non viene proposta come combinazione consigliata."
                            else -> "Fonti discordanti: la relazione resta informativa e non guida la disposizione automatica."
                        },
                    )
                    Text("Uso nell’app: ${relation.appPolicy}")
                    Text("Fonte: ${relation.source}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.rotationItems(
    guide: CropGuideItem,
    state: GardenState,
    selectedZoneId: String,
    onZoneSelected: (String) -> Unit,
    onAddHistory: () -> Unit,
    onRemoveHistory: (String) -> Unit,
) {
    val history = state.history.filter { it.zoneId == selectedZoneId }.sortedByDescending { it.year }
    val assessment = rotationAssessment(guide, history.firstOrNull(), state.seasonYear)
    item {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            ZoneSelector(selectedZoneId, onZoneSelected)
        }
    }
    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text("${guide.emoji} ${guide.name}", style = MaterialTheme.typography.titleLarge)
                Text("Famiglia: ${guide.familyCode}")
                Text("Tempo di ritorno: ${guide.returnYears} anni")
                Text("Non trapiantare dopo: ${guide.avoidAfter.ifEmpty { listOf("Non indicato") }.joinToString()}")
                Text("Favorevole dopo: ${guide.favorableAfter.ifEmpty { listOf("Non indicato") }.joinToString()}")
            }
        }
    }
    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(assessment.first, style = MaterialTheme.typography.titleMedium)
                Text(assessment.second)
            }
        }
    }
    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Storico della zona", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = onAddHistory) { Text("+ Registra") }
                }
                if (history.isEmpty()) Text("Nessuna stagione precedente registrata.")
                history.forEach { entry ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${entry.year} · ${entry.speciesName}")
                        TextButton(onClick = { onRemoveHistory(entry.id) }) { Text("Elimina") }
                    }
                }
            }
        }
    }
}

private fun rotationAssessment(guide: CropGuideItem, previous: CropHistoryEntry?, currentYear: Int): Pair<String, String> {
    if (previous == null) return "Storico non disponibile" to "Registra almeno una coltura passata per valutare questa proda."
    val previousGuide = cropGuideItem(previous.speciesId) ?: cropGuideForName(previous.speciesName)
        ?: return "Coltura precedente non riconosciuta" to "La coltura ${previous.speciesName} non è collegata alla banca dati."
    val gap = (currentYear - previous.year).coerceAtLeast(0)
    if (previousGuide.familyCode == guide.familyCode && gap < guide.returnYears) {
        return "Rotazione sconsigliata" to "Nel ${previous.year} c’era ${previousGuide.name}, della famiglia ${previousGuide.familyCode}; sono trascorsi $gap anni rispetto ai ${guide.returnYears} indicati."
    }
    val previousText = (previousGuide.name + " " + previousGuide.familyCode).uppercase()
    if (guide.avoidAfter.any { previousText.contains(it) || it.contains(previousGuide.name.uppercase()) }) {
        return "Precessione sconsigliata" to "La tabella indica di non trapiantare ${guide.name} dopo ${previousGuide.name} o il relativo gruppo."
    }
    if (guide.favorableAfter.any { previousText.contains(it) || it.contains(previousGuide.name.uppercase()) }) {
        return "Precessione favorevole" to "${previousGuide.name} rientra fra le precessioni favorevoli riportate per ${guide.name}."
    }
    return "Rotazione non classificata" to "La tabella non classifica esplicitamente il passaggio ${previousGuide.name} → ${guide.name}."
}

private fun androidx.compose.foundation.lazy.LazyListScope.careItems(
    guide: CropGuideItem,
    state: GardenState,
    selectedZoneId: String,
    onZoneSelected: (String) -> Unit,
    onApplyDistances: (String) -> Unit,
    onAddTask: (GardenTask) -> Unit,
) {
    val zone = DEFAULT_ZONES.firstOrNull { it.id == selectedZoneId } ?: DEFAULT_ZONES.first()
    val denseSowingIds = setOf(
        "rucola", "lattughini_da_taglio", "bietoline_da_taglio",
        "spinacio", "valeriana", "prezzemolo",
    )
    val isDenseSowing = guide.id in denseSowingIds
    val rows = ((zone.widthMeters * 100.0) / guide.rowSpacingCm).toInt().coerceAtLeast(1)
    val perRow = ((zone.lengthMeters * 100.0) / guide.plantSpacingCm).toInt().coerceAtLeast(1)
    val total = rows * perRow
    val linearMeters = rows * zone.lengthMeters
    val matches = state.crops.count { cropGuideForName(it.name)?.id == guide.id }
    item {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) { ZoneSelector(selectedZoneId, onZoneSelected) }
    }
    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("${guide.emoji} ${guide.name}", style = MaterialTheme.typography.titleLarge)
                Text("Distanze riportate nella tabella", style = MaterialTheme.typography.titleMedium)
                Text("Tra le file: ${guide.rowSpacingCm.toInt()} cm")
                Text("Sulla fila / diradamento: ${guide.plantSpacingCm.toInt()} cm")
                Text("Mezz’ombra: ${when (guide.toleratesHalfShade) { true -> "Sì"; false -> "No"; null -> "Non indicato" }}")
                Text(
                    "I valori della fonte non vengono più trasformati automaticamente in un numero di piante senza considerare la modalità di coltivazione.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                if (isDenseSowing) {
                    Text("Impianto pratico", style = MaterialTheme.typography.titleMedium)
                    Text("$rows file · ${"%.1f".format(linearMeters)} m lineari di semina")
                    Text(
                        "Per ${guide.name} non mostriamo un conteggio di piantine: semina, diradamento e raccolta a taglio rendono il numero individuale fuorviante.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Text("Stima geometrica", style = MaterialTheme.typography.titleMedium)
                    Text("$total posizioni ($rows ${if (rows == 1) "fila" else "file"} × $perRow)")
                    Text(
                        "È una stima di collocazione, da verificare con varietà, ingombro adulto, sostegno, accessi e consociazioni.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (guide.id == "pomodoro") {
                        Text(
                            "Nella proda larga 1,20 m, i 100 cm tra file della tabella corrispondono normalmente a una sola fila centrale; non vengono interpretati come due file interne distanti 100 cm.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("💧 Irrigazione dopo l’attecchimento", style = MaterialTheme.typography.titleMedium)
                Text(guide.irrigationAfterEstablishment)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = {
                    onAddTask(
                        GardenTask(
                            title = "Irrigazione: ${guide.name}",
                            category = "Irrigazione",
                            notes = guide.irrigationAfterEstablishment,
                        )
                    )
                }) { Text("Crea attività") }
            }
        }
    }
    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("🌱 Concimazione", style = MaterialTheme.typography.titleMedium)
                Text("Organica: ${guide.organicQuantity.lowercase()}")
                if (guide.fertilizeInHole) Text("• In buca al trapianto")
                if (guide.fertilizeMonthly) Text("• Mensile dopo il trapianto")
                if (guide.reduceBeforeHarvest) Text("• Riduzione prima della raccolta")
                if (guide.suspendBeforeHarvest) Text("• Sospensione prima della raccolta")
                OutlinedButton(onClick = {
                    val notes = buildList {
                        add("Concimazione organica: ${guide.organicQuantity.lowercase()}.")
                        if (guide.fertilizeInHole) add("In buca al trapianto.")
                        if (guide.fertilizeMonthly) add("Mensile dopo il trapianto.")
                        if (guide.reduceBeforeHarvest) add("Ridurre prima della raccolta.")
                        if (guide.suspendBeforeHarvest) add("Sospendere prima della raccolta.")
                    }.joinToString(" ")
                    onAddTask(GardenTask(title = "Concimazione: ${guide.name}", category = "Concimazione", notes = notes))
                }) { Text("Crea attività") }
            }
        }
    }
    item {
        Card(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Applica all’orto", style = MaterialTheme.typography.titleMedium)
                Text("Colture riconosciute presenti: $matches")
                Button(
                    enabled = matches > 0,
                    onClick = { onApplyDistances(guide.id) },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Applica ${guide.plantSpacingCm.toInt()}/${guide.rowSpacingCm.toInt()} cm") }
            }
        }
    }
}

@Composable
private fun AddHistoryDialog(
    defaultYear: Int,
    defaultZoneId: String,
    onDismiss: () -> Unit,
    onConfirm: (CropHistoryEntry) -> Unit,
) {
    var yearText by remember { mutableStateOf(defaultYear.toString()) }
    var zoneId by remember { mutableStateOf(defaultZoneId) }
    var speciesId by remember { mutableStateOf("pomodoro") }
    val species = cropGuideItem(speciesId) ?: CROP_GUIDE.first()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registra una coltura passata") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = yearText, onValueChange = { yearText = it }, label = { Text("Anno") })
                ZoneSelector(zoneId) { zoneId = it }
                SpeciesSelector(species) { speciesId = it.id }
            }
        },
        confirmButton = {
            Button(onClick = {
                val year = yearText.toIntOrNull() ?: (LocalDate.now().year - 1)
                onConfirm(CropHistoryEntry(year = year, zoneId = zoneId, speciesId = species.id, speciesName = species.name))
            }) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
    )
}
