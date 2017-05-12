package pl.szymonchaber.donttouchit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


public class OverlayService extends Service {

    private static final int ID = 1;
    private static final String CLICK = "CLICK";
    private static final String DELETION = "DELETION";

    private WindowManager windowManager;
    private FrameLayout frameLayout;
    private Notification.Builder notificationBuilder;
    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification("Enable blocking");
        if ((intent != null)) {
            String action = intent.getAction();

            if (CLICK.equals(action)) {
                switchState();
            } else if (DELETION.equals(action)) {
                notificationManager.cancelAll();
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void switchState() {
        if (frameLayout.getVisibility() == View.GONE) {
            showNotification("Disable blocking");
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            showNotification("Enable blocking");
            frameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle("Don't touch!")
                .setContentIntent(notificationClickedIntent())
                .setDeleteIntent(notificationDeletedIntent());
        createInvisibleOverlayView();
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
        frameLayout = new FrameLayout(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;

        frameLayout.setVisibility(View.GONE);
        windowManager.addView(frameLayout, params);
    }

    @Override
    public void onDestroy() {
        if (frameLayout != null) windowManager.removeView(frameLayout);
        super.onDestroy();
    }

    private void showNotification(String message) {
        notificationManager.notify(ID, notificationBuilder.setContentText(message).build());
    }
}
