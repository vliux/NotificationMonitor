package com.example.vliux.notificationlistener.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by vliux on 17/7/6.
 */

public class NotificationRecordProvider extends ContentProvider {
    public static final String AUTHORITY = "com.vliux.notification.provider";
    public static final String RECORD_TABLE = "record";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri RECORD_CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, RECORD_TABLE);
    
    public static UriMatcher URI_MATCHER;
    private static final int MATCHER_RECORDS = 1;
    private static final int MATCHER_SINGLE_RECORD = 2;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, RECORD_TABLE, MATCHER_RECORDS);
        URI_MATCHER.addURI(AUTHORITY, RECORD_TABLE + "/*", MATCHER_SINGLE_RECORD);
    }
    
    private final String TYPE_RECORDS = "vnd.android.cursor.dir/records";
    private final String TYPE_SINGLE_RECORD = "vnd.android.cursor.dir/record";
    
    private RecordDbHelper mDbHelper;
    
    @Override
    public boolean onCreate() {
        mDbHelper = new RecordDbHelper(getContext());
        return true;
    }
    
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (getType(uri)){
            case TYPE_RECORDS:
                return mDbHelper.queryRecords();
            case TYPE_SINGLE_RECORD:
                final String pkg = toPkg(uri);
                if(null != pkg) return mDbHelper.queryRecord(pkg);
                else return null;
            default:
                return null;
        }
    }
    
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case MATCHER_RECORDS:
                return TYPE_RECORDS;
            case MATCHER_SINGLE_RECORD:
                return TYPE_SINGLE_RECORD;
            default:
                return null;
        }
    }
    
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final long id = mDbHelper.insertRecord(values);
        if(id >= 0L){
            return Uri.withAppendedPath(RECORD_CONTENT_URI, String.valueOf(id));
        }else return null;
    }
    
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    
    @Nullable
    private static String toPkg(final Uri uri){
        final List<String> pathes = uri.getPathSegments();
        if(null != pathes && pathes.size() > 0) return pathes.get(pathes.size() - 1);
        else return null;
    }
    
    private static final String TAG = "NotifRecords";
}
