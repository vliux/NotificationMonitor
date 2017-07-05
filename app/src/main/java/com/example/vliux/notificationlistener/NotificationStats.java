package com.example.vliux.notificationlistener;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vliux on 17/7/5.
 * Package-wise notification state tracing.
 */

public class NotificationStats {
    public NotificationStats(final Context context){
        mSp = context.getSharedPreferences("notif_stats", Context.MODE_PRIVATE);
    }
    
    public long getLastTime(final String pkg){
        return mSp.getLong(pkg, INVALID_TIME);
    }
    
    public void setLastTime(final String pkg, final long time){
        mSp.edit().putLong(pkg, time).apply();
    }
    
    private final SharedPreferences mSp;
    public static final long INVALID_TIME = -1L;
}
