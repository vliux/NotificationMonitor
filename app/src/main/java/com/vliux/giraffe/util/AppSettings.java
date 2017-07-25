package com.vliux.giraffe.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vliux on 2017/7/8.
 */

public class AppSettings implements Closeable{
    
    public AppSettings(@NonNull final Context context) {
        mContext = context;
        mSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        mContext.registerReceiver(mUpdateReceiver, new IntentFilter(ACTION_UPDATED));
    }
    
    public void close(){
       mContext.unregisterReceiver(mUpdateReceiver);
    }
    
    public void set(@NonNull final String key, final boolean b){
        mSp.edit().putBoolean(key, b).commit();
        mContext.sendBroadcast(new Intent(ACTION_UPDATED).putExtra(EXTRA_PID, android.os.Process.myPid()));
    }
    
    public boolean getBoolean(@NonNull final String key, final boolean defaultValue){
        return mSp.getBoolean(key, defaultValue);
    }
    
    public void set(@NonNull final String key, @Nullable final List<String> stringList){
        //TODO
    }
    
    public List<String> getStringList(@NonNull final String key){
        //TODO
        return new ArrayList<>(0);
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
