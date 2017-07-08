package com.example.vliux.notificationlistener.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.vliux.notificationlistener.R;

/**
 * Created by vliux on 2017/7/8.
 */

public class UserGuideActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        final UserGuideFragment fragment = new UserGuideFragment();
        getFragmentManager().beginTransaction()
                .addToBackStack(UserGuideFragment.class.getSimpleName())
                .replace(R.id.container, fragment, UserGuideFragment.class.getSimpleName())
                .commit();
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
}
