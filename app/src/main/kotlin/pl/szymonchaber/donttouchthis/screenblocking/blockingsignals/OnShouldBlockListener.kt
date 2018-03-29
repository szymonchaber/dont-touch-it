package pl.szymonchaber.donttouchthis.screenblocking.blockingsignals

interface OnShouldBlockListener {

    fun blockScreen()
    fun unblockScreen()
}