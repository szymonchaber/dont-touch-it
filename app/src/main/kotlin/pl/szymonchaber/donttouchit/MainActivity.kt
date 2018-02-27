package pl.szymonchaber.donttouchit

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import pl.szymonchaber.donttouchit.permissions.OnPermissionResultListener
import pl.szymonchaber.donttouchit.permissions.OverlayPermissionController
import pl.szymonchaber.donttouchit.screenblocking.OverlayService
import pl.szymonchaber.donttouchit.screenblocking.settings.BlockingMode.SMART
import pl.szymonchaber.donttouchit.screenblocking.settings.BlockingMode.TOGGLE
import pl.szymonchaber.donttouchit.screenblocking.settings.SettingsManager
import pl.szymonchaber.donttouchit.screenblocking.settings.SignalType

class MainActivity : AppCompatActivity(), OnPermissionResultListener {

    private lateinit var overlayPermissionController: OverlayPermissionController
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        overlayPermissionController = OverlayPermissionController(this, this)

        settingsManager = SettingsManager(this)
        restartBlocking()

        setModeViews()
        setSignalViews()
    }

    private fun setModeViews() {
        when (settingsManager.getMode()) {
            TOGGLE -> findViewById<RadioButton>(R.id.blockingModeToggle)
            else -> findViewById<RadioButton>(R.id.blockingModeSmart)
        }.isChecked = true

        findViewById<RadioGroup>(R.id.blockingMode).setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.blockingModeToggle -> {
                    settingsManager.setMode(TOGGLE)
                }

                R.id.blockingModeSmart -> {
                    settingsManager.setMode(SMART)
                }
            }
            setSignalViews()
            restartBlocking()
        }
    }

    private fun setSignalViews() {
        val radioGroup = findViewById<RadioGroup>(R.id.blockingSignal)

        if (settingsManager.getMode() == SMART) {
            radioGroup.visibility = View.VISIBLE
        } else {
            radioGroup.visibility = View.GONE
        }

        when (settingsManager.getSignalType()) {
            SignalType.PROXIMITY -> findViewById<RadioButton>(R.id.blockingByProximity)
            else -> findViewById<RadioButton>(R.id.blockingByRotation)
        }.isChecked = true


        radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.blockingByProximity -> {
                    settingsManager.setSignalType(SignalType.PROXIMITY)
                }

                R.id.blockingByRotation -> {
                    settingsManager.setSignalType(SignalType.ROTATION)
                }
            }
            restartBlocking()
        }
    }

    private fun restartBlocking() {
        stopOverlayService()
        overlayPermissionController.getPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            overlayPermissionController.onPermissionRequestResult()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
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
        startService(createOverlayServiceIntent())
    }

    private fun stopOverlayService() {
        stopService(createOverlayServiceIntent())
    }

    private fun createOverlayServiceIntent() = Intent(this, OverlayService::class.java)

    companion object {

        const val REQUEST_CODE_OVERLAY_PERMISSION = 250
    }
}