package pl.szymonchaber.donttouchit.screenblocking.blockingsignals

interface OnShouldBlockListener {

    fun blockScreen()
    fun unblockScreen()
}