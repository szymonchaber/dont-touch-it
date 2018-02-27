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
        return when (sharedPreferences.getInt(KEY_MODE, MODE_TOGGLE)) {
            MODE_TOGGLE -> BlockingMode.TOGGLE
            else -> BlockingMode.SMART
        }
    }

    fun setSignalType(signalType: SignalType) {
        when (signalType) {
            SignalType.PROXIMITY -> SIGNAL_PROXIMITY
            else -> SIGNAL_ROTATION
        }.let {
            sharedPreferences.edit()
                    .putInt(KEY_SIGNAL, it)
                    .apply()
        }
    }

    fun getSignalType(): SignalType {
        return when (sharedPreferences.getInt(KEY_SIGNAL, SIGNAL_PROXIMITY)) {
            SIGNAL_PROXIMITY -> SignalType.PROXIMITY
            else -> SignalType.ROTATION
        }
    }

    companion object {

        const val KEY_SETTINGS = "SettingsPrefs"
        const val KEY_MODE = "KEY_MODE"
        const val KEY_SIGNAL = "KEY_SIGNAL"
        const val MODE_TOGGLE = 0
        const val MODE_SMART = 1
        const val SIGNAL_PROXIMITY = 10
        const val SIGNAL_ROTATION = 11
    }
}