package com.vliux.giraffe.decro;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;

import com.vliux.giraffe.Constants;
import com.vliux.giraffe.R;
import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.util.Apps;

import java.util.List;

/**
 * Created by vliux on 2017/7/18.
 * @author vliux
 */

class WechatDecro implements IDecro {
    
    @NonNull
    @Override
    public String getPackage() {
        return Constants.Pkgs.WECHAT;
    }
    
    @Override
    public Notification decro(@NonNull final Context context, @NonNull String pkg, @NonNull Apps.AppDesc appDesc, @NonNull List<NotificationRecord> records) {
        final Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(appDesc.label)
                .setContentText(context.getString(R.string.total_msg, String.valueOf(records.size())))
                .setSmallIcon(R.mipmap.ic_launcher);
    
        final Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        int i = 0;
        for (final NotificationRecord record : records) {
            inboxStyle.addLine(Constants.Item.DOT + record.getText());
            if (++i >= MAX_INBOX_ITEMS) break;
        }
        builder.setStyle(inboxStyle);
        return builder.build();
    }
}
