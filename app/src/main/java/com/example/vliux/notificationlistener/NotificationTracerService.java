package com.example.vliux.notificationlistener;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by vliux on 17/4/27.
 */

public class NotificationTracerService extends NotificationListenerService {
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "*** Current active notifications:");
        mNofiticationStats = new NotificationStats(this);
        for(final StatusBarNotification sbn : getActiveNotifications()){
            processNotification(sbn);
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(TAG, "*** onNotificationPosted:");
        processNotification(sbn);
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onListenerHintsChanged(int hints) {
        super.onListenerHintsChanged(hints);
    }

    private void processNotification(final StatusBarNotification sbn){
        final String pkg = sbn.getPackageName();
        final long time = sbn.getPostTime();
        if(mNofiticationStats.getLastTime(pkg) > time){
            Log.w(TAG, "notification.time < recorded_time for " + pkg);
            return;
        }
        //if(!"com.tencent.mm".equals(pkg)) return;
        final String group = sbn.getGroupKey();
        final String key = sbn.getKey();
        
        final Notification notification = sbn.getNotification();
        final String text = notification.extras.toString();
        
        Log.d(TAG, String.format("  Notification: pkg=%s, groupkey=%s, key=%s",
                pkg, group, key));
        Log.d(TAG, "   \\_ " + text);
        EventBus.getDefault().post(new Event(pkg, group, key, text, time));
    }
    
    private NotificationStats mNofiticationStats;
    private static final String TAG = NotificationTracerService.class.getSimpleName();
}
