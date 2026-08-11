package it.luca.ilmiorto.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.luca.ilmiorto.data.Crop
import it.luca.ilmiorto.data.cropPlacementRole
import it.luca.ilmiorto.data.DEFAULT_ZONES
import it.luca.ilmiorto.data.GardenState
import it.luca.ilmiorto.data.GardenZone

@Composable
fun MapScreen(
    state: GardenState,
    onAddCrop: (String) -> Unit,
    onRemoveCrop: (String) -> Unit,
    onUpdateCrop: (Crop) -> Unit,
    onAutoArrangeCrop: (String) -> Unit,
    onRemovePlant: (cropId: String, plantId: String) -> Unit,
    onMovePlant: (cropId: String, plantId: String, xFraction: Double, yFraction: Double) -> Unit,
    onDetailZoneChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var detailZoneId by rememberSaveable { mutableStateOf<String?>(null) }
    val zoneId = detailZoneId

    if (zoneId == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionTitle(
                title = "Mappa dell’orto",
                subtitle = "Tocca una proda o un terreno per aprire la sua schermata dedicata.",
            )

            GardenPlan(
                state = state,
                selectedZoneId = "",
                onZoneSelected = { selected ->
                    detailZoneId = selected
                    onDetailZoneChanged(selected)
                },
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                ),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Superficie coltivabile censita", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Circa ${String.format(java.util.Locale.ITALY, "%.1f", DEFAULT_ZONES.sumOf { it.areaSquareMeters })} m² complessivi nelle sette sezioni configurate.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Ogni zona dispone di una videata propria con piante, distanze e colture.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    } else {
        val selectedZone = DEFAULT_ZONES.first { it.id == zoneId }
        val selectedCrops = state.crops.filter { it.zoneId == zoneId }

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TextButton(
                onClick = {
                    detailZoneId = null
                    onDetailZoneChanged("")
                },
            ) {
                Text("‹ Torna alla mappa")
            }

            SectionTitle(
                title = selectedZone.name,
                subtitle = "${selectedZone.dimensionsLabel} · ${String.format(java.util.Locale.ITALY, "%.2f", selectedZone.areaSquareMeters)} m²",
            )

            SelectedZoneDetails(
                zone = selectedZone,
                crops = selectedCrops,
                onAddCrop = { onAddCrop(zoneId) },
                onRemoveCrop = onRemoveCrop,
                onUpdateCrop = onUpdateCrop,
                onAutoArrangeCrop = onAutoArrangeCrop,
                onRemovePlant = onRemovePlant,
                onMovePlant = onMovePlant,
            )
        }
    }
}

@Composable
private fun GardenPlan(
    state: GardenState,
    selectedZoneId: String,
    onZoneSelected: (String) -> Unit,
) {
    val zoneById = DEFAULT_ZONES.associateBy { it.id }
    fun count(id: String) = state.crops.count { it.zoneId == id }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    listOf("proda_1", "proda_2", "proda_3", "proda_4").forEach { id ->
                        val zone = zoneById.getValue(id)
                        SelectableZoneCard(
                            zone = zone,
                            cropCount = count(id),
                            selected = id == selectedZoneId,
                            onClick = { onZoneSelected(id) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                SelectableZoneCard(
                    zone = zoneById.getValue("striscia"),
                    cropCount = count("striscia"),
                    selected = selectedZoneId == "striscia",
                    onClick = { onZoneSelected("striscia") },
                    modifier = Modifier
                        .weight(1.35f)
                        .height(338.dp),
                )
            }

            Text(
                "Area a L — un’unica area continua",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            LShapedArea(
                verticalZone = zoneById.getValue("area_l_lungo"),
                horizontalZone = zoneById.getValue("area_l_corto"),
                verticalCropCount = count("area_l_lungo"),
                horizontalCropCount = count("area_l_corto"),
                selectedZoneId = selectedZoneId,
                onZoneSelected = onZoneSelected,
            )
        }
    }
}

@Composable
private fun LShapedArea(
    verticalZone: GardenZone,
    horizontalZone: GardenZone,
    verticalCropCount: Int,
    horizontalCropCount: Int,
    selectedZoneId: String,
    onZoneSelected: (String) -> Unit,
) {
    val outlineColor = MaterialTheme.colorScheme.outline
    val baseColor = MaterialTheme.colorScheme.surface
    val selectedColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.82f)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(245.dp),
    ) {
        val verticalWidth = maxWidth * (1.2f / (1.2f + 2.6f))
        val horizontalHeight = maxHeight * (0.8f / 2.8f)

        Canvas(modifier = Modifier.fillMaxSize()) {
            val verticalX = size.width * (1.2f / (1.2f + 2.6f))
            val topArmBottom = size.height * (0.8f / 2.8f)
            val lPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, topArmBottom)
                lineTo(verticalX, topArmBottom)
                lineTo(verticalX, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(lPath, color = baseColor)
        }

        LClickableSegment(
            zone = verticalZone,
            shortTitle = "Braccio verticale",
            cropCount = verticalCropCount,
            selected = selectedZoneId == verticalZone.id,
            onClick = { onZoneSelected(verticalZone.id) },
            selectedColor = selectedColor,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .width(verticalWidth)
                .height(maxHeight),
        )

        LClickableSegment(
            zone = horizontalZone,
            shortTitle = "Braccio corto in alto",
            cropCount = horizontalCropCount,
            selected = selectedZoneId == horizontalZone.id,
            onClick = { onZoneSelected(horizontalZone.id) },
            selectedColor = selectedColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(maxWidth - verticalWidth)
                .height(horizontalHeight),
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val verticalX = size.width * (1.2f / (1.2f + 2.6f))
            val topArmBottom = size.height * (0.8f / 2.8f)
            val lPath = Path().apply {
                moveTo(1f, 1f)
                lineTo(size.width - 1f, 1f)
                lineTo(size.width - 1f, topArmBottom)
                lineTo(verticalX, topArmBottom)
                lineTo(verticalX, size.height - 1f)
                lineTo(1f, size.height - 1f)
                close()
            }
            drawPath(lPath, color = outlineColor, style = Stroke(width = 2f))
        }
    }
}

@Composable
private fun LClickableSegment(
    zone: GardenZone,
    shortTitle: String,
    cropCount: Int,
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(if (selected) selectedColor else androidx.compose.ui.graphics.Color.Transparent)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            shortTitle,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(zone.dimensionsLabel, style = MaterialTheme.typography.bodySmall)
        Text(
            if (cropCount == 0) "Vuota" else "$cropCount colture",
            style = MaterialTheme.typography.labelSmall,
            color = if (cropCount == 0) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.primary
            },
        )
    }
}

@Composable
private fun SelectedZoneDetails(
    zone: GardenZone,
    crops: List<Crop>,
    onAddCrop: () -> Unit,
    onRemoveCrop: (String) -> Unit,
    onUpdateCrop: (Crop) -> Unit,
    onAutoArrangeCrop: (String) -> Unit,
    onRemovePlant: (cropId: String, plantId: String) -> Unit,
    onMovePlant: (cropId: String, plantId: String, xFraction: Double, yFraction: Double) -> Unit,
) {
    var editingCrop by remember { mutableStateOf<Crop?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PlantingPlanner(
            zone = zone,
            crops = crops,
            onMovePlant = onMovePlant,
            onRemovePlant = onRemovePlant,
        )

        Card {
            Column(Modifier.padding(16.dp)) {
                Text(zone.name, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${zone.dimensionsLabel} · ${String.format(java.util.Locale.ITALY, "%.2f", zone.areaSquareMeters)} m²",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(14.dp))

                if (crops.isEmpty()) {
                    Text(
                        "Non ci sono ancora colture assegnate a questa zona.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    crops.forEachIndexed { index, crop ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(crop.name, style = MaterialTheme.typography.titleMedium)
                                val details = buildList {
                                    if (crop.variety.isNotBlank()) add(crop.variety)
                                    if (crop.plantCount > 0) add("${crop.plantCount} piante")
                                    add("icona Ø ${String.format(java.util.Locale.ITALY, "%.0f", crop.iconDiameterCm)} cm")
                                    add("distanze ${String.format(java.util.Locale.ITALY, "%.0f", crop.plantSpacingCm)}/${String.format(java.util.Locale.ITALY, "%.0f", crop.rowSpacingCm)} cm")
                                }.joinToString(" · ")
                                if (details.isNotBlank()) {
                                    Text(details, style = MaterialTheme.typography.bodySmall)
                                }
                                Text(
                                    "Posizione preferita: ${cropPlacementRole(crop).label}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                if (crop.transplantDate.isNotBlank()) {
                                    Text(
                                        "Trapianto: ${crop.transplantDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                TextButton(onClick = { editingCrop = crop }) { Text("Modifica") }
                                TextButton(onClick = { onAutoArrangeCrop(crop.id) }) { Text("Progetta la proda") }
                                TextButton(onClick = { onRemoveCrop(crop.id) }) { Text("Elimina") }
                            }
                        }
                        if (index < crops.lastIndex) {
                            HorizontalDivider(Modifier.padding(vertical = 10.dp))
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))
                Button(onClick = onAddCrop, modifier = Modifier.fillMaxWidth()) {
                    Text("Aggiungi una coltura qui")
                }
            }
        }
    }

    editingCrop?.let { crop ->
        EditCropDialog(
            crop = crop,
            onDismiss = { editingCrop = null },
            onConfirm = {
                onUpdateCrop(it)
                editingCrop = null
            },
        )
    }
}
