package com.example.vliux.notificationlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.vliux.notificationlistener.service.NotificationDecroService;

/**
 * Created by vliux on 17/7/6.
 */

public class NotificationChangedNotifier {
    public static void notify(final Context context, final String pkg){
        context.getApplicationContext()
                .sendBroadcast(new Intent(ACTION_NOTIFICATION_CAHNGED).setPackage(context.getPackageName()));
        NotificationDecroService.decro(context, pkg);
    }
    
    public static void register(final Context context, final BroadcastReceiver receiver){
        context.getApplicationContext()
                .registerReceiver(receiver, new IntentFilter(ACTION_NOTIFICATION_CAHNGED));
    }
    
    public static void unregister(final Context context, final BroadcastReceiver receiver){
        context.getApplicationContext()
                .unregisterReceiver(receiver);
    }
    
    private static final String ACTION_NOTIFICATION_CAHNGED = "com.vliux.notification.NOTIF_CHANGED";
}
