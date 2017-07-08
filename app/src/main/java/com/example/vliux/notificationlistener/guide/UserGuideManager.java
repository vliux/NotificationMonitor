package com.example.vliux.notificationlistener.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vliux on 2017/7/8.
 */

public class UserGuideManager {
    public static void showUserGuideIfNeeded(final Context context){
        context.startActivity(new Intent(context, UserGuideActivity.class));
    }
    
    static void setUserGuideShown(final Context context){
        
    }
    
    static SharedPreferences getSharedPreferences(final Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static final String KEY_WECHAT_ONLY = "wechat_only";
    static final boolean DEFAULT_WECHAT_ONLY = true;
}
