package it.luca.ilmiorto.data

import java.time.LocalDate
import java.util.UUID
import kotlin.math.ceil
import kotlin.math.hypot
import kotlin.math.sqrt

data class GardenZone(
    val id: String,
    val name: String,
    val widthMeters: Double,
    val lengthMeters: Double,
    val group: String = "",
) {
    val areaSquareMeters: Double get() = widthMeters * lengthMeters
    val dimensionsLabel: String
        get() = "${formatMeasure(widthMeters)} × ${formatMeasure(lengthMeters)} m"
}

data class PlantPosition(
    val id: String = UUID.randomUUID().toString(),
    val xFraction: Double,
    val yFraction: Double,
)

data class CropCatalogItem(
    val id: String,
    val name: String,
    val emoji: String,
    val colorHex: String,
    val iconDiameterCm: Double,
    val plantSpacingCm: Double,
    val rowSpacingCm: Double,
    val varieties: List<String> = emptyList(),
    val aliases: List<String> = emptyList(),
)

data class Crop(
    val id: String = UUID.randomUUID().toString(),
    val zoneId: String,
    val name: String,
    val variety: String = "",
    val plantCount: Int = 0,
    val sowingDate: String = "",
    val transplantDate: String = "",
    val expectedHarvestDate: String = "",
    val notes: String = "",
    val catalogId: String = "custom",
    val iconColorHex: String = "#C9E2BD",
    val iconDiameterCm: Double = 20.0,
    val plantSpacingCm: Double = 30.0,
    val rowSpacingCm: Double = 40.0,
    val plants: List<PlantPosition> = emptyList(),
)

data class GardenTask(
    val id: String = UUID.randomUUID().toString(),
    val date: String = LocalDate.now().toString(),
    val title: String,
    val category: String = "Altro",
    val zoneId: String = "",
    val cropId: String = "",
    val notes: String = "",
    val completed: Boolean = false,
)

data class Harvest(
    val id: String = UUID.randomUUID().toString(),
    val date: String = LocalDate.now().toString(),
    val cropId: String = "",
    val cropName: String,
    val zoneId: String = "",
    val weightGrams: Int = 0,
    val quantity: Int = 0,
    val quality: String = "Buona",
    val notes: String = "",
)

data class GardenState(
    val seasonYear: Int = LocalDate.now().year,
    val crops: List<Crop> = emptyList(),
    val tasks: List<GardenTask> = emptyList(),
    val harvests: List<Harvest> = emptyList(),
    val history: List<CropHistoryEntry> = emptyList(),
)

data class PlantSpacingWarning(
    val firstCropId: String,
    val firstPlantId: String,
    val secondCropId: String,
    val secondPlantId: String,
    val distanceCm: Double,
    val requiredCm: Double,
    val overlap: Boolean,
)

val DEFAULT_ZONES = listOf(
    GardenZone("proda_1", "Proda 1", 1.20, 3.00, "Prode rialzate"),
    GardenZone("proda_2", "Proda 2", 1.20, 3.00, "Prode rialzate"),
    GardenZone("proda_3", "Proda 3", 1.20, 3.00, "Prode rialzate"),
    GardenZone("proda_4", "Proda 4", 1.20, 3.00, "Prode rialzate"),
    GardenZone("striscia", "Striscia laterale", 0.80, 5.70, "Zona laterale"),
    GardenZone("area_l_lungo", "Area a L · braccio verticale", 1.20, 2.80, "Area a L"),
    GardenZone("area_l_corto", "Area a L · braccio corto in alto", 0.80, 2.60, "Area a L"),
)

private val BASE_CROP_CATALOG = listOf(
    CropCatalogItem("pomodoro", "Pomodoro", "🍅", "#F6B1AA", 20.0, 50.0, 80.0, listOf("Mondesse", "Cuore di bue", "Ciliegino", "Datterino", "Tondo da ripieno"), listOf("pomodor")),
    CropCatalogItem("peperone", "Peperone", "🫑", "#CBE8A9", 18.0, 40.0, 60.0, aliases = listOf("peperon")),
    CropCatalogItem("melanzana", "Melanzana", "🍆", "#D6B6E8", 20.0, 50.0, 70.0, aliases = listOf("melanz")),
    CropCatalogItem("zucchino", "Zucchino", "🥒", "#B9DFAA", 28.0, 80.0, 100.0, aliases = listOf("zucchin")),
    CropCatalogItem("trombetta", "Trombetta", "🥒", "#C5E8AE", 25.0, 80.0, 120.0, listOf("Trombetta di Albenga"), listOf("trombett")),
    CropCatalogItem("fagiolino", "Fagiolino nano", "🫘", "#D8E8A7", 10.0, 15.0, 40.0, aliases = listOf("fagiolin")),
    CropCatalogItem("cipolla", "Cipolla", "🧅", "#F0DFB3", 8.0, 10.0, 25.0, aliases = listOf("cipoll")),
    CropCatalogItem("porro", "Porro", "🧅", "#D9E9C5", 10.0, 15.0, 30.0, aliases = listOf("porro")),
    CropCatalogItem("lattuga", "Lattuga", "🥬", "#BDE2A6", 18.0, 30.0, 30.0, aliases = listOf("lattug", "insalat")),
    CropCatalogItem("basilico", "Basilico", "🌿", "#A9D89E", 12.0, 25.0, 30.0, aliases = listOf("basil")),
    CropCatalogItem("costa", "Costa", "🥬", "#C9E8B6", 16.0, 30.0, 40.0, aliases = listOf("costa", "coste")),
    CropCatalogItem("barbabietola", "Barbabietola", "🟣", "#DFB3C9", 10.0, 15.0, 30.0, aliases = listOf("barbabiet")),
    CropCatalogItem("finocchio", "Finocchio", "🌿", "#DCEBD1", 14.0, 25.0, 40.0, aliases = listOf("finocch")),
    CropCatalogItem("cavolfiore", "Cavolfiore", "🥦", "#E6E2CF", 22.0, 50.0, 60.0, aliases = listOf("cavolfior")),
    CropCatalogItem("broccolo", "Broccolo", "🥦", "#AED2A6", 22.0, 50.0, 60.0, aliases = listOf("broccol")),
    CropCatalogItem("verza", "Verza", "🥬", "#BFD9AD", 22.0, 50.0, 60.0, aliases = listOf("verza")),
    CropCatalogItem("cappuccio", "Cavolo cappuccio", "🥬", "#CADFB7", 22.0, 45.0, 60.0, aliases = listOf("cappuccio")),
    CropCatalogItem("hokkaido", "Zucca Hokkaido", "🎃", "#F2C08F", 32.0, 100.0, 150.0, aliases = listOf("hokkaido")),
    CropCatalogItem("patata", "Patata", "🥔", "#DDC4A1", 12.0, 35.0, 70.0, aliases = listOf("patat")),
    CropCatalogItem("cetriolo", "Cetriolo", "🥒", "#B7DFA8", 18.0, 45.0, 90.0, aliases = listOf("cetriol")),
    CropCatalogItem("rucola", "Rucola", "🌿", "#B5DCA7", 10.0, 10.0, 20.0, aliases = listOf("rucol")),
    CropCatalogItem("tagete", "Tagete", "🌼", "#F3D68C", 14.0, 25.0, 30.0, aliases = listOf("tagete", "calendula")),
)

private val GUIDE_ID_BY_BASE_ID = mapOf(
    "costa" to "bietola_da_coste",
    "barbabietola" to "bietola_da_radice",
    "fagiolino" to "fagiolo_e_fagiolino",
    "cavolfiore" to "cavoli",
    "broccolo" to "cavoli",
    "verza" to "cavoli",
    "cappuccio" to "cavoli",
    "hokkaido" to "zucca",
    "trombetta" to "zucchino",
)

val CROP_CATALOG: List<CropCatalogItem> = buildList {
    BASE_CROP_CATALOG.forEach { base ->
        val guide = cropGuideItem(GUIDE_ID_BY_BASE_ID[base.id] ?: base.id)
        add(
            if (guide == null) base else base.copy(
                plantSpacingCm = guide.plantSpacingCm,
                rowSpacingCm = guide.rowSpacingCm,
            )
        )
    }
    CROP_GUIDE.filter { guide -> none { it.id == guide.id } }.forEach { guide ->
        add(
            CropCatalogItem(
                id = guide.id,
                name = guide.name,
                emoji = guide.emoji,
                colorHex = guide.colorHex,
                iconDiameterCm = (guide.plantSpacingCm * 0.8).coerceIn(8.0, 32.0),
                plantSpacingCm = guide.plantSpacingCm,
                rowSpacingCm = guide.rowSpacingCm,
                aliases = guide.aliases,
            )
        )
    }
}

val CROP_SUGGESTIONS = CROP_CATALOG.map { it.name }

val TASK_CATEGORIES = listOf(
    "Semina", "Trapianto", "Irrigazione", "Concimazione", "Cura", "Protezione", "Raccolta", "Altro",
)

fun catalogItem(id: String): CropCatalogItem? = CROP_CATALOG.firstOrNull { it.id == id }

fun inferCatalog(name: String): CropCatalogItem? {
    val normalized = name.trim().lowercase()
    return CROP_CATALOG.firstOrNull { item ->
        normalized == item.name.lowercase() || item.aliases.any(normalized::contains)
    }
}

fun initialPlantPositions(count: Int): List<PlantPosition> {
    val safeCount = count.coerceIn(0, 100)
    if (safeCount == 0) return emptyList()
    val columns = ceil(sqrt(safeCount.toDouble())).toInt()
    val rows = ceil(safeCount.toDouble() / columns).toInt()
    return List(safeCount) { index ->
        val column = index % columns
        val row = index / columns
        PlantPosition(
            xFraction = (column + 1).toDouble() / (columns + 1),
            yFraction = (row + 1).toDouble() / (rows + 1),
        )
    }
}

fun automaticPlantPositions(
    zoneId: String,
    count: Int,
    iconDiameterCm: Double,
    plantSpacingCm: Double,
    rowSpacingCm: Double,
): List<PlantPosition> {
    val zone = DEFAULT_ZONES.firstOrNull { it.id == zoneId } ?: return initialPlantPositions(count)
    val safeCount = count.coerceIn(0, 100)
    if (safeCount == 0) return emptyList()

    val widthCm = zone.widthMeters * 100.0
    val lengthCm = zone.lengthMeters * 100.0
    val diameter = iconDiameterCm.coerceIn(5.0, 100.0)
    val radius = diameter / 2.0
    val usableWidth = (widthCm - diameter).coerceAtLeast(1.0)
    val usableLength = (lengthCm - diameter).coerceAtLeast(1.0)
    var horizontalStep = plantSpacingCm.coerceIn(diameter, 300.0)
    var verticalStep = rowSpacingCm.coerceIn(diameter, 300.0)
    var columns = (kotlin.math.floor(usableWidth / horizontalStep).toInt() + 1).coerceIn(1, safeCount)
    var rows = ceil(safeCount.toDouble() / columns).toInt()

    if (rows > 1 && (rows - 1) * verticalStep > usableLength) {
        columns = ceil(sqrt(safeCount * widthCm / lengthCm)).toInt().coerceIn(1, safeCount)
        rows = ceil(safeCount.toDouble() / columns).toInt()
        horizontalStep = if (columns <= 1) 0.0 else usableWidth / (columns - 1)
        verticalStep = if (rows <= 1) 0.0 else usableLength / (rows - 1)
    }

    val totalHeight = (rows - 1) * verticalStep
    val startY = (lengthCm - totalHeight) / 2.0
    return List(safeCount) { index ->
        val row = index / columns
        val itemsInRow = minOf(columns, safeCount - row * columns)
        val column = index % columns
        val rowWidth = (itemsInRow - 1) * horizontalStep
        val startX = (widthCm - rowWidth) / 2.0
        val xCm = (startX + column * horizontalStep).coerceIn(radius, widthCm - radius)
        val yCm = (startY + row * verticalStep).coerceIn(radius, lengthCm - radius)
        PlantPosition(xFraction = xCm / widthCm, yFraction = yCm / lengthCm)
    }
}

fun GardenState.movePlant(
    cropId: String,
    plantId: String,
    xFraction: Double,
    yFraction: Double,
): GardenState = copy(
    crops = crops.map { crop ->
        if (crop.id != cropId) crop else {
            val zone = DEFAULT_ZONES.firstOrNull { it.id == crop.zoneId }
            val radiusMeters = crop.iconDiameterCm.coerceIn(5.0, 100.0) / 200.0
            val minX = if (zone == null) 0.02 else (radiusMeters / zone.widthMeters).coerceIn(0.0, 0.49)
            val minY = if (zone == null) 0.02 else (radiusMeters / zone.lengthMeters).coerceIn(0.0, 0.49)
            crop.copy(
                plants = crop.plants.map { plant ->
                    if (plant.id != plantId) plant else plant.copy(
                        xFraction = xFraction.coerceIn(minX, 1.0 - minX),
                        yFraction = yFraction.coerceIn(minY, 1.0 - minY),
                    )
                }
            )
        }
    }
)

fun GardenState.removePlant(cropId: String, plantId: String): GardenState = copy(
    crops = crops.map { crop ->
        if (crop.id != cropId) crop else {
            val remaining = crop.plants.filterNot { it.id == plantId }
            crop.copy(plantCount = remaining.size, plants = remaining)
        }
    }
)

private fun sameCropType(first: Crop, second: Crop): Boolean =
    first.name.trim().equals(second.name.trim(), ignoreCase = true) &&
        first.variety.trim().equals(second.variety.trim(), ignoreCase = true)

private fun Crop.normalized(): Crop {
    val inferred = catalogItem(catalogId) ?: inferCatalog(name)
    return copy(
        catalogId = inferred?.id ?: catalogId.ifBlank { "custom" },
        iconColorHex = iconColorHex.ifBlank { inferred?.colorHex ?: "#C9E2BD" },
        iconDiameterCm = iconDiameterCm.coerceIn(5.0, 100.0),
        plantSpacingCm = plantSpacingCm.coerceIn(5.0, 300.0),
        rowSpacingCm = rowSpacingCm.coerceIn(5.0, 300.0),
    )
}

fun GardenState.updateCrop(updated: Crop): GardenState {
    val normalized = updated.normalized()
    return copy(
        crops = crops.map { existing ->
            when {
                existing.id == normalized.id -> normalized
                sameCropType(existing, normalized) -> existing.copy(
                    iconDiameterCm = normalized.iconDiameterCm,
                    plantSpacingCm = normalized.plantSpacingCm,
                    rowSpacingCm = normalized.rowSpacingCm,
                )
                else -> existing
            }
        }
    )
}

fun GardenState.withCrop(crop: Crop): GardenState {
    val normalized = crop.normalized()
    return copy(
        crops = crops.map { existing ->
            if (sameCropType(existing, normalized)) existing.copy(
                iconDiameterCm = normalized.iconDiameterCm,
                plantSpacingCm = normalized.plantSpacingCm,
                rowSpacingCm = normalized.rowSpacingCm,
            ) else existing
        } + normalized
    )
}

private const val AUTO_CLEARANCE_CM = 3.0

private data class ZonePlacementItem(
    val cropId: String,
    val plantId: String,
    val cropName: String,
    val variety: String,
    val catalogId: String,
    val radiusCm: Double,
    val spacingCm: Double,
    val rowSpacingCm: Double,
    val role: PlacementRole,
    val order: Int,
)

private data class PlacedZoneItem(
    val item: ZonePlacementItem,
    val xCm: Double,
    val yCm: Double,
)

private fun samePlacementType(first: ZonePlacementItem, second: ZonePlacementItem): Boolean =
    first.cropName.trim().equals(second.cropName.trim(), ignoreCase = true) &&
        first.variety.trim().equals(second.variety.trim(), ignoreCase = true)

private fun axisCandidates(minimum: Double, maximum: Double, step: Double): List<Double> {
    if (maximum < minimum) return listOf((minimum + maximum) / 2.0)
    val values = mutableListOf<Double>()
    var value = minimum
    while (value <= maximum + 0.001) {
        values += minOf(maximum, value)
        value += step
    }
    if (values.isEmpty() || kotlin.math.abs(values.last() - maximum) > 0.5) values += maximum
    return values
}

private fun preferredColumns(item: ZonePlacementItem, widthCm: Double): List<Double> {
    val columns = (kotlin.math.floor((widthCm - 2.0 * item.radiusCm) / item.rowSpacingCm).toInt() + 1)
        .coerceIn(1, 3)
    if (columns == 1) return listOf(widthCm / 2.0)
    return List(columns) { index ->
        item.radiusCm + (widthCm - 2.0 * item.radiusCm) * index / (columns - 1)
    }
}

private fun strictRequiredDistance(first: ZonePlacementItem, second: ZonePlacementItem): Double {
    val visualMinimum = first.radiusCm + second.radiusCm + AUTO_CLEARANCE_CM
    if (samePlacementType(first, second)) {
        return maxOf(visualMinimum, first.spacingCm, second.spacingCm)
    }
    val companionMinimum = companionRule(first.catalogId, second.catalogId)?.minimumDistanceCm ?: 0.0
    return maxOf(visualMinimum, companionMinimum)
}

private fun roleScore(
    item: ZonePlacementItem,
    xCm: Double,
    yCm: Double,
    placed: List<PlacedZoneItem>,
    widthCm: Double,
    lengthCm: Double,
): Double {
    val same = placed.filter { samePlacementType(item, it.item) }
    val nearestSame = same.minOfOrNull { hypot(xCm - it.xCm, yCm - it.yCm) } ?: 0.0
    return when (item.role) {
        PlacementRole.EDGE -> {
            val usefulEdge = minOf(
                xCm - item.radiusCm,
                widthCm - xCm - item.radiusCm,
                lengthCm - yCm - item.radiusCm,
            )
            val upperPenalty = if (yCm < lengthCm * 0.52) (lengthCm * 0.52 - yCm) * 12.0 else 0.0
            usefulEdge * 35.0 + upperPenalty + nearestSame * 0.08
        }
        PlacementRole.STAKE -> {
            val columnPenalty = preferredColumns(item, widthCm).minOf { kotlin.math.abs(xCm - it) }
            columnPenalty * 55.0 + yCm * 0.08 + nearestSame * 0.04
        }
        PlacementRole.COMPANION -> {
            val targets = placed.filter {
                companionRule(item.catalogId, it.item.catalogId)?.evidence != "Evidenza limitata"
            }
            if (targets.isEmpty()) 8_000.0 + yCm * 0.05
            else {
                val targetPenalty = targets.minOf { target ->
                    val rule = companionRule(item.catalogId, target.item.catalogId)
                    val ideal = ((rule?.minimumDistanceCm ?: 20.0) + (rule?.maximumDistanceCm ?: 50.0)) / 2.0
                    kotlin.math.abs(hypot(xCm - target.xCm, yCm - target.yCm) - ideal)
                }
                targetPenalty * 55.0 + yCm * 0.03 + nearestSame * 0.08
            }
        }
        else -> yCm * 0.08 + xCm * 0.01 + nearestSame * 0.06
    }
}

private fun tryPlaceZonePlants(
    zone: GardenZone,
    crops: List<Crop>,
    stepCm: Double,
): Map<String, Pair<Double, Double>>? {
    val widthCm = zone.widthMeters * 100.0
    val lengthCm = zone.lengthMeters * 100.0
    val items = crops.flatMap { crop ->
        crop.plants.mapIndexed { index, plant ->
            ZonePlacementItem(
                cropId = crop.id,
                plantId = plant.id,
                cropName = crop.name,
                variety = crop.variety,
                catalogId = crop.catalogKey(),
                radiusCm = crop.iconDiameterCm.coerceIn(5.0, 100.0) / 2.0,
                spacingCm = crop.plantSpacingCm.coerceIn(5.0, 300.0),
                rowSpacingCm = crop.rowSpacingCm.coerceIn(5.0, 300.0),
                role = cropPlacementRole(crop),
                order = index,
            )
        }
    }.sortedWith(
        compareBy<ZonePlacementItem> { it.role.priority }
            .thenBy { it.cropName.lowercase() }
            .thenBy { it.variety.lowercase() }
            .thenBy { it.order }
    )

    val placed = mutableListOf<PlacedZoneItem>()
    for (item in items) {
        val xCandidates = if (item.role == PlacementRole.STAKE) {
            preferredColumns(item, widthCm)
        } else {
            axisCandidates(item.radiusCm, widthCm - item.radiusCm, stepCm)
        }
        val yCandidates = if (item.role == PlacementRole.STAKE) {
            axisCandidates(item.radiusCm, lengthCm - item.radiusCm, item.spacingCm)
        } else {
            axisCandidates(item.radiusCm, lengthCm - item.radiusCm, stepCm)
        }
        var best: PlacedZoneItem? = null
        var bestScore = Double.POSITIVE_INFINITY

        for (yCm in yCandidates) {
            for (xCm in xCandidates) {
                val blocked = placed.any { other ->
                    hypot(xCm - other.xCm, yCm - other.yCm) + 0.1 < strictRequiredDistance(item, other.item)
                }
                if (blocked) continue
                val score = roleScore(item, xCm, yCm, placed, widthCm, lengthCm)
                if (score < bestScore) {
                    bestScore = score
                    best = PlacedZoneItem(item, xCm, yCm)
                }
            }
        }
        placed += best ?: return null
    }

    return placed.associate { placedItem ->
        placedItem.item.plantId to (
            placedItem.xCm / widthCm to placedItem.yCm / lengthCm
        )
    }
}

fun GardenState.autoArrangeZone(zoneId: String): GardenState {
    val zone = DEFAULT_ZONES.firstOrNull { it.id == zoneId } ?: return this
    val zoneCrops = crops.filter { it.zoneId == zoneId }
    if (zoneCrops.none { it.plants.isNotEmpty() }) return this

    val positions = tryPlaceZonePlants(zone, zoneCrops, 5.0) ?: return this

    return copy(
        crops = crops.map { crop ->
            if (crop.zoneId != zoneId) crop else crop.copy(
                plants = crop.plants.map { plant ->
                    positions[plant.id]?.let { (xFraction, yFraction) ->
                        plant.copy(xFraction = xFraction, yFraction = yFraction)
                    } ?: plant
                }
            )
        }
    )
}

fun GardenState.autoArrangeCrop(cropId: String): GardenState {
    val crop = crops.firstOrNull { it.id == cropId } ?: return this
    return autoArrangeZone(crop.zoneId)
}

fun GardenState.duplicateCrop(cropId: String, targetZoneId: String): GardenState {
    val source = crops.firstOrNull { it.id == cropId } ?: return this
    val duplicate = source.copy(
        id = UUID.randomUUID().toString(),
        zoneId = targetZoneId,
        plants = source.plants.map { it.copy(id = UUID.randomUUID().toString()) },
    )
    return withCrop(duplicate)
}

fun spacingWarnings(zone: GardenZone, crops: List<Crop>): List<PlantSpacingWarning> {
    data class Item(val crop: Crop, val plant: PlantPosition)
    val plants = crops.flatMap { crop -> crop.plants.map { Item(crop, it) } }
    val warnings = mutableListOf<PlantSpacingWarning>()
    for (firstIndex in plants.indices) {
        for (secondIndex in firstIndex + 1 until plants.size) {
            val first = plants[firstIndex]
            val second = plants[secondIndex]
            val dxCm = (first.plant.xFraction - second.plant.xFraction) * zone.widthMeters * 100.0
            val dyCm = (first.plant.yFraction - second.plant.yFraction) * zone.lengthMeters * 100.0
            val distanceCm = kotlin.math.hypot(dxCm, dyCm)
            val radiusSum = (first.crop.iconDiameterCm + second.crop.iconDiameterCm) / 2.0
            val clearance = distanceCm - radiusSum
            val sameType = sameCropType(first.crop, second.crop)
            val recommended = if (sameType) maxOf(first.crop.plantSpacingCm, second.crop.plantSpacingCm) else 0.0
            val visualOverlap = clearance < AUTO_CLEARANCE_CM - 0.05
            if (visualOverlap || (sameType && distanceCm + 0.1 < recommended)) {
                warnings += PlantSpacingWarning(
                    firstCropId = first.crop.id,
                    firstPlantId = first.plant.id,
                    secondCropId = second.crop.id,
                    secondPlantId = second.plant.id,
                    distanceCm = distanceCm,
                    requiredCm = if (visualOverlap) radiusSum + AUTO_CLEARANCE_CM else recommended,
                    overlap = visualOverlap,
                )
            }
        }
    }
    return warnings
}

fun GardenState.withTask(task: GardenTask): GardenState = copy(tasks = tasks + task)
fun GardenState.withHarvest(harvest: Harvest): GardenState = copy(harvests = harvests + harvest)
fun GardenState.toggleTask(taskId: String): GardenState = copy(tasks = tasks.map { if (it.id == taskId) it.copy(completed = !it.completed) else it })
fun GardenState.removeCrop(cropId: String): GardenState = copy(
    crops = crops.filterNot { it.id == cropId },
    tasks = tasks.map { if (it.cropId == cropId) it.copy(cropId = "") else it },
    harvests = harvests.map { if (it.cropId == cropId) it.copy(cropId = "") else it },
)
fun GardenState.removeTask(taskId: String): GardenState = copy(tasks = tasks.filterNot { it.id == taskId })
fun GardenState.removeHarvest(harvestId: String): GardenState = copy(harvests = harvests.filterNot { it.id == harvestId })
fun GardenState.cropName(cropId: String): String = crops.firstOrNull { it.id == cropId }?.name.orEmpty()
fun GardenState.zoneName(zoneId: String): String = DEFAULT_ZONES.firstOrNull { it.id == zoneId }?.name.orEmpty()
fun isIsoDate(value: String): Boolean = value.isBlank() || runCatching { LocalDate.parse(value) }.isSuccess

private fun formatMeasure(value: Double): String = if (value % 1.0 == 0.0) {
    value.toInt().toString()
} else {
    String.format(java.util.Locale.ITALY, "%.2f", value).trimEnd('0').trimEnd(',')
}
