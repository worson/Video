package app.sen.video.utils

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 */
class Pref<T>(
    val context: Context,
    val prefName: String = "pref",
    val key: String,
    val default: T
) : ReadWriteProperty<Any, T> {
    private val pref by lazy { context.getSharedPreferences(prefName, Context.MODE_PRIVATE) }
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return when (default) {
            is Boolean -> pref.getBoolean(key, default)
            is Float -> pref.getFloat(key, default)
            is Int -> pref.getInt(key, default)
            is Long -> pref.getLong(key, default)
            is String -> pref.getString(key, default)
            else -> throw IllegalArgumentException("unsupported type")
        } as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        with(pref.edit()) {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("unsupported type")
            }
            apply()
        }
    }
}