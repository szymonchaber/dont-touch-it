package pl.szymonchaber.donttouchthis.screenblocking.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import pl.szymonchaber.donttouchthis.MainActivity
import pl.szymonchaber.donttouchthis.R
import pl.szymonchaber.donttouchthis.screenblocking.OverlayService

internal class NotificationsManager(private val context: Context, private val listener: OnNotificationActionListener) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationBuilder: NotificationCompat.Builder

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID, context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        val action = NotificationCompat.Action.Builder(
            R.drawable.ic_settings_black_24dp,
            context.getString(R.string.notification_settings),
            PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)
        ).build()

        notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentIntent(notificationClickedIntent())
            .setDeleteIntent(notificationDeletedIntent())
            .addAction(action)
    }

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
        ID, notificationBuilder.setContentText(message).build()
    )

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
        private const val CHANNEL_ID = "main_channel_id"
    }
}