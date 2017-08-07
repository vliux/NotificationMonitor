package com.vliux.giraffe.intent;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vliux.giraffe.util.Apps;
import com.vliux.giraffe.util.Notifications;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.vliux.giraffe.Constants.IntentExtras.*;
import static com.vliux.giraffe.Constants.TAG;

/**
 * Created by vliux on 2017/7/22.
 * @author vliux
 */

public class IntentLaunchService extends IntentService {

    @Nullable
    public static PendingIntent getForNotification(@NonNull final Context context, @NonNull final String pkg, @NonNull final Uri uri){
        return PendingIntent.getService(context, 0,
                new Intent(context, IntentLaunchService.class).setData(uri).putExtra(PKG, pkg),
                FLAG_UPDATE_CURRENT);
    }

    public IntentLaunchService() {
        super("IntentLaunchService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(null == intent) return;
        final Uri uri = intent.getData();
        if(null == uri) return;
        final String pkg = intent.getStringExtra(PKG);
        if(null == pkg || pkg.length() <= 0) return;
        onLaunch(pkg, uri);
    }
    
    private void onLaunch(final String pkg, final Uri uri){
        final PendingIntent pendingIntent = IntentCaches.get().getAndRemove(pkg, uri);
        if(null != pendingIntent) try {
            pendingIntent.send();
            Notifications.collapseNotificationBar(this);
        } catch (final PendingIntent.CanceledException e) {
            Log.e(TAG, "failed to launch from PendingIntent for " + uri.toString(), e);
        } else {
            final Intent launcherIntent = Apps.ofLauncher(this, pkg);
            if (null != launcherIntent) {
                launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launcherIntent);
                Notifications.collapseNotificationBar(this);
            }
        }
    }
}
