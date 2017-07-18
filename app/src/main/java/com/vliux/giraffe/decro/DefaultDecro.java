package com.vliux.giraffe.decro;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vliux.giraffe.R;
import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.util.Apps;

import java.util.List;

/**
 * Created by vliux on 2017/7/18.
 */

public class DefaultDecro implements IDecro {
    @NonNull
    @Override
    public String getPackage() {
        return "ALL";
    }
    
    @Nullable
    @Override
    public Notification decro(@NonNull final Context context, @NonNull String pkg, @NonNull Apps.AppDesc appDesc, @NonNull List<NotificationRecord> records) {
        final Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(appDesc.label)
                .setContentText(context.getString(R.string.total_msg, String.valueOf(records.size())))
                .setSmallIcon(R.mipmap.ic_launcher);
    
        final Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        int i = 0;
        for (final NotificationRecord record : records) {
            inboxStyle.addLine("•" +record.getTitle() + ": " + record.getText());
            if (++i >= MAX_INBOX_ITEMS) break;
        }
        builder.setStyle(inboxStyle);
        return builder.build();
    }
}
