package com.vliux.giraffe.intent;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vliux.giraffe.Constants;
import com.vliux.giraffe.util.Apps;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by vliux on 2017/7/22.
 */

public class IntentLaunchService extends IntentService {
    private static final IntentCaches sCache = new IntentCaches();

    @Nullable
    public PendingIntent getForNotification(@NonNull final Context context, @NonNull final String pkg, @NonNull final Uri uri){
        return PendingIntent.getService(context, 0,
                new Intent(context, IntentLaunchService.class).setData(uri).putExtra(Intent.EXTRA_PACKAGE_NAME, pkg),
                FLAG_UPDATE_CURRENT);
    }

    @NonNull
    public static void cache(@NonNull final Uri uri, @NonNull final PendingIntent pendingIntent){
        sCache.add(uri, pendingIntent);
    }

    public IntentLaunchService() {
        super("IntentLaunchService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final Uri uri = intent.getData();
        if(null != uri){
            final PendingIntent pendingIntent = sCache.get(uri);
            if(null != pendingIntent) try {
                pendingIntent.send();
            } catch (final PendingIntent.CanceledException e) {
                Log.e(Constants.TAG, "failed to launch from PendingIntent for " + uri.toString(), e);
            } else {
                final String pkg = intent.getStringExtra(Intent.EXTRA_PACKAGE_NAME);
                if(null != pkg){
                    final Intent launcherIntent = Apps.ofLauncher(this, pkg);
                    if(null != launcherIntent) {
                        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launcherIntent);
                    }
                }
            }
        }
    }
}
