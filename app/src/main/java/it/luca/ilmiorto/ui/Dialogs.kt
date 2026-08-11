package it.luca.ilmiorto.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.CROP_CATALOG
import it.luca.ilmiorto.data.Crop
import it.luca.ilmiorto.data.DEFAULT_ZONES
import it.luca.ilmiorto.data.GardenState
import it.luca.ilmiorto.data.GardenTask
import it.luca.ilmiorto.data.TASK_CATEGORIES
import it.luca.ilmiorto.data.isIsoDate
import it.luca.ilmiorto.data.automaticPlantPositions
import it.luca.ilmiorto.data.catalogItem
import it.luca.ilmiorto.data.inferCatalog
import it.luca.ilmiorto.data.initialPlantPositions
import java.time.LocalDate

@Composable
fun AddCropDialog(
    state: GardenState,
    initialZoneId: String,
    onDismiss: () -> Unit,
    onConfirm: (Crop) -> Unit,
) {
    var zoneId by remember(initialZoneId) { mutableStateOf(initialZoneId.ifBlank { DEFAULT_ZONES.first().id }) }
    var catalogId by remember { mutableStateOf("custom") }
    var name by remember { mutableStateOf("") }
    var variety by remember { mutableStateOf("") }
    var plantCount by remember { mutableStateOf("3") }
    var iconDiameterCm by remember { mutableStateOf("20") }
    var plantSpacingCm by remember { mutableStateOf("30") }
    var rowSpacingCm by remember { mutableStateOf("40") }
    var sowingDate by remember { mutableStateOf("") }
    var transplantDate by remember { mutableStateOf("") }
    var expectedHarvestDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val selectedCatalog = catalogItem(catalogId)
    val dateValid = isIsoDate(sowingDate) && isIsoDate(transplantDate) && isIsoDate(expectedHarvestDate)
    val canSave = name.isNotBlank() && dateValid

    fun selectCatalog(itemName: String) {
        val item = CROP_CATALOG.firstOrNull { it.name == itemName } ?: return
        catalogId = item.id
        name = item.name
        iconDiameterCm = item.iconDiameterCm.toString().replace('.', ',')
        plantSpacingCm = item.plantSpacingCm.toString().replace('.', ',')
        rowSpacingCm = item.rowSpacingCm.toString().replace('.', ',')
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuova coltura") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("Zona")
                ZoneChoiceChips(DEFAULT_ZONES, zoneId, { zoneId = it })
                Text("Catalogo colture")
                ChoiceChips(
                    choices = CROP_CATALOG.map { it.name },
                    selected = selectedCatalog?.name.orEmpty(),
                    onSelected = ::selectCatalog,
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        catalogId = inferCatalog(it)?.id ?: "custom"
                    },
                    label = { Text("Coltura *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text("Varietà") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                if (!selectedCatalog?.varieties.isNullOrEmpty()) {
                    ChoiceChips(selectedCatalog!!.varieties, variety, { variety = it })
                }
                NumberField(plantCount, { plantCount = it }, "Numero piante", Modifier.fillMaxWidth())
                NumberField(iconDiameterCm, { iconDiameterCm = it }, "Diametro icona (cm)", Modifier.fillMaxWidth())
                NumberField(plantSpacingCm, { plantSpacingCm = it }, "Distanza tra piante (cm)", Modifier.fillMaxWidth())
                NumberField(rowSpacingCm, { rowSpacingCm = it }, "Distanza tra file (cm)", Modifier.fillMaxWidth())
                Text("L'app crea una disposizione automatica. Dimensione e distanze restano comuni alle piante con lo stesso nome e la stessa varietà.")
                DateInput("Data di semina", sowingDate, { sowingDate = it })
                DateInput("Data di trapianto", transplantDate, { transplantDate = it })
                DateInput("Raccolta prevista", expectedHarvestDate, { expectedHarvestDate = it })
                OutlinedTextField(notes, { notes = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                if (!dateValid) Text("Le date devono essere nel formato AAAA-MM-GG.")
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    val parsedCount = (plantCount.toIntOrNull() ?: 0).coerceIn(0, 100)
                    val catalog = catalogItem(catalogId) ?: inferCatalog(name)
                    val existing = state.crops.firstOrNull {
                        it.name.trim().equals(name.trim(), ignoreCase = true) &&
                            it.variety.trim().equals(variety.trim(), ignoreCase = true)
                    }
                    val diameter = existing?.iconDiameterCm
                        ?: iconDiameterCm.replace(',', '.').toDoubleOrNull()?.coerceIn(5.0, 100.0)
                        ?: catalog?.iconDiameterCm ?: 20.0
                    val plantSpacing = existing?.plantSpacingCm
                        ?: plantSpacingCm.replace(',', '.').toDoubleOrNull()?.coerceIn(5.0, 300.0)
                        ?: catalog?.plantSpacingCm ?: 30.0
                    val rowSpacing = existing?.rowSpacingCm
                        ?: rowSpacingCm.replace(',', '.').toDoubleOrNull()?.coerceIn(5.0, 300.0)
                        ?: catalog?.rowSpacingCm ?: 40.0
                    onConfirm(
                        Crop(
                            zoneId = zoneId,
                            name = name.trim(),
                            variety = variety.trim(),
                            plantCount = parsedCount,
                            sowingDate = sowingDate.trim(),
                            transplantDate = transplantDate.trim(),
                            expectedHarvestDate = expectedHarvestDate.trim(),
                            notes = notes.trim(),
                            catalogId = catalog?.id ?: "custom",
                            iconColorHex = catalog?.colorHex ?: "#C9E2BD",
                            iconDiameterCm = diameter,
                            plantSpacingCm = plantSpacing,
                            rowSpacingCm = rowSpacing,
                            plants = automaticPlantPositions(zoneId, parsedCount, diameter, plantSpacing, rowSpacing),
                        )
                    )
                },
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
    )
}

@Composable
fun EditCropDialog(
    crop: Crop,
    onDismiss: () -> Unit,
    onConfirm: (Crop) -> Unit,
) {
    var catalogId by remember(crop.id) { mutableStateOf(crop.catalogId) }
    var name by remember(crop.id) { mutableStateOf(crop.name) }
    var variety by remember(crop.id) { mutableStateOf(crop.variety) }
    var plantCount by remember(crop.id) { mutableStateOf(crop.plantCount.toString()) }
    var iconDiameterCm by remember(crop.id) { mutableStateOf(crop.iconDiameterCm.toString().replace('.', ',')) }
    var plantSpacingCm by remember(crop.id) { mutableStateOf(crop.plantSpacingCm.toString().replace('.', ',')) }
    var rowSpacingCm by remember(crop.id) { mutableStateOf(crop.rowSpacingCm.toString().replace('.', ',')) }
    val parsedCount = (plantCount.toIntOrNull() ?: 0).coerceIn(0, 100)
    val selectedCatalog = catalogItem(catalogId)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modifica coltura") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("Catalogo colture")
                ChoiceChips(
                    choices = CROP_CATALOG.map { it.name },
                    selected = selectedCatalog?.name.orEmpty(),
                    onSelected = { selectedName ->
                        CROP_CATALOG.firstOrNull { it.name == selectedName }?.let { item ->
                            catalogId = item.id
                            name = item.name
                            iconDiameterCm = item.iconDiameterCm.toString().replace('.', ',')
                            plantSpacingCm = item.plantSpacingCm.toString().replace('.', ',')
                            rowSpacingCm = item.rowSpacingCm.toString().replace('.', ',')
                        }
                    },
                )
                OutlinedTextField(name, { name = it; catalogId = inferCatalog(it)?.id ?: "custom" }, label = { Text("Coltura *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(variety, { variety = it }, label = { Text("Varietà") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (!selectedCatalog?.varieties.isNullOrEmpty()) ChoiceChips(selectedCatalog!!.varieties, variety, { variety = it })
                NumberField(plantCount, { plantCount = it }, "Numero piante", Modifier.fillMaxWidth())
                NumberField(iconDiameterCm, { iconDiameterCm = it }, "Diametro icona (cm)", Modifier.fillMaxWidth())
                NumberField(plantSpacingCm, { plantSpacingCm = it }, "Distanza tra piante (cm)", Modifier.fillMaxWidth())
                NumberField(rowSpacingCm, { rowSpacingCm = it }, "Distanza tra file (cm)", Modifier.fillMaxWidth())
                Text("Le posizioni non cambiano finché non usi il comando di disposizione automatica.")
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = {
                    val resizedPlants = when {
                        parsedCount < crop.plants.size -> crop.plants.take(parsedCount)
                        parsedCount > crop.plants.size -> crop.plants + initialPlantPositions(parsedCount - crop.plants.size)
                        else -> crop.plants
                    }
                    val catalog = catalogItem(catalogId) ?: inferCatalog(name)
                    onConfirm(
                        crop.copy(
                            name = name.trim(),
                            variety = variety.trim(),
                            plantCount = parsedCount,
                            catalogId = catalog?.id ?: "custom",
                            iconColorHex = catalog?.colorHex ?: crop.iconColorHex,
                            iconDiameterCm = iconDiameterCm.replace(',', '.').toDoubleOrNull()?.coerceIn(5.0, 100.0) ?: crop.iconDiameterCm,
                            plantSpacingCm = plantSpacingCm.replace(',', '.').toDoubleOrNull()?.coerceIn(5.0, 300.0) ?: crop.plantSpacingCm,
                            rowSpacingCm = rowSpacingCm.replace(',', '.').toDoubleOrNull()?.coerceIn(5.0, 300.0) ?: crop.rowSpacingCm,
                            plants = resizedPlants,
                        )
                    )
                },
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
    )
}

@Composable
fun AddTaskDialog(
    state: GardenState,
    onDismiss: () -> Unit,
    onConfirm: (GardenTask) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var category by remember { mutableStateOf("Irrigazione") }
    var zoneId by remember { mutableStateOf("") }
    var cropId by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val cropsInZone = if (zoneId.isBlank()) state.crops else state.crops.filter { it.zoneId == zoneId }
    val cropChoices = listOf("Nessuna") + cropsInZone.map { it.name }
    val selectedCropName = state.crops.firstOrNull { it.id == cropId }?.name ?: "Nessuna"
    val canSave = title.isNotBlank() && isIsoDate(date) && date.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuova attività") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Attività *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                DateInput("Data *", date, { date = it })
                Text("Categoria")
                ChoiceChips(TASK_CATEGORIES, category, { category = it })
                Text("Zona")
                ZoneChoiceChips(
                    zones = DEFAULT_ZONES,
                    selectedId = zoneId,
                    onSelected = {
                        zoneId = it
                        if (state.crops.none { crop -> crop.id == cropId && (zoneId.isBlank() || crop.zoneId == zoneId) }) {
                            cropId = ""
                        }
                    },
                    allowNone = true,
                )
                if (cropsInZone.isNotEmpty()) {
                    Text("Coltura")
                    ChoiceChips(
                        choices = cropChoices,
                        selected = selectedCropName,
                        onSelected = { selected ->
                            cropId = if (selected == "Nessuna") "" else cropsInZone.first { it.name == selected }.id
                        },
                    )
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Note") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                )
                if (!isIsoDate(date)) Text("La data deve essere nel formato AAAA-MM-GG.")
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    onConfirm(
                        GardenTask(
                            title = title.trim(),
                            date = date.trim(),
                            category = category,
                            zoneId = zoneId,
                            cropId = cropId,
                            notes = notes.trim(),
                        )
                    )
                },
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
    )
}

@Composable
private fun DateInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text("AAAA-MM-GG") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = value.isNotBlank() && !isIsoDate(value),
    )
}
