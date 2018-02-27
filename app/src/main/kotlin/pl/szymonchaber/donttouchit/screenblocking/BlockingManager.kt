package pl.szymonchaber.donttouchit.screenblocking

import android.content.Context
import pl.szymonchaber.donttouchit.screenblocking.blockingsignals.BlockingSignal
import pl.szymonchaber.donttouchit.screenblocking.blockingsignals.BlockingSignalAggregate
import pl.szymonchaber.donttouchit.screenblocking.blockingsignals.OnShouldBlockListener
import pl.szymonchaber.donttouchit.screenblocking.settings.SettingsManager

class BlockingManager(context: Context, listener: OnShouldBlockListener) {

    private val blockingSignal: BlockingSignal = BlockingSignalAggregate(
            SettingsManager(context), context, listener)

    var isBlocking = false

    fun startBlocking() {
        isBlocking = true
        blockingSignal.startBlocking()
    }

    fun stopBlocking() {
        isBlocking = false
        blockingSignal.stopBlocking()
    }
}