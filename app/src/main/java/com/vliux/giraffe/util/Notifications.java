package com.vliux.giraffe.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by vliux on 2017/7/24.
 */

public class Notifications {
    public static void collapseNotificationBar(final Context context){
        final Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
}
