package pl.szymonchaber.donttouchit.screenblocking.settings

import android.content.Context

class SettingsManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
            KEY_SETTINGS, Context.MODE_PRIVATE)

    fun setMode(blockingMode: BlockingMode) {
        when (blockingMode) {
            BlockingMode.TOGGLE -> MODE_TOGGLE
            else -> MODE_SMART
        }.let {
            sharedPreferences.edit()
                    .putInt(KEY_MODE, it)
                    .apply()
        }
    }

    fun getMode(): BlockingMode {
        return when (sharedPreferences.getInt(
                KEY_MODE,
                MODE_TOGGLE)) {
            MODE_TOGGLE -> BlockingMode.TOGGLE
            else -> BlockingMode.SMART
        }
    }

    companion object {

        const val KEY_SETTINGS = "SettingsPrefs"
        const val KEY_MODE = "KEY_MODE"
        const val MODE_TOGGLE = 0
        const val MODE_SMART = 1
    }
}