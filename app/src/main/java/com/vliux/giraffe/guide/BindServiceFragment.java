package com.vliux.giraffe.guide;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vliux.giraffe.MainActivity;
import com.vliux.giraffe.R;
import com.vliux.giraffe.listener.TraceServiceNotifier;
import com.vliux.giraffe.util.NotifPermission;

import static com.vliux.giraffe.guide.UserGuideManager.setUserGuideShown;

/**
 * Created by vliux on 2017/7/8.
 */

public class BindServiceFragment extends AbstractGuideFragment {
    
    @Override
    protected int getLayoutRes() {
        return R.layout.guide_bind;
    }
    
    @Override
    protected Fragment getNextFragment() {
        return null;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mBtnBind = (Button)rootView.findViewById(R.id.btn_bind);
        mBtnBind.setOnClickListener(mOnBindBtnClicked);
        mNavBar.getNextButton().setText(R.string.finish);
        return rootView;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private final View.OnClickListener mOnBindBtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TraceServiceNotifier.registerServiceBound(getActivity(), new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    TraceServiceNotifier.unregister(context, this);
                    final Activity activity = getActivity();
                    if(null != activity) activity.finish();
                    MainActivity.start(context, false);
                }
            });
            NotifPermission.request(getActivity());
            setUserGuideShown(getActivity());
        }
    };
    
    private Button mBtnBind;
}
