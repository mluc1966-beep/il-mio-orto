package it.luca.ilmiorto.data

import org.json.JSONArray
import org.json.JSONObject

object GardenJson {
    private const val FORMAT_VERSION = 5

    fun encode(state: GardenState): String {
        val root = JSONObject()
            .put("formatVersion", FORMAT_VERSION)
            .put("seasonYear", state.seasonYear)
            .put("crops", JSONArray().apply { state.crops.forEach { put(it.toJson()) } })
            .put("tasks", JSONArray().apply { state.tasks.forEach { put(it.toJson()) } })
            .put("harvests", JSONArray().apply { state.harvests.forEach { put(it.toJson()) } })
            .put("history", JSONArray().apply { state.history.forEach { put(it.toJson()) } })
        return root.toString(2)
    }

    fun decode(text: String): GardenState {
        val root = JSONObject(text)
        val version = root.optInt("formatVersion", 1)
        require(version in 1..FORMAT_VERSION) { "Versione backup non supportata: $version" }

        return GardenState(
            seasonYear = root.optInt("seasonYear", java.time.LocalDate.now().year),
            crops = root.optJSONArray("crops").toObjectList(::cropFromJson),
            tasks = root.optJSONArray("tasks").toObjectList(::taskFromJson),
            harvests = root.optJSONArray("harvests").toObjectList(::harvestFromJson),
            history = root.optJSONArray("history").toObjectList(::historyFromJson),
        ).also(::validate)
    }

    private fun validate(state: GardenState) {
        require(state.seasonYear in 2000..2200) { "Anno stagione non valido" }
        require(state.crops.size <= 5_000) { "Numero di colture non valido" }
        require(state.tasks.size <= 20_000) { "Numero di attività non valido" }
        require(state.harvests.size <= 20_000) { "Numero di raccolti non valido" }
        require(state.history.size <= 5_000) { "Numero di rotazioni non valido" }
        require(state.crops.all { it.name.isNotBlank() && it.zoneId.isNotBlank() }) {
            "Il backup contiene una coltura incompleta"
        }
        require(state.crops.sumOf { it.plants.size } <= 50_000) {
            "Il backup contiene troppe piante"
        }
        require(state.crops.all { it.iconDiameterCm in 5.0..100.0 }) {
            "Il backup contiene una dimensione icona non valida"
        }
        require(state.crops.all { it.plantSpacingCm in 5.0..300.0 && it.rowSpacingCm in 5.0..300.0 }) {
            "Il backup contiene distanze di impianto non valide"
        }
        require(state.crops.all { crop ->
            crop.plants.all { it.xFraction in 0.0..1.0 && it.yFraction in 0.0..1.0 }
        }) {
            "Il backup contiene coordinate delle piante non valide"
        }
        require(state.tasks.all { it.title.isNotBlank() && isIsoDate(it.date) }) {
            "Il backup contiene un'attività non valida"
        }
        require(state.harvests.all { it.cropName.isNotBlank() && it.weightGrams >= 0 && isIsoDate(it.date) }) {
            "Il backup contiene un raccolto non valido"
        }
        require(state.history.all { it.year in 2000..2200 && it.zoneId.isNotBlank() && it.speciesName.isNotBlank() }) {
            "Il backup contiene una rotazione non valida"
        }
    }

    private fun Crop.toJson() = JSONObject()
        .put("id", id)
        .put("zoneId", zoneId)
        .put("name", name)
        .put("variety", variety)
        .put("plantCount", plantCount)
        .put("sowingDate", sowingDate)
        .put("transplantDate", transplantDate)
        .put("expectedHarvestDate", expectedHarvestDate)
        .put("notes", notes)
        .put("catalogId", catalogId)
        .put("iconColorHex", iconColorHex)
        .put("iconDiameterCm", iconDiameterCm)
        .put("plantSpacingCm", plantSpacingCm)
        .put("rowSpacingCm", rowSpacingCm)
        .put("plants", JSONArray().apply { plants.forEach { put(it.toJson()) } })

    private fun PlantPosition.toJson() = JSONObject()
        .put("id", id)
        .put("xFraction", xFraction)
        .put("yFraction", yFraction)

    private fun GardenTask.toJson() = JSONObject()
        .put("id", id)
        .put("date", date)
        .put("title", title)
        .put("category", category)
        .put("zoneId", zoneId)
        .put("cropId", cropId)
        .put("notes", notes)
        .put("completed", completed)

    private fun Harvest.toJson() = JSONObject()
        .put("id", id)
        .put("date", date)
        .put("cropId", cropId)
        .put("cropName", cropName)
        .put("zoneId", zoneId)
        .put("weightGrams", weightGrams)
        .put("quantity", quantity)
        .put("quality", quality)
        .put("notes", notes)


    private fun CropHistoryEntry.toJson() = JSONObject()
        .put("id", id)
        .put("year", year)
        .put("zoneId", zoneId)
        .put("speciesId", speciesId)
        .put("speciesName", speciesName)

    private fun cropFromJson(json: JSONObject) = Crop(
        id = json.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
        zoneId = json.optString("zoneId"),
        name = json.optString("name"),
        variety = json.optString("variety"),
        plantCount = json.optInt("plantCount", 0).coerceAtLeast(0),
        sowingDate = json.optString("sowingDate"),
        transplantDate = json.optString("transplantDate"),
        expectedHarvestDate = json.optString("expectedHarvestDate"),
        notes = json.optString("notes"),
        catalogId = json.optString("catalogId").ifBlank { inferCatalog(json.optString("name"))?.id ?: "custom" },
        iconColorHex = json.optString("iconColorHex").ifBlank { inferCatalog(json.optString("name"))?.colorHex ?: "#C9E2BD" },
        iconDiameterCm = json.optDouble("iconDiameterCm", inferCatalog(json.optString("name"))?.iconDiameterCm ?: 20.0).coerceIn(5.0, 100.0),
        plantSpacingCm = json.optDouble("plantSpacingCm", inferCatalog(json.optString("name"))?.plantSpacingCm ?: 30.0).coerceIn(5.0, 300.0),
        rowSpacingCm = json.optDouble("rowSpacingCm", inferCatalog(json.optString("name"))?.rowSpacingCm ?: 40.0).coerceIn(5.0, 300.0),
        plants = json.optJSONArray("plants")
            .toObjectList(::plantFromJson)
            .let { positions ->
                if (positions.isNotEmpty() || json.optInt("plantCount", 0) == 0) positions
                else initialPlantPositions(json.optInt("plantCount", 0))
            },
    )

    private fun plantFromJson(json: JSONObject) = PlantPosition(
        id = json.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
        xFraction = json.optDouble("xFraction", 0.5).coerceIn(0.04, 0.96),
        yFraction = json.optDouble("yFraction", 0.5).coerceIn(0.04, 0.96),
    )

    private fun taskFromJson(json: JSONObject) = GardenTask(
        id = json.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
        date = json.optString("date"),
        title = json.optString("title"),
        category = json.optString("category", "Altro"),
        zoneId = json.optString("zoneId"),
        cropId = json.optString("cropId"),
        notes = json.optString("notes"),
        completed = json.optBoolean("completed", false),
    )

    private fun harvestFromJson(json: JSONObject) = Harvest(
        id = json.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
        date = json.optString("date"),
        cropId = json.optString("cropId"),
        cropName = json.optString("cropName"),
        zoneId = json.optString("zoneId"),
        weightGrams = json.optInt("weightGrams", 0).coerceAtLeast(0),
        quantity = json.optInt("quantity", 0).coerceAtLeast(0),
        quality = json.optString("quality", "Buona"),
        notes = json.optString("notes"),
    )


    private fun historyFromJson(json: JSONObject) = CropHistoryEntry(
        id = json.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
        year = json.optInt("year", java.time.LocalDate.now().year - 1),
        zoneId = json.optString("zoneId"),
        speciesId = json.optString("speciesId"),
        speciesName = json.optString("speciesName"),
    )

    private fun <T> JSONArray?.toObjectList(transform: (JSONObject) -> T): List<T> {
        if (this == null) return emptyList()
        return buildList {
            for (index in 0 until length()) {
                optJSONObject(index)?.let { add(transform(it)) }
            }
        }
    }
}
