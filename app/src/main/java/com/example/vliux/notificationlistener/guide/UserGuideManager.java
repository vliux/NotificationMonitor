package com.example.vliux.notificationlistener.guide;

import android.content.Context;
import android.content.Intent;

/**
 * Created by vliux on 2017/7/8.
 */

public class UserGuideManager {
    public static void showUserGuideIfNeeded(final Context context){
        context.startActivity(new Intent(context, UserGuideActivity.class));
    }
    
    static void setUserGuideShown(final Context context){
        
    }
}