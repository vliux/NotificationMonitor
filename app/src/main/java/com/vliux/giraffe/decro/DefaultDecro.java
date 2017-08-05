package com.vliux.giraffe.decro;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.RemoteViews;

import com.vliux.giraffe.R;
import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.data.NotificationRecordStorage;
import com.vliux.giraffe.intent.IntentDeleteService;
import com.vliux.giraffe.intent.IntentLaunchService;
import com.vliux.giraffe.util.Apps;

import java.util.List;

/**
 * Created by vliux on 2017/7/18.
 */

class DefaultDecro implements IDecro {
    @NonNull
    @Override
    public String getPackage() {
        return "ALL";
    }
    
    @Nullable
    @Override
    public Notification decro(@NonNull final Context context, @NonNull String pkg,
                              @NonNull Apps.AppDesc appDesc,
                              @NonNull List<NotificationRecord> records) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentText(context.getString(R.string.total_msg, String.valueOf(records.size())))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDeleteIntent(IntentDeleteService.get(context, pkg))
                .setColor(context.getResources().getColor(R.color.colorAccent));
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) builder.setContentTitle(appDesc.label);
        else builder.setSubText(appDesc.label);
    
        final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        int i = 0;
        for (final NotificationRecord record : records) {
            addInboxStyle(inboxStyle, record);
            if (++i >= MAX_INBOX_ITEMS) break;
        }
        builder.setStyle(inboxStyle);
        final Notification notification = builder.build();

        i = 0;
        final RemoteViews remoteViews = notification.bigContentView;
        for (final NotificationRecord record : records) {
            setInboxItem(context, remoteViews, i, record);
            if (++i >= MAX_INBOX_ITEMS) break;
        }
        return notification;
    }

    private static void addInboxStyle(final NotificationCompat.InboxStyle inboxStyle,
                                      final NotificationRecord record) {
        final SpannableString ss = SpannableString.valueOf(record.getTitle());
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, record.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inboxStyle.addLine(new SpannableStringBuilder(ss).append(' ').append(record.getText()));
    }

    private static boolean setInboxItem(final Context context, RemoteViews rvs, final int index,
                                final NotificationRecord record){
        if (rvs == null) return false;
        final String itemResId = "inbox_text" + index;
        final int resId = Resources.getSystem().getIdentifier(itemResId, "id", "android");
        if (resId == 0) return false;
        try {
            final PendingIntent pendingIntent =
                    IntentLaunchService.getForNotification(context, record.getPkg(),
                            NotificationRecordStorage.get(record.getId()));
            if(null != pendingIntent)
                rvs.setOnClickPendingIntent(resId, pendingIntent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, String.format("error setting PendingIntent for inbox item: pkg=%s, itemResId=%s", record.getPkg(), itemResId), e);
            return false;
        }

    }
    
}
