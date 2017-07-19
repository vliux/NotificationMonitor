package com.vliux.giraffe.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vliux.giraffe.BuildConfig;
import com.vliux.giraffe.R;

/**
 * Created by vliux on 2017/7/19.
 */

public class AboutView extends FrameLayout {
    public AboutView(@NonNull Context context) {
        super(context);
        init();
    }
    
    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_about, this, true);
        final TextView tvAppName = (TextView)findViewById(R.id.appver);
        tvAppName.setText(getContext().getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
        final TextView tvLink = (TextView)findViewById(R.id.link);
        tvLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
