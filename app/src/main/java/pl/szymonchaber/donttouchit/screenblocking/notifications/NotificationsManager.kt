package pl.szymonchaber.donttouchit.screenblocking.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pl.szymonchaber.donttouchit.R
import pl.szymonchaber.donttouchit.screenblocking.OverlayService

internal class NotificationsManager(private val context: Context, private val listener: OnNotificationActionListener) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationBuilder = Notification.Builder(context).setSmallIcon(android.R.drawable.btn_star)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentIntent(notificationClickedIntent())
            .setDeleteIntent(notificationDeletedIntent())

    fun consumeAction(action: String?) {
        when (action) {
            CLICK -> listener.onNotificationClick()
            DISMISS -> {
                listener.onNotificationDismiss()
                notificationManager.cancelAll()
            }
        }
    }

    fun notify(message: String) = notificationManager.notify(
            ID, notificationBuilder.setContentText(message).build())

    private fun notificationClickedIntent(): PendingIntent {
        val intent = Intent(context, OverlayService::class.java).apply {
            action = CLICK
        }
        return PendingIntent.getService(context, 0, intent, 0)
    }

    private fun notificationDeletedIntent(): PendingIntent {
        val intent = Intent(context, OverlayService::class.java).apply {
            action = DISMISS
        }
        return PendingIntent.getService(context, 0, intent, 0)
    }

    companion object {

        private const val ID = 1
        private const val CLICK = "CLICK"
        private const val DISMISS = "DISMISS"
    }
}