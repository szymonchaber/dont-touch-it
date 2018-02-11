package pl.szymonchaber.donttouchit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import pl.szymonchaber.donttouchit.permissions.OnPermissionResultListener
import pl.szymonchaber.donttouchit.permissions.OverlayPermissionController
import pl.szymonchaber.donttouchit.screenblocking.OverlayService

class MainActivity : AppCompatActivity(), OnPermissionResultListener {

    private lateinit var overlayPermissionController: OverlayPermissionController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overlayPermissionController = OverlayPermissionController(this, this)
        overlayPermissionController.getPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            overlayPermissionController.onPermissionRequestResult()
        }
    }

    override fun requestPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + packageName))
        ActivityCompat.startActivityForResult(this, intent,
                MainActivity.REQUEST_CODE_OVERLAY_PERMISSION, null)
    }

    override fun onPermissionGranted() {
        startOverlayService()
    }

    override fun onError() {
        Toast.makeText(this, R.string.no_permission_no_overlay, Toast.LENGTH_SHORT).show()
    }

    private fun startOverlayService() {
        startService(Intent(this, OverlayService::class.java))
    }

    companion object {

        const val REQUEST_CODE_OVERLAY_PERMISSION = 250
    }
}