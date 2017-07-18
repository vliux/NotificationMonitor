package com.vliux.giraffe.decro;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.util.Apps;

import java.util.List;

/**
 * Created by vliux on 2017/7/18.
 */

class DecroDelegates {
    
    static DecroDelegates get(){
        return Instance.d;
    }
    
    private static class Instance {
        private static DecroDelegates d = new DecroDelegates();
    }
    
    private DecroDelegates() {
        final IDecro wechatDecro = new WechatDecro();
        mDecros.put(wechatDecro.getPackage(), wechatDecro);
    }
    
    @NonNull
    public Notification decro(@NonNull Context context, @NonNull String pkg, @NonNull Apps.AppDesc appDesc, @NonNull List<NotificationRecord> records) {
        IDecro decro = mDecros.get(pkg);
        if(null != decro) decro = mDefault;
        return decro.decro(context, pkg, appDesc, records);
    }
    
    private final ArrayMap<String, IDecro> mDecros = new ArrayMap<>();
    private final IDecro mDefault = new DefaultDecro();
}
