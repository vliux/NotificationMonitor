package com.example.vliux.notificationlistener.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vliux.notificationlistener.provider.NotificationRecordProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vliux on 17/7/5.
 */

public class NotificationRecordStorage {
    public NotificationRecordStorage(final Context context) {
        mContext = context.getApplicationContext();
    }
    
    @Nullable
    public List<NotificationRecord> get(){
        final Cursor cursor = mContext.getContentResolver().query(NotificationRecordProvider.RECORD_CONTENT_URI, null, null, null, null);
        if(null != cursor){
            final List<NotificationRecord> records = new ArrayList<>(cursor.getCount());
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                final String pkg = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_PKG));
                final String title = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TITLE));
                final String text = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TEXT));
                final long time = cursor.getLong(cursor.getColumnIndex(NotificationRecord.COL_TIME));
                records.add(new NotificationRecord(pkg, title, text, time));
            }
            return records;
        }
        return null;
    }
    
    public Uri add(@NonNull final NotificationRecord record){
        if(null != record) {
            final ContentValues cv = new ContentValues();
            cv.put(NotificationRecord.COL_PKG, record.pkg);
            cv.put(NotificationRecord.COL_TITLE, record.title);
            cv.put(NotificationRecord.COL_TEXT, record.text);
            cv.put(NotificationRecord.COL_TIME, record.time);
            return mContext.getContentResolver().insert(NotificationRecordProvider.RECORD_CONTENT_URI, cv);
        }
        return null;
    }
    
    private Context mContext;
}
