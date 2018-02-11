package pl.szymonchaber.donttouchit.screenblocking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

import pl.szymonchaber.donttouchit.R;

public class OverlayService extends Service implements OnShouldBlockListener {

    public static final int BLOCKING_PROXIMITY = 8;
    private static final int ID = 1;
    private static final String CLICK = "CLICK";
    private static final String DELETION = "DELETION";
    private WindowManager windowManager;
    private BackBlockingFrameLayout layout;
    private Notification.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private BlockingManager blockingManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification(getString(R.string.enable_blocking));
        if (intent != null) {
            consumeAction(intent.getAction());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void consumeAction(String action) {
        if (CLICK.equals(action)) {
            toggleBlocking();
        } else if (DELETION.equals(action)) {
            stopBlocking();
            notificationManager.cancelAll();
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationBuilder = new Notification.Builder(this).setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(notificationClickedIntent())
                .setDeleteIntent(notificationDeletedIntent());

        blockingManager = new BlockingManager(this);
        createInvisibleOverlayView();
    }

    @Override
    public void onDestroy() {
        if (layout != null) {
            windowManager.removeView(layout);
        }
        super.onDestroy();
    }

    private void toggleBlocking() {
        if (layout.getVisibility() == View.GONE) {
            startSmartBlocking();
        } else {
            stopBlocking();
        }
    }

    private void startSmartBlocking() {
        showNotification(getString(R.string.disable_blocking));
        blockingManager.startBlocking();
    }

    private void stopBlocking() {
        showNotification(getString(R.string.enable_blocking));
        layout.setVisibility(View.GONE);
        blockingManager.stopBlocking();
    }

    private PendingIntent notificationClickedIntent() {
        Intent intent = new Intent(this, OverlayService.class);
        intent.setAction(CLICK);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    private PendingIntent notificationDeletedIntent() {
        Intent intent = new Intent(this, OverlayService.class);
        intent.setAction(DELETION);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    private void createInvisibleOverlayView() {
        layout = new BackBlockingFrameLayout(this);
        layout.setVisibility(View.GONE);
        windowManager.addView(layout, layout.createParams());
    }

    private void showNotification(String message) {
        notificationManager.notify(ID, notificationBuilder.setContentText(message).build());
    }

    public void blockScreen() {
        layout.setVisibility(View.VISIBLE);
    }

    public void unblockScreen() {
        layout.setVisibility(View.GONE);
    }
}
