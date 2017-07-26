package com.vliux.giraffe.util;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.APP_OPEN;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_CONTENT;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.TUTORIAL_BEGIN;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.TUTORIAL_COMPLETE;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_CATEGORY;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_NAME;

/**
 * Created by vliux on 2017/7/20.
 */

public class Analytics {
    private static FirebaseAnalytics sAnalytics;
    
    public static void init(final Application application){
        initFirebase(application);
        sAnalytics = FirebaseAnalytics.getInstance(application);
    }
    
    /*
     * init by code, to avoid have a ContentProvider running in main proc.
     * https://firebase.googleblog.com/2017/03/take-control-of-your-firebase-init-on.html
     */
    private static void initFirebase(final Application application){
        FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
                .setApplicationId("1:141615603992:android:fa9b58c2ed2a46c6")
                .setApiKey("AIzaSyAvpcUWjxs-FIkxyMN6pzNxjW-x7PpiKIw")
                .setDatabaseUrl("https://giraffe-b3dc1.firebaseio.com")
                .setStorageBucket("giraffe-b3dc1.appspot.com");
        FirebaseApp.initializeApp(application, builder.build());
    }
    
    public static void logMenuEvent(final String action){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, action);
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logAppOpen(){
        sAnalytics.logEvent(APP_OPEN, null);
    }
    
    public static void logSettingsUnsupported(@NonNull final Context context){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, "settingUnsupport");
        putIfNotEmpty(bundle, "androidId", DeviceUtil.getAndroidId(context));
        putIfNotEmpty(bundle, "devBrand", DeviceUtil.getDeviceBrand());
        putIfNotEmpty(bundle, "devModel", DeviceUtil.getDeviceModel());
        putIfNotEmpty(bundle, "devName", DeviceUtil.getDeviceName());
        putIfNotEmpty(bundle, "manufacture", DeviceUtil.getManufacture());
        putIfNotEmpty(bundle, "sysVer", DeviceUtil.getSystemVersion());
        putIfNotEmpty(bundle, "bldSerial", DeviceUtil.getBuildSerial());
        putIfNotEmpty(bundle, "sysLang", DeviceUtil.getSystemLanguage());
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logBindServiceOnUi(){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, "bind_srv");
        bundle.putString(ITEM_CATEGORY, ":main");
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logBindService(){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, "bind_srv");
        bundle.putString(ITEM_CATEGORY, ":service");
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logUnbindService(){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, "unbind_srv");
        bundle.putString(ITEM_CATEGORY, ":service");
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logWizardOpen(){
        sAnalytics.logEvent(TUTORIAL_BEGIN, null);
    }
    
    public static void logWizardComplete(){
        sAnalytics.logEvent(TUTORIAL_COMPLETE, null);
    }
    
    public static void logClickListItem(final String pkg){
        final Bundle bundle = new Bundle();
        bundle.putString("pkg", pkg);
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
    }
    
    public static void logError(@NonNull final Throwable e){
        final Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, "err");
        bundle.putString(ITEM_NAME, "reqBind");
        sAnalytics.logEvent(SELECT_CONTENT, bundle);
        
    }
    private static void putIfNotEmpty(final Bundle bundle, final String key, final String value){
        if(null != key && key.length() > 0
                && null != value && value.length() > 0)
            bundle.putString(key, value);
    }
}
