package com.vliux.giraffe.data;

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
    
    private final String pkg;
    private final String title;
    private String text;
    private StringBuilder mTextSb;
    private final long time;
    
    public NotificationRecord(String pkg, String title, String text, long time) {
        this.pkg = pkg;
        this.title = title;
        this.text = text;
        this.time = time;
    }
    
    public void insertTextAtHead(final String text){
        if(null == mTextSb) mTextSb = new StringBuilder();
        mTextSb.insert(0, '\n').insert(0, text);
    }
    
    public String getPkg() {
        return pkg;
    }
    
    public String getText() {
        if(null != mTextSb) {
            if(null != text) text = mTextSb.insert(0, '\n').insert(0, text).toString();
            else text = mTextSb.toString();
            mTextSb = null;
        }
        return text;
    }
    
    public String getTitle() {
        return title;
    }
    
    public long getTime() {
        return time;
    }
}
