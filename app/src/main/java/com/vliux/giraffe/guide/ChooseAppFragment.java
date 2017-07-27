package com.vliux.giraffe.guide;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vliux.giraffe.R;
import com.vliux.giraffe.ui.pkgtgt.AppSelectActivity;

/**
 * Created by vliux on 2017/7/18.
 */

public class ChooseAppFragment extends AbstractGuideFragment {
    @Override
    protected int getLayoutRes() {
        return R.layout.guide_app;
    }
    
    @Override
    protected Fragment getNextFragment() {
        return new BindServiceFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        final Button btnSet = (Button) rootView.findViewById(R.id.btn_set_apps);
        btnSet.setOnClickListener(v -> {
            final Activity activity = getActivity();
            activity.startActivity(new Intent(activity, AppSelectActivity.class));
        });
        return rootView;
    }
}
