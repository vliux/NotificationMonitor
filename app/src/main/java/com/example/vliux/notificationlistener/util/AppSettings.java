package com.example.vliux.notificationlistener.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Closeable;

/**
 * Created by vliux on 2017/7/8.
 */

public class AppSettings implements Closeable{
    
    public AppSettings(final Context context) {
        mContext = context;
        mSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        mContext.registerReceiver(mUpdateReceiver, new IntentFilter(ACTION_UPDATED));
    }
    
    public void close(){
       mContext.unregisterReceiver(mUpdateReceiver);
    }
    
    public void setBoolean(final String key, final boolean b){
        mSp.edit().putBoolean(key, b).apply();
        mContext.sendBroadcast(new Intent(ACTION_UPDATED).putExtra(EXTRA_PID, android.os.Process.myPid()));
    }
    
    public boolean get(final String key, final boolean defaultValue){
        return mSp.getBoolean(key, defaultValue);
    }
    
    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(android.os.Process.myPid() != intent.getIntExtra(EXTRA_PID, 0)){
                Log.d(TAG, "AppSettings update detected");
                mContext.getSharedPreferences(NAME, Context.MODE_MULTI_PROCESS);
            }
        }
    };
    
    private final Context mContext;
    private final SharedPreferences mSp;
    private static final String NAME = "settings";
    
    private static final String ACTION_UPDATED = "com.vliux.notification.APPSETTINGS_UPDATED";
    private static final String EXTRA_PID = "pid";
    
    private static final String TAG = "AppSettings";
}
