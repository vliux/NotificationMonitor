package com.vliux.giraffe.guide;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vliux.giraffe.R;

/**
 * Created by vliux on 2017/7/8.
 */

public class WelcomeFragment extends AbstractGuideFragment {
    
    @Override
    protected int getLayoutRes() {
        return R.layout.guide_welcome;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mNavBar.getBackButton().setVisibility(View.INVISIBLE);
        return rootView;
    }
    
    @Override
    protected Fragment getNextFragment() {
        return new ChooseAppFragment();
    }
}
