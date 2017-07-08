package com.example.vliux.notificationlistener;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.example.vliux.notificationlistener.util.Apps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vliux on 17/7/7.
 */

public class SettingsActivity extends PreferenceActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        final Preference pkgPref = findPreference(getString(R.string.pref_pkg_id));
        pkgPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });
        final Preference sysNotifPref = findPreference(getString(R.string.pref_sys_notif_id));
        sysNotifPref.setOnPreferenceChangeListener(mPrefChangeedListener);
    }
    
    private final Preference.OnPreferenceChangeListener mPrefChangeedListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return false;
        }
    };
    
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
