package com.example.vliux.notificationlistener.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vliux.notificationlistener.NotificationRecord;

import static com.example.vliux.notificationlistener.NotificationRecord.*;

/**
 * Created by vliux on 17/7/6.
 */

public class RecordDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notif_records";
    private static final int VERSION = 1;
    private static final String[] COLOLUMS = new String[]{COL_PKG, COL_TITLE, COL_TEXT, COL_TIME};
    
    public RecordDbHelper(final Context context){
        super(context, DB_NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DB_TABLE + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_PKG + " TEXT,"
                + COL_TITLE + " TEXT,"
                + COL_TEXT + " TEXT,"
                + COL_TIME + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    
    public Cursor queryRecords(){
        return getReadableDatabase().query(DB_TABLE, COLOLUMS,
                null, null,
                COL_PKG,
                null,
                COL_TIME + " desc");
    }
    
    public long insertRecord(final ContentValues cv){
        return getWritableDatabase().insert(DB_TABLE, null, cv);
    }
}
