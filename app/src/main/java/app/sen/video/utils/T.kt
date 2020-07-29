package app.sen.video.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import app.sen.video.R
import app.sen.video.comm.L
import es.dmoral.toasty.Toasty

object T {
    init {
        Toasty.Config.getInstance()
            .setTextSize(13)
            .setToastTypeface(Typeface.DEFAULT)
            .apply()
    }

    const val SHORT = Toasty.LENGTH_SHORT
    const val LONG = Toasty.LENGTH_LONG
    private fun defaultHeight(): Int {
        val point = Point()
        val wm = GlobalContext.instance.applicationContext
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getRealSize(point)
        return point.y / 8
    }

    @SuppressLint("ResourceAsColor")
    fun normal(
        msg: String,
        context: Context=GlobalContext.instance,
        duration: Int = SHORT,
        @DrawableRes icon: Int = R.drawable.ic_launcher_foreground,
        @ColorRes backgroundColor: Int = R.color.toast_normal_bg,
        @ColorRes textColor: Int = R.color.toast_normal_text,
        withIcon: Boolean = false,
        shouldTint: Boolean = true
    ): Toast {
        L.i("toast", msg)
        return Toasty.custom(
            context, msg,
            ContextCompat.getDrawable(context, icon),
            backgroundColor, textColor, duration, withIcon, shouldTint
        ).apply {
            setGravity(Gravity.BOTTOM, 0, defaultHeight())
        }
    }


    fun normalWithIcon(
        msg: String,
        context: Context,
        duration: Int = SHORT,
        icon: Drawable,
        @ColorRes backgroundColor: Int = R.color.toast_normal_bg,
        @ColorRes textColor: Int = R.color.toast_normal_text,
        withIcon: Boolean = true,
        shouldTint: Boolean = true
    ): Toast {
        L.i("toast", msg)
        return Toasty.custom(
            context, msg,
            icon,
            ContextCompat.getColor(context, backgroundColor),
            ContextCompat.getColor(context, textColor),
            duration, withIcon, shouldTint
        ).apply {
            setGravity(Gravity.BOTTOM, 0, defaultHeight())
        }
    }
}