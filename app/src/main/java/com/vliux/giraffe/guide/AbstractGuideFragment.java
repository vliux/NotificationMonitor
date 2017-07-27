package com.vliux.giraffe.guide;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.setupwizardlib.SetupWizardLayout;
import com.android.setupwizardlib.view.NavigationBar;
import com.vliux.giraffe.R;
import com.vliux.giraffe.AppSettings;

import static com.vliux.giraffe.guide.UserGuideManager.setUserGuideShown;

/**
 * Created by vliux on 2017/7/8.
 */

public abstract class AbstractGuideFragment extends Fragment implements NavigationBar.NavigationBarListener {
    protected abstract int getLayoutRes();
    protected abstract Fragment getNextFragment();
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(getLayoutRes(), container, false);
        mWizardLayout = (SetupWizardLayout)rootView;
        mNavBar = mWizardLayout.getNavigationBar();
        mNavBar.setNavigationBarListener(this);
        return rootView;
    }
    
    @Override
    public void onNavigateBack(){
        getFragmentManager().popBackStack();
    }
    
    @Override
    public void onNavigateNext(){
        final Fragment next = getNextFragment();
        if(null != next) {
            getFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getClass().getSimpleName())
                    .replace(R.id.container, next, next.getClass().getSimpleName())
                    .commit();
        }else{
            final Activity activity = getActivity();
            setUserGuideShown(activity);
            activity.finish();
        }
    }
    
    protected AppSettings getSettings(){
        return ((UserGuideActivity)getActivity()).getSettings();
    }
    
    protected SetupWizardLayout mWizardLayout;
    protected NavigationBar mNavBar;
}
