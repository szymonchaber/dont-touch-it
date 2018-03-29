package pl.szymonchaber.donttouchthis.screenblocking.blockingsignals

abstract class BlockingSignal(internal val shouldBlockListener: OnShouldBlockListener) {

    abstract fun startBlocking()
    abstract fun stopBlocking()
}