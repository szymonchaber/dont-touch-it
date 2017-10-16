package pl.szymonchaber.donttouchit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class OverlayService extends Service implements SensorEventListener {

    public static final int BLOCKING_PROXIMITY = 8;
    private static final int ID = 1;
    private static final String CLICK = "CLICK";
    private static final String DELETION = "DELETION";
    private static final String TAG = "OverlayService";
    private WindowManager windowManager;
    private BackBlockingFrameLayout layout;
    private Notification.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification(getString(R.string.enable_blocking));
        if (intent != null) {
            String action = intent.getAction();

            if (CLICK.equals(action)) {
                switchState();
            } else if (DELETION.equals(action)) {
                notificationManager.cancelAll();
                stopBlocking();
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        createInvisibleOverlayView();
    }

    @Override
    public void onDestroy() {
        if (layout != null) {
            windowManager.removeView(layout);
        }
        super.onDestroy();
    }

    private void switchState() {
        if (layout.getVisibility() == View.GONE) {
            startSmartBlocking();
        } else {
            stopBlocking();
        }
    }

    private void startSmartBlocking() {
        showNotification(getString(R.string.disable_blocking));
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopBlocking() {
        showNotification(getString(R.string.enable_blocking));
        layout.setVisibility(View.GONE);
        sensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float proximity = event.values[0];

        if (proximity < BLOCKING_PROXIMITY) {
            if (!isScreenBlocked()) {
                blockScreen();
                Log.d(TAG, "onSensorChanged: blocked view");
            }
        } else {
            if (isScreenBlocked()) {
                unblockScreen();
                Log.d(TAG, "onSensorChanged: unblocked view");
            }
        }
        Log.d(TAG, "onSensorChanged: " + proximity);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //nop
    }

    private boolean isScreenBlocked() {
        return layout.getVisibility() == View.VISIBLE;
    }

    private void blockScreen() {
        layout.setVisibility(View.VISIBLE);
    }

    private void unblockScreen() {
        layout.setVisibility(View.GONE);
    }
}
