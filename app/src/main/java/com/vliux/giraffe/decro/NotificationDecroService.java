package com.vliux.giraffe.decro;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.data.NotificationRecordStorage;
import com.vliux.giraffe.util.Apps;

import java.util.List;

import static com.vliux.giraffe.Constants.IntentExtras.*;

/**
 * Created by vliux on 2017/7/17.
 */

public class NotificationDecroService extends IntentService {
    public static void decro(final Context context, final String pkg){
        final Intent intent = new Intent(context, NotificationDecroService.class)
                .putExtra(PKG, pkg);
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
    
        nm.notify(uid, DecroDelegates.get().decro(this, pkg, appDesc, records));
    }
    
    private static String pkg(final Intent intent){
        return intent.getStringExtra(PKG);
    }
    
}
