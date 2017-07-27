package com.vliux.giraffe.util;

import android.content.Context;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by vliux on 2017/7/27.
 */

public class DateUtil {
    public static void setDate(final Context context, final TextView tv, final long time){
        final Date date = new Date(time);
        final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        final DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        tv.setText(dateFormat.format(date) + " " + timeFormat.format(date));
    }
}
