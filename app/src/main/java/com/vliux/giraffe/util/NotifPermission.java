package com.vliux.giraffe.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.vliux.giraffe.R;

import java.util.List;

/**
 * Created by vliux on 2017/7/8.
 */

public class NotifPermission {
    
    @Nullable
    public static Intent intent(@NonNull final Activity activity){
        final Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        return null != intent.resolveActivity(activity.getPackageManager()) ?
                intent : null;
    }
    
    public static boolean request(@NonNull final Activity activity){
        Analytics.logBindServiceOnUi();
        final Intent intent = intent(activity);
        if(null != intent){
            activity.startActivity(intent);
            Toast.makeText(activity,
                    activity.getString(R.string.goto_bind_msg_2, activity.getString(R.string.service_name)),
                    Toast.LENGTH_LONG).show();
            return true;
        }else return false;
    }
    
    public static void showUnsupported(@NonNull final Activity activity){
        Analytics.logSettingsUnsupported(activity);
        new AlertDialog.Builder(activity)
                .setMessage(R.string.dev_not_support)
                .setCancelable(false)
                .setPositiveButton(R.string.quit, (dialog, which) -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
                        final List<ActivityManager.AppTask> tasks = am.getAppTasks();
                        if (null != tasks) for (final ActivityManager.AppTask t : tasks)
                            t.finishAndRemoveTask();
                    }else {
                        activity.finish();
                    }
                }).show();
    }
}
