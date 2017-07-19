package com.vliux.giraffe.data;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vliux.giraffe.provider.NotificationRecordProvider;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Notification;

/**
 * Created by vliux on 17/7/5.
 */

public class NotificationRecordStorage implements Closeable {
    public NotificationRecordStorage(final Context context) {
        mContext = context.getApplicationContext();
        mClient = mContext.getContentResolver().acquireContentProviderClient(NotificationRecordProvider.AUTHORITY);
    }

    @Override
    public void close(){
        if(null != mClient){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mClient.close();
            }else mClient.release();
        }
    }
    
    /**
     * @param records the List to receive records.
     * @return num of raw records (without merging).
     */
    public int getMerged(@NonNull final List<NotificationRecord> records){
        records.clear();
        int n = 0;
        final Cursor cursor = mContext.getContentResolver().query(NotificationRecordProvider.RECORD_CONTENT_URI, null, null, null, null);
        if(null != cursor){
            NotificationRecord lastRecord = null;
            n = cursor.getCount();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                final String pkg = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_PKG));
                final String title = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TITLE));
                final String text = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TEXT));
                final long time = cursor.getLong(cursor.getColumnIndex(NotificationRecord.COL_TIME));
                if(null != lastRecord
                        && lastRecord.getPkg().equals(pkg)
                        && lastRecord.getTitle().equals(title)) {
                    lastRecord.insertTextAtHead(text);
                }else {
                    lastRecord = new NotificationRecord(pkg, title, text, time);
                    records.add(lastRecord);
                }
            }
        }
        return n;
    }
    
    @Nullable
    public List<NotificationRecord> get(final String pkg){
        final Cursor cursor = mContext.getContentResolver()
                .query(Uri.withAppendedPath(NotificationRecordProvider.RECORD_CONTENT_URI, pkg),
                null, null, null, null);
        if(null != cursor){
            final List<NotificationRecord> records = new ArrayList<>(cursor.getCount());
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                //final String pkg = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_PKG));
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
            cv.put(NotificationRecord.COL_PKG, record.getPkg());
            cv.put(NotificationRecord.COL_TITLE, record.getTitle());
            cv.put(NotificationRecord.COL_TEXT, record.getText());
            cv.put(NotificationRecord.COL_TIME, record.getTime());
            if(null != mClient) {
                try {
                    mClient.insert(NotificationRecordProvider.RECORD_CONTENT_URI, cv);
                } catch (RemoteException e) {
                    Log.e(TAG, "unable to save through ContentProviderClient", e);
                    return null;
                }
            } else return mContext.getContentResolver().insert(NotificationRecordProvider.RECORD_CONTENT_URI, cv);
        }
        return null;
    }
    
    private Context mContext;
    private volatile ContentProviderClient mClient;
    private static final String TAG = "NotificationStorage";
}
