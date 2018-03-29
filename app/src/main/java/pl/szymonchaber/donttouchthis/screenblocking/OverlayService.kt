package pl.szymonchaber.donttouchthis.screenblocking

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.View
import android.view.WindowManager

import pl.szymonchaber.donttouchthis.R
import pl.szymonchaber.donttouchthis.screenblocking.blockingsignals.OnShouldBlockListener
import pl.szymonchaber.donttouchthis.screenblocking.notifications.NotificationsManager
import pl.szymonchaber.donttouchthis.screenblocking.notifications.OnNotificationActionListener
import pl.szymonchaber.donttouchthis.screenblocking.view.BackBlockingFrameLayout

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
            showNotification(getString(R.string.notification_enable_blocking))
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
    }

    override fun unblockScreen() {
        layout.visibility = View.GONE
    }

    private fun createInvisibleOverlayView() {
        layout = BackBlockingFrameLayout(this)
        layout.visibility = View.GONE
        windowManager.addView(layout, layout.createParams())
    }

    private fun toggleBlocking() {
        if (blockingManager.isBlocking) {
            stopBlocking()
        } else {
            startBlocking()
        }
    }

    private fun startBlocking() {
        showNotification(getString(R.string.notification_disable_blocking))
        blockingManager.startBlocking()
    }

    private fun stopBlocking() {
        showNotification(getString(R.string.notification_enable_blocking))
        blockingManager.stopBlocking()
        layout.visibility = View.GONE
    }

    private fun showNotification(message: String) = notificationsManager.notify(message)
}
