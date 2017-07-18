package com.vliux.giraffe.listener;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by vliux on 17/7/5.
 * Package-wise notification state tracing.
 */

class NotificationStats {
    public NotificationStats(final Context context){
        mSp = context.getSharedPreferences("notif_stats", Context.MODE_PRIVATE);
    }
    
    public boolean setFirstRun(final boolean firstRun){
        final Map map = mSp.getAll();
        final boolean result = (null == map || map.size() <= 0);
        mSp.edit().putBoolean("first_run", false).apply();
        return result;
    }
    
    private final SharedPreferences mSp;
}
