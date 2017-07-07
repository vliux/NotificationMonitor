package com.example.vliux.notificationlistener.util;

import java.util.concurrent.Executor;

/**
 * Created by vliux on 17/7/6.
 */

public class AppExecutors {
    public static Executor get(){
        return sExecutor;
    }
    
    private static final Executor sExecutor = java.util.concurrent.Executors.newScheduledThreadPool(3);
}
