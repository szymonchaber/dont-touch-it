package pl.szymonchaber.donttouchit.screenblocking

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.View
import android.view.WindowManager

import pl.szymonchaber.donttouchit.R

class OverlayService : Service(), OnShouldBlockListener, OnNotificationActionListener {

    private lateinit var windowManager: WindowManager
    private lateinit var layout: BackBlockingFrameLayout

    private lateinit var blockingManager: BlockingManager
    private lateinit var notificationsManager: NotificationsManager

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action != null) {
            notificationsManager.consumeAction(intent.action)
        } else {
            showNotification(getString(R.string.enable_blocking))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        notificationsManager = NotificationsManager(this, this)
        blockingManager = BlockingManager(this, this)
        createInvisibleOverlayView()
    }

    override fun onDestroy() {
        windowManager.removeView(layout)
        super.onDestroy()
    }

    override fun onNotificationClick() = toggleBlocking()

    override fun onNotificationDismiss() {
        stopBlocking()
        stopSelf()
    }

    override fun blockScreen() {
        layout.visibility = View.VISIBLE
        showNotification(getString(R.string.disable_blocking))
    }

    override fun unblockScreen() {
        layout.visibility = View.GONE
        showNotification(getString(R.string.enable_blocking))
    }

    private fun createInvisibleOverlayView() {
        layout = BackBlockingFrameLayout(this)
        layout.visibility = View.GONE
        windowManager.addView(layout, layout.createParams())
    }

    private fun toggleBlocking() {
        if (blockingManager.isBlocking) {
            startBlocking()
        } else {
            stopBlocking()
        }
    }

    private fun startBlocking() {
        showNotification(getString(R.string.disable_blocking))
        blockingManager.startBlocking()
    }

    private fun showNotification(message: String) = notificationsManager.notify(message)

    private fun stopBlocking() {
        blockingManager.stopBlocking()
        layout.visibility = View.GONE
    }
}
