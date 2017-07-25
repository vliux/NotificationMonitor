package com.vliux.giraffe.util;

import android.support.annotation.NonNull;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by vliux on 2017/7/25.
 */

public class TextViews {
    
    public static void setLeftDrawable(@NonNull final TextView tv, @NonNull final Apps.AppDesc appDesc){
        if(null != appDesc.icon){
            tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    appDesc.icon.setBounds(0, 0,
                            tv.getMeasuredHeight(),
                            tv.getMeasuredHeight());
                    tv.setCompoundDrawables(appDesc.icon, null, null, null);
                }
            });
            tv.requestLayout();
        }
    }
}
