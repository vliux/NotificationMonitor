package com.vliux.giraffe.listener;

import android.app.Notification;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.data.NotificationRecordStorage;
import com.vliux.giraffe.intent.IntentCaches;
import com.vliux.giraffe.util.Analytics;
import com.vliux.giraffe.AppSettings;

import java.util.Set;

/**
 * Created by vliux on 17/4/27.
 * @author vliux
 */

public class NotificationTracerService extends NotificationListenerService {
    
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "*** Current active notifications:");
        Analytics.logBindService();
        mAppSettings = new AppSettings(this);
        mStorage = new NotificationRecordStorage(this);
        if(!mAppSettings.boundedEver()) {
            mAppSettings.setBoundedEver();
            for (final StatusBarNotification sbn : getActiveNotifications()) processNotification(sbn);
        }
        TraceServiceNotifier.notifyServiceBound(this);
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
    
    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        mStorage.close();
        Analytics.logUnbindService();
    }
    
    private void processNotification(final StatusBarNotification sbn){
        if(!intercept(sbn)) return;
        final String pkg = sbn.getPackageName();
        final long time = sbn.getPostTime();
        final Notification notification = sbn.getNotification();
        final String title = NotificationParser.getTitle(notification.extras);
        final String text = NotificationParser.geText(notification.extras);
        
        Log.d(TAG, String.format("  Notification: pkg=%s, t=%d, txt=%s", pkg, time, text));
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
            final Uri uri = mStorage.add(NotificationRecord.fromListener(pkg, title, text, time));
            Log.d(TAG, "   added to storage: " + uri);
            cachePendingIntent(pkg, uri, notification);
            TraceServiceNotifier.notifyNotificationUpdated(this, pkg);
            cancelNotification(sbn);
        }
    }

    private void cachePendingIntent(final String pkg, final Uri uri, final Notification notification){
        final PendingIntent pendingIntent =
                null != notification.contentIntent ? notification.contentIntent : notification.fullScreenIntent;
        if (null != pendingIntent) IntentCaches.get().add(pkg, uri, pendingIntent);
    }

    private boolean intercept(final StatusBarNotification sbn){
        final String pkg = sbn.getPackageName();
        if(getPackageName().equals(pkg)){
            Log.d(TAG, "ignore notification from myself");
            return false;
        }else if(sbn.isOngoing()) {
            Log.d(TAG, "notification is ongoing, ignored: " + pkg);
            return false;
        }
        
        final Set<String> pkgs = mAppSettings.getTargetPkgs();
        if(AppSettings.targetAllPkgs(pkgs)){
            Log.d(TAG, "target_apps is ALL, pkg will be processed: " + pkg);
            return true;
        } else {
            final boolean contains = pkgs.contains(pkg);
            Log.d(TAG, "pkg in target_apps? " + contains);
            return contains;
        }
    }
    
    private void cancelNotification(final StatusBarNotification sbn){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) cancelNotification(sbn.getKey());
        else //noinspection deprecation
            cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
    }
    
    private NotificationRecordStorage mStorage;
    private AppSettings mAppSettings;
    private static final String TAG = "NotifTracer";
    
}
