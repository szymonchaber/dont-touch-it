package pl.szymonchaber.donttouchit.permissions

interface OnPermissionResultListener {

    fun onPermissionGranted()
    fun onError()
    fun requestPermission()
}