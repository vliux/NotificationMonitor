package com.vliux.giraffe.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.vliux.giraffe.decro.NotificationDecroService;

/**
 * Created by vliux on 17/7/6.
 */

public class TraceServiceNotifier {
    public static void notifyNotificationUpdated(final Context context, final String pkg){
        context.getApplicationContext()
                .sendBroadcast(new Intent(ACTION_NOTIFICATION_CAHNGED).setPackage(context.getPackageName()));
        NotificationDecroService.decro(context, pkg);
    }
    
    public static void registerNotificationUpdated(final Context context, final BroadcastReceiver receiver){
        context.getApplicationContext()
                .registerReceiver(receiver, new IntentFilter(ACTION_NOTIFICATION_CAHNGED));
    }
    
    public static void unregister(final Context context, final BroadcastReceiver receiver){
        context.getApplicationContext()
                .unregisterReceiver(receiver);
    }
    
    public static void notifyServiceBound(final Context context){
        context.getApplicationContext()
                .sendBroadcast(new Intent(ACTION_SERVICE_BOUND).setPackage(context.getPackageName()));
    }
    
    public static void registerServiceBound(final Context context, final BroadcastReceiver receiver){
        context.getApplicationContext()
                .registerReceiver(receiver, new IntentFilter(ACTION_SERVICE_BOUND));
    }
    
    private static final String ACTION_NOTIFICATION_CAHNGED = "com.vliux.giraffe.NOTIF_CHANGED";
    private static final String ACTION_SERVICE_BOUND = "com.vliux.giraffe.SERVICE_BOUND";
}
