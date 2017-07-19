package com.vliux.giraffe.guide;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vliux.giraffe.R;
import com.vliux.giraffe.util.AppSettings;

/**
 * Created by vliux on 2017/7/8.
 */

public class UserGuideActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mAppSettings = new AppSettings(this);
        final Fragment fragment = new WelcomeFragment();
        getFragmentManager().beginTransaction()
                .addToBackStack(BindServiceFragment.class.getSimpleName())
                .replace(R.id.container, fragment, BindServiceFragment.class.getSimpleName())
                .commit();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppSettings.close();
    }
    
    public AppSettings getSettings(){
        return mAppSettings;
    }
    
    private AppSettings mAppSettings;
    
    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 1) super.onBackPressed();
    }
}
