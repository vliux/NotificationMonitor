package com.vliux.giraffe.listener;

import android.app.Notification;
import android.net.Uri;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.vliux.giraffe.Constants;
import com.vliux.giraffe.MainActivity;
import com.vliux.giraffe.NotificationChangedNotifier;
import com.vliux.giraffe.R;
import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.data.NotificationRecordStorage;
import com.vliux.giraffe.util.AppSettings;

import static com.vliux.giraffe.Constants.Settings.*;

/**
 * Created by vliux on 17/4/27.
 * @author vliux
 */

public class NotificationTracerService extends NotificationListenerService {
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "*** Current active notifications:");
        mNofiticationStats = new NotificationStats(this);
        mAppSettings = new AppSettings(this);
        mStorage = new NotificationRecordStorage(this);
        if(mNofiticationStats.setFirstRun(false)) {
            for (final StatusBarNotification sbn : getActiveNotifications()) {
                processNotification(sbn);
            }
        }
        MainActivity.start(getApplicationContext(), true);
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
        mAppSettings.close();
    }
    
    private void processNotification(final StatusBarNotification sbn){
        final String pkg = sbn.getPackageName();
        if(mAppSettings.get(getString(R.string.pref_wechat_only_k), DEFAULT_WECHAT_ONLY) && !Constants.Pkgs.WECHAT.equals(pkg)){
            Log.w(TAG, "WECHAT_ONLY mode, current pkg is not WeChat: " + pkg);
            return;
        }
        final long time = sbn.getPostTime();
        final Notification notification = sbn.getNotification();
        final String title = NotificationParser.getTitle(notification.extras);
        final String text = NotificationParser.geText(notification.extras);
        
        Log.d(TAG, String.format("  Notification: pkg=%s, t=%d, txt=%s", pkg, time, text));
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
            final Uri uri = mStorage.add(new NotificationRecord(pkg, title, text, time));
            Log.d(TAG, "   added to storage: " + uri);
            NotificationChangedNotifier.notify(this, pkg);
        }
        cancelNotification(sbn);
    }
    
    private void cancelNotification(final StatusBarNotification sbn){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) cancelNotification(sbn.getKey());
        else cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
    }
    
    private NotificationRecordStorage mStorage;
    private NotificationStats mNofiticationStats;
    private AppSettings mAppSettings;
    private static final String TAG = NotificationTracerService.class.getSimpleName();
    
}
