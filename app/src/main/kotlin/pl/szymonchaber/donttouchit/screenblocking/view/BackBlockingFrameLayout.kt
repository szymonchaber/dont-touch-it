package pl.szymonchaber.donttouchit.screenblocking.view

import android.content.Context
import android.graphics.PixelFormat
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout

class BackBlockingFrameLayout : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return when (event?.keyCode) {
            KeyEvent.KEYCODE_BACK -> true
            else                  -> super.dispatchKeyEvent(event)
        }
    }

    fun createParams(): ViewGroup.LayoutParams {
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }
    }
}
