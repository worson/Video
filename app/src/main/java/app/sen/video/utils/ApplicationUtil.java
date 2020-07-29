package app.sen.video.utils;

import android.app.Application;

public class ApplicationUtil {


    private ApplicationUtil() {
    }


    public static Application getApplicationByReflection() {
        try {
            return (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
