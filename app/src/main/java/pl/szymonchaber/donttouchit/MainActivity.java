package pl.szymonchaber.donttouchit;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showOverlayIfPossible();
    }

    private void showOverlayIfPossible() {
        if (permissionRequestRequired()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            startOverlayService();
            finish();
        }
    }

    private boolean permissionRequestRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !canDrawOverlays();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (canDrawOverlays()) {
                startOverlayService();
            } else {
                Toast.makeText(this, R.string.no_permission_no_overlay, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private boolean canDrawOverlays() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
    }

    private void startOverlayService() {
        Intent intent = new Intent(this, OverlayService.class);
        startService(intent);
    }
}


