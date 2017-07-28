package com.vliux.giraffe.listener;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by vliux on 2017/7/28.
 */

public class TracerEnsurerService extends IntentService {
    
    public TracerEnsurerService() {
        super("TracerEnsurerService ");
    }
    
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "TracerEnsurerService is running ...");
        TracerEnsurer.ensureServiceRunning(this);
    }
    
    private static final String TAG = "TracerEnsurerService";
}
