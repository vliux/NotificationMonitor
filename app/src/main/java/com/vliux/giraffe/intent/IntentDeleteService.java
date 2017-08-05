package com.vliux.giraffe.intent;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vliux.giraffe.data.NotificationRecordStorage;

/**
 * Created by vliux on 2017/8/4.
 * @author vliux
 */

public class IntentDeleteService extends IntentService {
    
    private static final String BASE_URI = "content://com.vliux.giraffe/delete#";
    
    public static PendingIntent get(@NonNull final Context context, @NonNull final String pkg){
        return PendingIntent.getService(context, 0,
                new Intent(context, IntentDeleteService.class).setData(Uri.parse(BASE_URI + pkg)),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    
    public IntentDeleteService() {
        super("IntentDeleteService");
    }
    
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("vliux", "IntentDeleteService.onHandleIntent() ");
        if(null == intent) return;
        final Uri data = intent.getData();
        if(null == data) return;
        final String pkg = pkg(data);
        if(null == pkg || pkg.length() <= 0) return;
        final NotificationRecordStorage storage = new NotificationRecordStorage(this);
        storage.delete(pkg);
        storage.close();
    }
    
    private static String pkg(final Uri uri){
        return uri.getFragment();
    }
    
    private static final String TAG = "IntDel";
}
