package it.luca.ilmiorto.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consume
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.luca.ilmiorto.data.Crop
import it.luca.ilmiorto.data.GardenZone
import it.luca.ilmiorto.data.PlantPosition
import it.luca.ilmiorto.data.inferCatalog
import it.luca.ilmiorto.data.spacingWarnings
import java.util.Locale
import kotlin.math.hypot
import kotlin.math.roundToInt

private data class PlantVisual(
    val crop: Crop,
    val plant: PlantPosition,
    val number: Int,
)

private data class PlantDistanceInfo(
    val leftMeters: Double,
    val rightMeters: Double,
    val topMeters: Double,
    val bottomMeters: Double,
    val nearestPlant: PlantVisual?,
    val nearestPlantMeters: Double?,
)

@Composable
fun PlantingPlanner(
    zone: GardenZone,
    crops: List<Crop>,
    onMovePlant: (cropId: String, plantId: String, xFraction: Double, yFraction: Double) -> Unit,
    onRemovePlant: (cropId: String, plantId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var livePositions by remember(zone.id) {
        mutableStateOf<Map<String, Pair<Double, Double>>>(emptyMap())
    }
    val plants = crops.flatMap { crop ->
        crop.plants.mapIndexed { index, plant ->
            val live = livePositions[plant.id]
            PlantVisual(
                crop = crop,
                plant = if (live == null) plant else plant.copy(
                    xFraction = live.first,
                    yFraction = live.second,
                ),
                number = index + 1,
            )
        }
    }
    val warnings = spacingWarnings(zone, crops)

    var selectedPlantId by rememberSaveable(zone.id) {
        mutableStateOf(plants.firstOrNull()?.plant?.id.orEmpty())
    }
    var editingPlant by remember { mutableStateOf<PlantVisual?>(null) }
    var removingPlant by remember { mutableStateOf<PlantVisual?>(null) }

    LaunchedEffect(zone.id, plants.map { it.plant.id }) {
        if (plants.none { it.plant.id == selectedPlantId }) {
            selectedPlantId = plants.firstOrNull()?.plant?.id.orEmpty()
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Disposizione delle piante", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Griglia da 10 cm, con linee più marcate ogni 50 cm.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    zone.dimensionsLabel,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }

            if (plants.isEmpty()) {
                Text(
                    "Inserisci il numero di piante: qui compariranno le icone da collocare.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 18.dp),
                )
            } else {
                val overlapCount = warnings.count { it.overlap }
                val closeCount = warnings.size - overlapCount
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            overlapCount > 0 -> Color(0xFFFDE8E7)
                            closeCount > 0 -> Color(0xFFFFF3CD)
                            else -> Color(0xFFE7F5E4)
                        },
                    ),
                ) {
                    Text(
                        text = if (warnings.isEmpty()) {
                            "✓ Nessuna sovrapposizione tra le piante e nessuna distanza sotto il valore consigliato."
                        } else {
                            "⚠ $overlapCount sovrapposizioni · $closeCount distanze sotto consiglio"
                        },
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                PlantBoard(
                    zone = zone,
                    plants = plants,
                    selectedPlantId = selectedPlantId,
                    onSelectPlant = { selectedPlantId = it },
                    onPreviewPlant = { plantId, x, y ->
                        livePositions = livePositions + (plantId to (x to y))
                    },
                    onCommitPlant = { cropId, plantId, x, y ->
                        onMovePlant(cropId, plantId, x, y)
                        livePositions = livePositions - plantId
                    },
                )

                val selected = plants.firstOrNull { it.plant.id == selectedPlantId } ?: plants.first()
                PlantMeasurements(
                    zone = zone,
                    selected = selected,
                    allPlants = plants,
                    onEditPosition = { editingPlant = selected },
                    onRemove = { removingPlant = selected },
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    crops.forEach { crop ->
                        Text(
                            "${cropEmoji(crop.name)} ${crop.name} · ${crop.plants.size} · " +
                                "${formatInputNumber(crop.plantSpacingCm)}/${formatInputNumber(crop.rowSpacingCm)} cm",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(50),
                                )
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(50),
                                )
                                .padding(horizontal = 9.dp, vertical = 5.dp),
                        )
                    }
                }
            }
        }
    }

    editingPlant?.let { target ->
        PlantPositionDialog(
            zone = zone,
            target = target,
            onDismiss = { editingPlant = null },
            onConfirm = { x, y ->
                onMovePlant(target.crop.id, target.plant.id, x, y)
                livePositions = livePositions - target.plant.id
                editingPlant = null
            },
        )
    }

    removingPlant?.let { target ->
        AlertDialog(
            onDismissRequest = { removingPlant = null },
            title = { Text("Rimuovere la pianta?") },
            text = {
                Text(
                    "Verrà rimossa soltanto ${cropEmoji(target.crop.name)} " +
                        "${target.crop.name} ${target.number}, non l’intera coltura."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemovePlant(target.crop.id, target.plant.id)
                        livePositions = livePositions - target.plant.id
                        removingPlant = null
                    },
                ) { Text("Rimuovi") }
            },
            dismissButton = {
                TextButton(onClick = { removingPlant = null }) { Text("Annulla") }
            },
        )
    }
}

@Composable
private fun PlantBoard(
    zone: GardenZone,
    plants: List<PlantVisual>,
    selectedPlantId: String,
    onSelectPlant: (String) -> Unit,
    onPreviewPlant: (plantId: String, xFraction: Double, yFraction: Double) -> Unit,
    onCommitPlant: (cropId: String, plantId: String, xFraction: Double, yFraction: Double) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val ratio = (zone.widthMeters / zone.lengthMeters).toFloat().coerceAtLeast(0.1f)
        val preferredWidth = minOf(maxWidth, 300.dp)
        val naturalHeight = preferredWidth / ratio
        val boardHeight = minOf(naturalHeight, 440.dp)
        val boardWidth = if (naturalHeight > 440.dp) 440.dp * ratio else preferredWidth

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            var boardSize by remember { mutableStateOf(IntSize.Zero) }
            val selected = plants.firstOrNull { it.plant.id == selectedPlantId }

            Box(
                modifier = Modifier
                    .width(boardWidth)
                    .height(boardHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFB98455))
                    .border(4.dp, Color(0xFF735337), RoundedCornerShape(12.dp))
                    .onSizeChanged { boardSize = it },
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val xTenths = (zone.widthMeters * 10).roundToInt().coerceAtLeast(1)
                    val yTenths = (zone.lengthMeters * 10).roundToInt().coerceAtLeast(1)
                    for (step in 1 until xTenths) {
                        val x = size.width * step / xTenths.toFloat()
                        val major = step % 5 == 0
                        drawLine(
                            color = Color.White.copy(alpha = if (major) 0.48f else 0.18f),
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = if (major) 2.2f else 1f,
                        )
                    }
                    for (step in 1 until yTenths) {
                        val y = size.height * step / yTenths.toFloat()
                        val major = step % 5 == 0
                        drawLine(
                            color = Color.White.copy(alpha = if (major) 0.48f else 0.18f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = if (major) 2.2f else 1f,
                        )
                    }

                    if (selected != null) {
                        val sx = size.width * selected.plant.xFraction.toFloat()
                        val sy = size.height * selected.plant.yFraction.toFloat()
                        val edgeX = if (selected.plant.xFraction <= 0.5) 0f else size.width
                        val edgeY = if (selected.plant.yFraction <= 0.5) 0f else size.height
                        drawLine(Color(0xFFFFE45C), Offset(sx, sy), Offset(edgeX, sy), 3f)
                        drawLine(Color(0xFFFFE45C), Offset(sx, sy), Offset(sx, edgeY), 3f)

                        nearestPlant(zone, selected, plants)?.let { nearest ->
                            drawLine(
                                Color.White,
                                Offset(sx, sy),
                                Offset(
                                    size.width * nearest.first.plant.xFraction.toFloat(),
                                    size.height * nearest.first.plant.yFraction.toFloat(),
                                ),
                                3f,
                            )
                        }
                    }
                }

                Text(
                    "alto",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(7.dp)
                        .background(Color(0xAA3D2C20), RoundedCornerShape(7.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                )
                Text(
                    "griglia 10 cm",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(7.dp)
                        .background(Color(0xAA3D2C20), RoundedCornerShape(7.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                )

                plants.forEach { visual ->
                    DraggablePlant(
                        zone = zone,
                        visual = visual,
                        selected = visual.plant.id == selectedPlantId,
                        boardSize = boardSize,
                        onSelect = { onSelectPlant(visual.plant.id) },
                        onPreview = { x, y -> onPreviewPlant(visual.plant.id, x, y) },
                        onCommit = { x, y ->
                            onCommitPlant(visual.crop.id, visual.plant.id, x, y)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun DraggablePlant(
    zone: GardenZone,
    visual: PlantVisual,
    selected: Boolean,
    boardSize: IntSize,
    onSelect: () -> Unit,
    onPreview: (Double, Double) -> Unit,
    onCommit: (Double, Double) -> Unit,
) {
    var x by remember(visual.plant.id) { mutableFloatStateOf(visual.plant.xFraction.toFloat()) }
    var y by remember(visual.plant.id) { mutableFloatStateOf(visual.plant.yFraction.toFloat()) }
    var dragging by remember(visual.plant.id) { mutableStateOf(false) }

    val radiusMeters = visual.crop.iconDiameterCm.coerceIn(5.0, 100.0) / 200.0
    val minX = (radiusMeters / zone.widthMeters).coerceIn(0.0, 0.49).toFloat()
    val minY = (radiusMeters / zone.lengthMeters).coerceIn(0.0, 0.49).toFloat()
    val pixelsPerMeter = if (boardSize.width > 0 && boardSize.height > 0) {
        minOf(boardSize.width / zone.widthMeters, boardSize.height / zone.lengthMeters)
    } else {
        100.0
    }
    val iconDiameterPx = (pixelsPerMeter * visual.crop.iconDiameterCm / 100.0)
        .toFloat()
        .coerceIn(8f, 92f)
    val visualSize = with(LocalDensity.current) { iconDiameterPx.toDp() }
    val hitSize = if (visualSize > 48.dp) visualSize else 48.dp
    val emojiSize = (visualSize.value * 0.55f).coerceIn(8f, 30f).sp

    LaunchedEffect(visual.plant.xFraction, visual.plant.yFraction, dragging, minX, minY) {
        if (!dragging) {
            x = visual.plant.xFraction.toFloat().coerceIn(minX, 1f - minX)
            y = visual.plant.yFraction.toFloat().coerceIn(minY, 1f - minY)
        }
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = (x * boardSize.width - hitSize.roundToPx() / 2f).roundToInt(),
                    y = (y * boardSize.height - hitSize.roundToPx() / 2f).roundToInt(),
                )
            }
            .size(hitSize)
            .clickable(onClick = onSelect)
            .pointerInput(visual.plant.id, boardSize, visual.crop.iconDiameterCm, zone.id) {
                detectDragGestures(
                    onDragStart = {
                        dragging = true
                        onSelect()
                    },
                    onDragCancel = {
                        dragging = false
                        onCommit(x.toDouble(), y.toDouble())
                    },
                    onDragEnd = {
                        dragging = false
                        onCommit(x.toDouble(), y.toDouble())
                    },
                ) { change, dragAmount ->
                    change.consume()
                    if (boardSize.width > 0 && boardSize.height > 0) {
                        x = (x + dragAmount.x / boardSize.width).coerceIn(minX, 1f - minX)
                        y = (y + dragAmount.y / boardSize.height).coerceIn(minY, 1f - minY)
                        onPreview(x.toDouble(), y.toDouble())
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(visualSize)
                .clip(CircleShape)
                .background(parseCropColor(visual.crop.iconColorHex))
                .border(
                    width = if (selected) 3.dp else 2.dp,
                    color = if (selected) Color(0xFFFFE45C) else Color.White,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(cropEmoji(visual.crop.name), fontSize = emojiSize)
            Text(
                visual.number.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color(0xFF234D2B), CircleShape)
                    .padding(horizontal = 5.dp, vertical = 1.dp),
            )
        }
    }
}

@Composable
private fun PlantMeasurements(
    zone: GardenZone,
    selected: PlantVisual,
    allPlants: List<PlantVisual>,
    onEditPosition: () -> Unit,
    onRemove: () -> Unit,
) {
    val info = distanceInfo(zone, selected, allPlants)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(13.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(13.dp))
            .padding(11.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            buildString {
                append(cropEmoji(selected.crop.name))
                append(" ${selected.crop.name}")
                if (selected.crop.variety.isNotBlank()) append(" · ${selected.crop.variety}")
                append(" · pianta ${selected.number}")
            },
            fontWeight = FontWeight.Bold,
        )
        Text(
            "Diametro icona: ${formatInputNumber(selected.crop.iconDiameterCm)} cm · comune a tutte le piante uguali",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            "Distanza consigliata: ${formatInputNumber(selected.crop.plantSpacingCm)} cm tra le piante · " +
                "${formatInputNumber(selected.crop.rowSpacingCm)} cm tra le file",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MeasurementPill("Bordo sinistro", formatCentimeters(info.leftMeters), Modifier.weight(1f))
            MeasurementPill("Bordo destro", formatCentimeters(info.rightMeters), Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MeasurementPill("Bordo superiore", formatCentimeters(info.topMeters), Modifier.weight(1f))
            MeasurementPill("Bordo inferiore", formatCentimeters(info.bottomMeters), Modifier.weight(1f))
        }
        if (info.nearestPlant != null && info.nearestPlantMeters != null) {
            val clearanceCentimeters = info.nearestPlantMeters * 100.0 -
                (selected.crop.iconDiameterCm + info.nearestPlant.crop.iconDiameterCm) / 2.0
            val sameType = selected.crop.name.trim().equals(info.nearestPlant.crop.name.trim(), ignoreCase = true) &&
                selected.crop.variety.trim().equals(info.nearestPlant.crop.variety.trim(), ignoreCase = true)
            val distanceCentimeters = info.nearestPlantMeters * 100.0
            val status = when {
                clearanceCentimeters < 0.0 -> " · SOVRAPPOSTE"
                sameType && distanceCentimeters < selected.crop.plantSpacingCm ->
                    " · sotto i ${formatInputNumber(selected.crop.plantSpacingCm)} cm consigliati"
                else -> " · distanza regolare"
            }
            Text(
                "Pianta più vicina: ${cropEmoji(info.nearestPlant.crop.name)} " +
                    "${info.nearestPlant.crop.name} ${info.nearestPlant.number} a " +
                    "${formatCentimeters(info.nearestPlantMeters)} (centro-centro). " +
                    "Spazio tra le sagome: ${formatInputNumber(clearanceCentimeters)} cm$status.",
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            Text("Non ci sono altre piante nella zona.", style = MaterialTheme.typography.bodySmall)
        }
        Text(
            "Coordinate: ${formatCentimeters(info.leftMeters)} da sinistra · " +
                "${formatCentimeters(info.topMeters)} dall’alto.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TextButton(onClick = onEditPosition, modifier = Modifier.weight(1f)) {
                Text("Posizione precisa")
            }
            TextButton(onClick = onRemove, modifier = Modifier.weight(1f)) {
                Text("Rimuovi pianta")
            }
        }
    }
}

@Composable
private fun PlantPositionDialog(
    zone: GardenZone,
    target: PlantVisual,
    onDismiss: () -> Unit,
    onConfirm: (xFraction: Double, yFraction: Double) -> Unit,
) {
    var leftCentimeters by remember(target.plant.id) {
        mutableStateOf(formatInputCentimeters(target.plant.xFraction * zone.widthMeters))
    }
    var topCentimeters by remember(target.plant.id) {
        mutableStateOf(formatInputCentimeters(target.plant.yFraction * zone.lengthMeters))
    }
    val left = leftCentimeters.replace(',', '.').toDoubleOrNull()
    val top = topCentimeters.replace(',', '.').toDoubleOrNull()
    val radiusCentimeters = target.crop.iconDiameterCm.coerceIn(5.0, 100.0) / 2.0
    val minLeft = radiusCentimeters
    val maxLeft = zone.widthMeters * 100.0 - radiusCentimeters
    val minTop = radiusCentimeters
    val maxTop = zone.lengthMeters * 100.0 - radiusCentimeters
    val valid = left != null && top != null && left in minLeft..maxLeft && top in minTop..maxTop

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Posizione precisa") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("${cropEmoji(target.crop.name)} ${target.crop.name} · pianta ${target.number}")
                OutlinedTextField(
                    value = leftCentimeters,
                    onValueChange = { leftCentimeters = it },
                    label = { Text("Distanza da sinistra (cm)") },
                    supportingText = { Text("Da ${formatInputNumber(minLeft)} a ${formatInputNumber(maxLeft)} cm") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = topCentimeters,
                    onValueChange = { topCentimeters = it },
                    label = { Text("Distanza dall’alto (cm)") },
                    supportingText = { Text("Da ${formatInputNumber(minTop)} a ${formatInputNumber(maxTop)} cm") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = valid,
                onClick = {
                    onConfirm(
                        (left!! / (zone.widthMeters * 100.0)).coerceIn(
                            minLeft / (zone.widthMeters * 100.0),
                            maxLeft / (zone.widthMeters * 100.0),
                        ),
                        (top!! / (zone.lengthMeters * 100.0)).coerceIn(
                            minTop / (zone.lengthMeters * 100.0),
                            maxTop / (zone.lengthMeters * 100.0),
                        ),
                    )
                },
            ) { Text("Salva posizione") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } },
    )
}

@Composable
private fun MeasurementPill(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
            .padding(8.dp),
    ) {
        Text(title, style = MaterialTheme.typography.bodySmall)
        Text(value, fontWeight = FontWeight.Bold)
    }
}

private fun distanceInfo(
    zone: GardenZone,
    selected: PlantVisual,
    allPlants: List<PlantVisual>,
): PlantDistanceInfo {
    val xMeters = selected.plant.xFraction * zone.widthMeters
    val yMeters = selected.plant.yFraction * zone.lengthMeters
    val nearest = nearestPlant(zone, selected, allPlants)

    return PlantDistanceInfo(
        leftMeters = xMeters,
        rightMeters = zone.widthMeters - xMeters,
        topMeters = yMeters,
        bottomMeters = zone.lengthMeters - yMeters,
        nearestPlant = nearest?.first,
        nearestPlantMeters = nearest?.second,
    )
}

private fun nearestPlant(
    zone: GardenZone,
    selected: PlantVisual,
    allPlants: List<PlantVisual>,
): Pair<PlantVisual, Double>? = allPlants
    .asSequence()
    .filter { it.plant.id != selected.plant.id }
    .map { other ->
        val dx = (other.plant.xFraction - selected.plant.xFraction) * zone.widthMeters
        val dy = (other.plant.yFraction - selected.plant.yFraction) * zone.lengthMeters
        other to hypot(dx, dy)
    }
    .minByOrNull { it.second }

private fun formatCentimeters(meters: Double): String =
    String.format(Locale.ITALY, "%.1f cm", meters * 100.0).replace(",0 cm", " cm")

private fun formatInputCentimeters(meters: Double): String =
    formatInputNumber(meters * 100.0)

private fun formatInputNumber(value: Double): String =
    String.format(Locale.ITALY, "%.1f", value).trimEnd('0').trimEnd(',')

private fun cropEmoji(name: String): String = inferCatalog(name)?.emoji ?: "🌱"

private fun parseCropColor(hex: String): Color = runCatching {
    Color(android.graphics.Color.parseColor(hex))
}.getOrDefault(Color(0xFFEDF8E8))
