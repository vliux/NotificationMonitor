package com.example.vliux.notificationlistener;

/**
 * Created by vliux on 17/7/5.
 */

public class Event {
    public String pkg;
    public String group;
    public String key;
    public String text;
    public long time;
    
    public Event(String pkg, String group, String key, String text, long time) {
        this.pkg = pkg;
        this.group = group;
        this.key = key;
        this.text = text;
        this.time = time;
    }
}
