package pl.szymonchaber.donttouchthis.screenblocking

import android.content.Context
import pl.szymonchaber.donttouchthis.screenblocking.blockingsignals.BlockingSignal
import pl.szymonchaber.donttouchthis.screenblocking.blockingsignals.BlockingSignalAggregate
import pl.szymonchaber.donttouchthis.screenblocking.blockingsignals.OnShouldBlockListener
import pl.szymonchaber.donttouchthis.screenblocking.settings.SettingsManager

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