package com.vliux.giraffe.util;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.vliux.giraffe.R;

/**
 * Created by vliux on 2017/7/8.
 */

public class NotifPermission {
    public static void request(final Activity context){
        context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        Toast.makeText(context,
                context.getString(R.string.goto_bind_msg_2, context.getString(R.string.service_name)),
                Toast.LENGTH_LONG).show();
    }
}
