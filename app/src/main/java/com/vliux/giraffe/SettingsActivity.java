package com.vliux.giraffe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by vliux on 17/7/7.
 */

public class SettingsActivity extends Activity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    
    /*private void setAppsListPreference(){
        final List<CharSequence> entries = new ArrayList<>();
        final List<CharSequence> values = new ArrayList<>();
        mPkgEntriesMap.clear();
        final PackageManager packageManager = getPackageManager();
        final List<ApplicationInfo> appInfos =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        int i = 0;
        for(final ApplicationInfo appInfo : appInfos){
            final CharSequence label = appInfo.loadLabel(packageManager);
            if(null != label){
                entries.add(label);
                values.add(appInfo.packageName);
                mPkgEntriesMap.put(appInfo.packageName, label);
            }
        }
        mPkgListPref.setEntries(entries.toArray(new CharSequence[entries.size()]));
        mPkgListPref.setEntryValues(values.toArray(new CharSequence[values.size()]));
    }*/
    
}
