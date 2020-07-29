package app.sen.video.utils

import android.app.Application


object GlobalContext {

    val instance: Application by lazy {
        ApplicationUtil.getApplicationByReflection()
    }

    @JvmStatic
    fun get():Application=instance


}