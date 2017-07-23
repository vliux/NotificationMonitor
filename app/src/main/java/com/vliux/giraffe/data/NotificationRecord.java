package com.vliux.giraffe.data;

import java.util.ArrayList;
import java.util.List;

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

    private int id;
    private final String pkg;
    private final String title;
    private String text;
    private final long time;
    private List<NotificationRecord> mMerged;

    private NotificationRecord(final int id, final String pkg, final String title, final String text, final long time) {
        this.id = id;
        this.pkg = pkg;
        this.title = title;
        this.text = text;
        this.time = time;
    }

    public static NotificationRecord fromListener(final String pkg, final String title, final String text, final long time){
        return new NotificationRecord(-1, pkg, title, text, time);
    }

    public static NotificationRecord fromStorage(final int id, final String pkg, final String title, final String text, final long time){
        return new NotificationRecord(id, pkg, title, text, time);
    }
    
    public void mergeWith(final NotificationRecord record){
        if(null == mMerged) mMerged = new ArrayList<>();
        mMerged.add(record);
    }
    
    public String getPkg() {
        return pkg;
    }
    
    public String getText() {
        return text;
    }
    
    public String getTitle() {
        return title;
    }
    
    public long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }
}
