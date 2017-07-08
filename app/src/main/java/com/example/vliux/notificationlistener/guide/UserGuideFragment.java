package com.example.vliux.notificationlistener.guide;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;
import com.example.vliux.notificationlistener.R;

/**
 * Created by vliux on 2017/7/8.
 */

public class UserGuideFragment extends Fragment implements NavigationBar.NavigationBarListener {
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.guide, container, false);
        mWizardLayout = (SetupWizardLayout)rootView;
        mNavBar = mWizardLayout.getNavigationBar();
        mNavBar.setNavigationBarListener(this);
        mNavBar.getBackButton().setVisibility(View.INVISIBLE);
        return rootView;
    }
    
    @Override
    public void onNavigateBack() {
        
    }
    
    @Override
    public void onNavigateNext() {
        UserGuideManager.setUserGuideShown(getActivity());
        getActivity().finish();
    }
    
    private SetupWizardLayout mWizardLayout;
    private NavigationBar mNavBar;
}
