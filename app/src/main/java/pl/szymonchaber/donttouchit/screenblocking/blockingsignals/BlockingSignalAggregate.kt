package pl.szymonchaber.donttouchit.screenblocking.blockingsignals

import android.content.Context
import pl.szymonchaber.donttouchit.screenblocking.settings.BlockingMode
import pl.szymonchaber.donttouchit.screenblocking.settings.SettingsManager

class BlockingSignalAggregate(settingsManager: SettingsManager, context: Context,
        shouldBlockListener: OnShouldBlockListener) : BlockingSignal(shouldBlockListener) {

    private val blockingSignals: MutableList<BlockingSignal> = ArrayList()

    init {
        when (settingsManager.getMode()) {
            BlockingMode.TOGGLE -> blockingSignals.add(
                    ToggleBlockingSignal(shouldBlockListener))
            BlockingMode.SMART -> blockingSignals.add(
                    LightBlockingSignal(context, shouldBlockListener))
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