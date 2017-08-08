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

import com.vliux.giraffe.Constants;
import com.vliux.giraffe.data.provider.NotificationRecordProvider;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import static com.vliux.giraffe.data.provider.NotificationRecordProvider.RECORD_CONTENT_URI;

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
    
    public List<NotificationRecord> getRaw(){
        List<NotificationRecord> records = null;
        final Cursor cursor = mContext.getContentResolver().query(RECORD_CONTENT_URI, null, null, null, null);
        if(null != cursor){
            records = new ArrayList<>();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                final int id = cursor.getInt(cursor.getColumnIndex(NotificationRecord.COL_ID));
                final String pkg = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_PKG));
                final String title = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TITLE));
                final String text = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TEXT));
                final long time = cursor.getLong(cursor.getColumnIndex(NotificationRecord.COL_TIME));
                records.add(NotificationRecord.fromStorage(id, pkg, title, text, time));
            }
        }
        return records;
    }
    
    /**
     * @param records the List to receive records.
     * @return num of raw records (without merging).
     */
    public int getMerged(@NonNull final List<NotificationRecord> records){
        records.clear();
        int n = 0;
        final Cursor cursor = mContext.getContentResolver().query(RECORD_CONTENT_URI, null, null, null, null);
        if(null != cursor){
            NotificationRecord lastRecord = null;
            int mergedNum = 0;
            n = cursor.getCount();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                final int id = cursor.getInt(cursor.getColumnIndex(NotificationRecord.COL_ID));
                final String pkg = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_PKG));
                final String title = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TITLE));
                final String text = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TEXT));
                final long time = cursor.getLong(cursor.getColumnIndex(NotificationRecord.COL_TIME));
                if(null != lastRecord
                        && mergedNum < Constants.ITEM_EXTRA_SUBITEMS
                        && lastRecord.getPkg().equals(pkg)
                        && lastRecord.getTitle().equals(title)) {
                    lastRecord.mergeWith(NotificationRecord.fromStorage(id, pkg, title, text, time));
                    mergedNum++;
                }else {
                    lastRecord = NotificationRecord.fromStorage(id, pkg, title, text, time);
                    records.add(lastRecord);
                }
            }
        }
        return n;
    }
    
    @Nullable
    public List<NotificationRecord> get(@NonNull final String pkg){
        final Cursor cursor = mContext.getContentResolver()
                .query(NotificationRecordProvider.fromPkg(pkg),
                null, null, null, null);
        if(null != cursor){
            final List<NotificationRecord> records = new ArrayList<>(cursor.getCount());
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                final int id = cursor.getInt(cursor.getColumnIndex(NotificationRecord.COL_ID));
                //final String pkg = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_PKG));
                final String title = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TITLE));
                final String text = cursor.getString(cursor.getColumnIndex(NotificationRecord.COL_TEXT));
                final long time = cursor.getLong(cursor.getColumnIndex(NotificationRecord.COL_TIME));
                records.add(NotificationRecord.fromStorage(id, pkg, title, text, time));
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
                    return mClient.insert(RECORD_CONTENT_URI, cv);
                } catch (RemoteException e) {
                    Log.e(TAG, "unable to save through ContentProviderClient", e);
                    return null;
                }
            } else return mContext.getContentResolver().insert(RECORD_CONTENT_URI, cv);
        }
        return null;
    }

    public int delete(@NonNull final String pkg){
        if(null != mClient){
            try {
                return mClient.delete(NotificationRecordProvider.fromPkg(pkg), null, null);
            } catch (final RemoteException e) {
                Log.e(TAG, "unable to delete records of pkg: " + pkg, e);
                return 0;
            }
        }else return mContext.getContentResolver().delete(NotificationRecordProvider.fromPkg(pkg), null, null);
    }
    
    public static Uri get(final int id){
        return Uri.withAppendedPath(RECORD_CONTENT_URI, String.valueOf(id));
    }

    private Context mContext;
    private volatile ContentProviderClient mClient;
    private static final String TAG = "NotificationStorage";
}
