package com.vliux.giraffe.util;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.APP_OPEN;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_CONTENT;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_CATEGORY;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID;

/**
 * Created by vliux on 2017/7/20.
 */

public class Analytics {
    private static FirebaseAnalytics sAnalytics;
    
    public static void init(final Application application){
        sAnalytics = FirebaseAnalytics.getInstance(application);
    }
    
    public static void logMenuEvent(final String action){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, action);
        bundle.putString(ITEM_CATEGORY, ":main");
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logAppOpen(){
        sAnalytics.logEvent(APP_OPEN, null);
    }
    
    public static void logBindServiceOnUi(){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, "bind_srv");
        bundle.putString(ITEM_CATEGORY, ":main");
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logBindServiceOnListener(){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, "bind_srv");
        bundle.putString(ITEM_CATEGORY, ":service");
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
}
