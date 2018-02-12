package pl.szymonchaber.donttouchit.screenblocking.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class BackBlockingFrameLayout extends FrameLayout {

    public BackBlockingFrameLayout(@NonNull Context context) {
        super(context);
    }

    public BackBlockingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BackBlockingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return event != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK || super.dispatchKeyEvent(event);
    }

    public ViewGroup.LayoutParams createParams() {
        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        return params;
    }
}
