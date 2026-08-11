package it.luca.ilmiorto.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class GardenStore(private val repository: GardenRepository) {
    var state by mutableStateOf(repository.load())
        private set

    fun update(transform: (GardenState) -> GardenState) {
        state = transform(state)
        repository.save(state)
    }

    fun addCrop(crop: Crop) = update { it.withCrop(crop) }
    fun addTask(task: GardenTask) = update { it.withTask(task) }
    fun addHarvest(harvest: Harvest) = update { it.withHarvest(harvest) }
    fun addHistory(entry: CropHistoryEntry) = update { it.withHistory(entry) }
    fun removeHistory(entryId: String) = update { it.removeHistory(entryId) }
    fun applyGuideDistances(guideId: String) = update { it.applyGuideDistances(guideId) }
    fun toggleTask(taskId: String) = update { it.toggleTask(taskId) }
    fun removeCrop(cropId: String) = update { it.removeCrop(cropId) }
    fun updateCrop(crop: Crop) = update { it.updateCrop(crop) }
    fun autoArrangeCrop(cropId: String): Boolean {
        val planned = state.autoArrangeCrop(cropId)
        val changed = planned != state
        if (changed) {
            state = planned
            repository.save(state)
        }
        return changed
    }
    fun duplicateCrop(cropId: String, targetZoneId: String) = update { it.duplicateCrop(cropId, targetZoneId) }
    fun removePlant(cropId: String, plantId: String) = update { it.removePlant(cropId, plantId) }
    fun removeTask(taskId: String) = update { it.removeTask(taskId) }
    fun removeHarvest(harvestId: String) = update { it.removeHarvest(harvestId) }
    fun movePlant(cropId: String, plantId: String, xFraction: Double, yFraction: Double) =
        update { it.movePlant(cropId, plantId, xFraction, yFraction) }

    fun exportJson(): String = GardenJson.encode(state)

    fun importJson(json: String) {
        val imported = GardenJson.decode(json)
        state = imported
        repository.save(state)
    }

    fun reset() {
        state = repository.reset()
    }
}
