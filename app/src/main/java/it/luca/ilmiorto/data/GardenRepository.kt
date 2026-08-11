package it.luca.ilmiorto.data

import android.content.Context

class GardenRepository(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun load(): GardenState {
        val json = preferences.getString(KEY_STATE, null) ?: return GardenState()
        return runCatching { GardenJson.decode(json) }.getOrElse { GardenState() }
    }

    fun save(state: GardenState) {
        preferences.edit().putString(KEY_STATE, GardenJson.encode(state)).apply()
    }

    fun reset(): GardenState = GardenState().also(::save)

    companion object {
        private const val PREFERENCES_NAME = "il_mio_orto"
        private const val KEY_STATE = "garden_state_v1"
    }
}
