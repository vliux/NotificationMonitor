package com.example.vliux.notificationlistener.guide;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;
import com.example.vliux.notificationlistener.MainActivity;
import com.example.vliux.notificationlistener.R;
import com.example.vliux.notificationlistener.util.AppSettings;
import com.example.vliux.notificationlistener.util.NotifPermission;

import static com.example.vliux.notificationlistener.Constants.Settings.*;
import static com.example.vliux.notificationlistener.guide.UserGuideManager.*;

/**
 * Created by vliux on 2017/7/8.
 */

public class UserGuideFragment extends Fragment implements NavigationBar.NavigationBarListener {
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mAppSettings = new AppSettings(getActivity());
        final View rootView = inflater.inflate(R.layout.frag_guide, container, false);
        mWizardLayout = (SetupWizardLayout)rootView;
        mNavBar = mWizardLayout.getNavigationBar();
        mNavBar.setNavigationBarListener(this);
        mNavBar.getBackButton().setVisibility(View.INVISIBLE);
        mNavBar.getNextButton().setEnabled(false);
        
        mSwWechat = (Switch)rootView.findViewById(R.id.sw_wechat);
        initWechatSwitch();
        
        mBtnBind = (Button)rootView.findViewById(R.id.btn_bind);
        initBindButton();
        return rootView;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mAppSettings.close();
    }
    
    private void initWechatSwitch(){
        mSwWechat.setOnCheckedChangeListener(mOnWechatSwitchChanged);
        updateWechatSwitch();
    }
    
    private void initBindButton(){
        mBtnBind.setOnClickListener(mOnBindBtnClicked);
    }
    
    private void updateWechatSwitch(){
        final boolean checked = mAppSettings.get(KEY_WECHAT_ONLY, DEFAULT_WECHAT_ONLY);
        mSwWechat.setChecked(checked);
        mSwWechat.setText(getString(checked ? R.string.switch_wechat_on : R.string.switch_wechat_off));
    }
    
    @Override
    public void onNavigateBack() {
    }
    
    @Override
    public void onNavigateNext() {
        final Activity activity = getActivity();
        setUserGuideShown(activity);
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
    
    private final CompoundButton.OnCheckedChangeListener mOnWechatSwitchChanged = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mAppSettings.setBoolean(KEY_WECHAT_ONLY, isChecked);
            updateWechatSwitch();
        }
    };
    
    private final View.OnClickListener mOnBindBtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NotifPermission.request(getActivity());
            mNavBar.getNextButton().setEnabled(true);
        }
    };
    
    private SetupWizardLayout mWizardLayout;
    private NavigationBar mNavBar;
    private Switch mSwWechat;
    private Button mBtnBind;
    private AppSettings mAppSettings;
}
