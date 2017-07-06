package com.example.vliux.notificationlistener;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by vliux on 17/4/27.
 */

public class NotificationTracerService extends NotificationListenerService {
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "*** Current active notifications:");
        mNofiticationStats = new NotificationStats(this);
        mStorage = new NotificationRecordStorage(this);
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
        //final String group = sbn.getGroupKey();
        //final String key = sbn.getKey();
        
        final Notification notification = sbn.getNotification();
        final String title = NotificationParser.getTitle(notification.extras);
        final String text = NotificationParser.geText(notification.extras);
        
        Log.d(TAG, String.format("  Notification: pkg=%s", pkg));
        Log.d(TAG, "   \\_ " + text);
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
            mStorage.add(new NotificationRecord(pkg, null, null, title, text, time));
            NotificationChangedNotifier.notify(this);
        }
    }
    
    private NotificationRecordStorage mStorage;
    private NotificationStats mNofiticationStats;
    private static final String TAG = NotificationTracerService.class.getSimpleName();
    
}
