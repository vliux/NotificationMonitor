package com.vliux.giraffe;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.util.Log;

import com.vliux.giraffe.listener.NotificationTracerService;
import com.vliux.giraffe.util.Analytics;

/**
 * Created by vliux on 2017/7/21.
 * @author vliux
 */

class TracerEnsurer {
    /**
     * Ensure that the NotificationTracerListener is bound to system notification service,
     * if it has been assigned the permission.
     */
    static void ensure(final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final AppSettings appSettings = new AppSettings(context);
            if (appSettings.boundedEver())
                try {
                    NotificationListenerService.requestRebind(
                            new ComponentName(context.getPackageName(), NotificationTracerService.class.getName()));
                }catch (final Throwable e){
                    Log.w("TracerEnsurer", "failed to request rebind", e);
                    Analytics.logError(e);
                }
        }
    }
}
