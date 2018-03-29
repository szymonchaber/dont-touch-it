package pl.szymonchaber.donttouchthis.permissions

interface OnPermissionResultListener {

    fun onPermissionGranted()
    fun onError()
    fun requestPermission()
}