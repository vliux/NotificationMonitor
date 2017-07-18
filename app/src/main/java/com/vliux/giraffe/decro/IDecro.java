package com.vliux.giraffe.decro;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;

import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.util.Apps;

import java.util.List;

/**
 * Created by vliux on 2017/7/18.
 */

interface IDecro {
    @NonNull
    String getPackage();
    
    Notification decro(@NonNull final Context context, @NonNull final String pkg,
                       @NonNull final Apps.AppDesc appDesc, @NonNull final List<NotificationRecord> records);
    
    int MAX_INBOX_ITEMS = 5;
}
