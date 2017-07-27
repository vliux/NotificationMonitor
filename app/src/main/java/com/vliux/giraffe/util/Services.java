package com.vliux.giraffe.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by vliux on 2017/7/27.
 */

public class Services {
    
    public static boolean isServiceRunning(@NonNull final Context context, @NonNull final Class<?> serviceClass) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (final ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
