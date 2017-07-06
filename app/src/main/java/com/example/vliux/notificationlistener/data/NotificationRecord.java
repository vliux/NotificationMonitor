package com.example.vliux.notificationlistener.data;

/**
 * Created by vliux on 17/7/5.
 */

public class NotificationRecord {
    public static final String DB_TABLE = "records";
    public static final String COL_ID = "_ID";
    public static final String COL_TIME = "time";
    public static final String COL_TITLE = "title";
    public static final String COL_TEXT = "text";
    public static final String COL_PKG = "pkg";
    
    public String pkg;
    public String title;
    public String text;
    public long time;
    
    public NotificationRecord(String pkg, String title, String text, long time) {
        this.pkg = pkg;
        this.title = title;
        this.text = text;
        this.time = time;
    }
}
