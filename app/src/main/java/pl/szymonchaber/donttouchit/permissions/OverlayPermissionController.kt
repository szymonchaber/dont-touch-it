package pl.szymonchaber.donttouchit.permissions

import android.os.Build
import android.provider.Settings
import android.support.v7.app.AppCompatActivity

class OverlayPermissionController(private val context: AppCompatActivity,
        private val onPermissionResultListener: OnPermissionResultListener) {

    fun onPermissionRequestResult() {
        if (canDrawOverlays()) {
            onPermissionResultListener.onPermissionGranted()
        } else {
            onPermissionResultListener.onError()
        }
    }

    fun getPermission() {

        if (permissionRequestRequired()) {
            onPermissionResultListener.requestPermission()
        } else {
            onPermissionResultListener.onPermissionGranted()
        }
    }

    private fun permissionRequestRequired(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !canDrawOverlays()
    }

    private fun canDrawOverlays(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(
                context)
    }
}