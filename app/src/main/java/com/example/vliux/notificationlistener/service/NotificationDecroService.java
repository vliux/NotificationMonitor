package com.example.vliux.notificationlistener.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.vliux.notificationlistener.R;
import com.example.vliux.notificationlistener.data.NotificationRecord;
import com.example.vliux.notificationlistener.data.NotificationRecordStorage;
import com.example.vliux.notificationlistener.util.Apps;

import java.util.List;

/**
 * Created by vliux on 2017/7/17.
 */

public class NotificationDecroService extends IntentService {
    public static void decro(final Context context, final String pkg){
        final Intent intent = new Intent(context, NotificationDecroService.class)
                .putExtra(Intent.EXTRA_PACKAGE_NAME, pkg);
        context.startService(intent);
    }
    
    public NotificationDecroService() {
        super("NotificationDecroService");
    }
    
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String pkg = pkg(intent);
        if(null == pkg || pkg.length() <= 0) return;
        
        final NotificationRecordStorage storage = new NotificationRecordStorage(this);
        final List<NotificationRecord> records = storage.get(pkg);
        if (null == records || records.size() <= 0) return;
        
        final NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        final Apps.AppDesc appDesc = Apps.ofDesc(this, pkg);
        final Integer uid = Apps.ofUid(this, pkg);
        if(null == appDesc || null == uid) return;
    
        final Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(appDesc.label + " " + records.get(0).getTitle())
                .setContentText(String.format("have %d messages", records.size()))
                .setSmallIcon(R.mipmap.ic_launcher);
    
        final Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        int i = 0;
        for (final NotificationRecord record : records) {
            inboxStyle.addLine(record.getText());
            if (++i >= MAX_GROUP_ITEMS) break;
        }
        builder.setStyle(inboxStyle);
        nm.notify(uid, builder.build());
    }
    
    private static String pkg(final Intent intent){
        return intent.getStringExtra(Intent.EXTRA_PACKAGE_NAME);
    }
    
    private static final int MAX_GROUP_ITEMS = 5;
}
