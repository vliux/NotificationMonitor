package com.example.vliux.notificationlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by vliux on 17/7/6.
 */

class NotificationChangedNotifier {
    static void notify(final Context context){
        context.getApplicationContext()
                .sendBroadcast(new Intent(ACTION_NOTIFICATION_CAHNGED).setPackage(context.getPackageName()));
    }
    
    static void register(final Context context, final BroadcastReceiver receiver){
        context.getApplicationContext()
                .registerReceiver(receiver, new IntentFilter(ACTION_NOTIFICATION_CAHNGED));
    }
    
    private static final String ACTION_NOTIFICATION_CAHNGED = "com.vliux.notification.NOTIF_CHANGED";
}
