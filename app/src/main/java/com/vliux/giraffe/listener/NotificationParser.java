package com.vliux.giraffe.listener;

import android.os.Bundle;

/**
 * Created by vliux on 17/7/5.
 */

class NotificationParser {
    private static final String KEY_TITLE = "android.title";
    private static final String KEY_TEXT = "android.text";
        
    static String getTitle(final Bundle bundle){
        return bundle.getString(KEY_TITLE, null);
    }
    
    static String geText(final Bundle bundle){
        return bundle.getString(KEY_TEXT, null);
    }
}
