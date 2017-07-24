package com.vliux.giraffe.guide;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.vliux.giraffe.R;

import static com.vliux.giraffe.Constants.Settings.DEFAULT_WECHAT_ONLY;

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
        mSwWechat = (Switch)rootView.findViewById(R.id.sw_wechat);
        mSwWechat.setOnCheckedChangeListener(mOnWechatSwitchChanged);
        updateWechatSwitch();
        return rootView;
    }
    
    private void updateWechatSwitch(){
        final boolean checked = getSettings().getBoolean(getString(R.string.pref_wechat_only_k), DEFAULT_WECHAT_ONLY);
        mSwWechat.setChecked(checked);
        mSwWechat.setText(getString(checked ? R.string.switch_wechat_on : R.string.switch_wechat_off));
    }
    
    private final CompoundButton.OnCheckedChangeListener mOnWechatSwitchChanged = (buttonView, isChecked) -> {
        getSettings().set(getString(R.string.pref_wechat_only_k), isChecked);
        updateWechatSwitch();
    };
    
    private Switch mSwWechat;
}
