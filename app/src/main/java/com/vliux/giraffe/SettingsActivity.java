package com.vliux.giraffe;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import com.vliux.giraffe.util.AppSettings;

/**
 * Created by vliux on 17/7/7.
 */

public class SettingsActivity extends PreferenceActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mAppSettings = new AppSettings(this);
        loadPrefKeys();
        
        mSysAppsPref = (SwitchPreference)findPreference(pref_sync_sys_apps_k);
        mSysAppsPref.setOnPreferenceChangeListener(mOnPrefChangedListener);
        mWechatPref = (SwitchPreference)findPreference(pref_wechat_only_k);
        mWechatPref.setOnPreferenceChangeListener(mOnPrefChangedListener);
    }
    
    private void loadPrefKeys(){
        pref_sync_sys_apps_k = getString(R.string.pref_sync_sys_apps_k);
        pref_wechat_only_k = getString(R.string.pref_wechat_only_k);
    }
    
    private Preference.OnPreferenceChangeListener mOnPrefChangedListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(final Preference preference, final Object newValue) {
            final String key = preference.getKey();
            if(pref_sync_sys_apps_k.equals(key)){
                onSysAppsChanged(newValue);
            } else if(pref_wechat_only_k.equals(key)){
                onWechatOnlyChanged(newValue);
            }
            return false;
        }
    };
    
    private void onWechatOnlyChanged(final Object newValue){
        final boolean checked = (boolean)newValue;
        mAppSettings.setBoolean(pref_wechat_only_k, checked);
        mWechatPref.setChecked(checked);
        mSysAppsPref.setEnabled(!checked);
    }
    
    private void onSysAppsChanged(final Object newValue){
        final boolean checked = (boolean)newValue;
        mAppSettings.setBoolean(pref_sync_sys_apps_k, checked);
        mSysAppsPref.setChecked(checked);
    }
    
    private AppSettings mAppSettings;
    
    private String pref_sync_sys_apps_k;
    private SwitchPreference mSysAppsPref;
    
    private String pref_wechat_only_k;
    private SwitchPreference mWechatPref;
    
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
