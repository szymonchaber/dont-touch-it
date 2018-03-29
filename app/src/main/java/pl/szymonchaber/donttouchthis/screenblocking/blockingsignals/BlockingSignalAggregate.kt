package pl.szymonchaber.donttouchthis.screenblocking.blockingsignals

import android.content.Context
import pl.szymonchaber.donttouchthis.screenblocking.settings.BlockingMode
import pl.szymonchaber.donttouchthis.screenblocking.settings.SettingsManager
import pl.szymonchaber.donttouchthis.screenblocking.settings.SignalType

class BlockingSignalAggregate(settingsManager: SettingsManager, context: Context,
        shouldBlockListener: OnShouldBlockListener) : BlockingSignal(shouldBlockListener) {

    private val blockingSignals: MutableList<BlockingSignal> = ArrayList()

    init {
        when (settingsManager.getMode()) {
            BlockingMode.TOGGLE -> blockingSignals.add(ToggleBlockingSignal(shouldBlockListener))
            BlockingMode.SMART ->
                when (settingsManager.getSignalType()) {
                    SignalType.PROXIMITY -> blockingSignals.add(ProximityBlockingSignal(context, shouldBlockListener))
                    SignalType.ROTATION -> blockingSignals.add(RotationBlockingSignal(context, shouldBlockListener))
                }
        }
    }

    override fun startBlocking() {
        blockingSignals.forEach {
            it.startBlocking()
        }
    }

    override fun stopBlocking() {
        blockingSignals.forEach {
            it.stopBlocking()
        }
    }
}