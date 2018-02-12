package pl.szymonchaber.donttouchit.screenblocking.blockingsignals

class ToggleBlockingSignal(shouldBlockListener: OnShouldBlockListener) : BlockingSignal(shouldBlockListener) {

    override fun startBlocking() {
        shouldBlockListener.blockScreen()
    }

    override fun stopBlocking() {
        shouldBlockListener.unblockScreen()
    }
}