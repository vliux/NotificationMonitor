package com.vliux.giraffe;

import android.app.Application;

import com.vliux.giraffe.util.Analytics;

/**
 * Created by vliux on 2017/7/20.
 */

public class GiraffeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Analytics.init(this);
    }
}
