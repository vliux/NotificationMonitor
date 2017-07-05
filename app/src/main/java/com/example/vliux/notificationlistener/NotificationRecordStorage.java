package com.example.vliux.notificationlistener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vliux on 17/7/5.
 */

public class NotificationRecordStorage {
    public NotificationRecordStorage() {
        
    }
    
    public List<NotificationRecord> get(){
        return mRecords;
    }
    
    public void add(final NotificationRecord record){
        mRecords.add(record);
    }
    
    private List<NotificationRecord> mRecords = new ArrayList<>();
}
