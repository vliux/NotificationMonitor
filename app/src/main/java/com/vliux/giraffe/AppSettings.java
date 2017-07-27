package com.vliux.giraffe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

import net.grandcentrix.tray.AppPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vliux on 2017/7/8.
 * @author vliux
 */

public class AppSettings {
    
    public AppSettings(@NonNull final Context context) {
        mPref = new AppPreferences(context);
        mKeyBoundedEver = context.getString(R.string.pref_notif_srv_bound);
        mKeyTargetPkgs = context.getString(R.string.pref_target_pkgs);
    }
   
    public boolean boundedEver(){
        return getBoolean(mKeyBoundedEver, Constants.Settings.DEFAULT_NOTIF_SRV_BOUNDED);
    }
    
    public void setBoundedEver(){
        set(mKeyBoundedEver, true);
    }
    
    @NonNull
    public Set<String> getTargetPkgs(){
        return getStringSet(mKeyTargetPkgs);
    }
    
    public void setTargetPkgs(@NonNull final Set<String> pkgs){
        setStringSet(mKeyTargetPkgs, pkgs);
    }
    
    public void setTargetAllPkgs(){
        final Set<String> set = new HashSet<>(1);
        set.add(Constants.Settings.TARGET_ALL_PKGS);
        setStringSet(mKeyTargetPkgs, set);
    }
    
    public static boolean targetAllPkgs(final Set<String> pkgs){
        if(null == pkgs || pkgs.size() != 1) return false;
        else for(final String p : pkgs){
            return Constants.Settings.TARGET_ALL_PKGS.equals(p);
        }
        return false;
    }
    
    private void set(@NonNull final String key, final boolean b){
        mPref.put(key, b);
    }
    
    private boolean getBoolean(@NonNull final String key, final boolean defaultValue){
        return mPref.getBoolean(key, defaultValue);
    }
    
    private void set(@NonNull final String key, @Nullable final Set<String> stringSet){
        mPref.put(key, setToStr(stringSet));
    }
    
    private Set<String> getStringSet(@NonNull final String key){
        return strToSet(mPref.getString(key, null));
    }
    
    private void setStringSet(@NonNull final String key, @NonNull Set<String> stringSet){
        mPref.put(key, setToStr(stringSet));
    }
    
    private void addToSet(@NonNull final String key, @NonNull final String value){
        final Set<String> set = new HashSet<>(getStringSet(key));
        set.add(value);
        set(key, set);
    }
    
    private void removeFromSet(@NonNull final String key, @NonNull final String value){
        final Set<String> set = new HashSet<>(getStringSet(key));
        if(set.contains(value)){
            set.remove(value);
            set(key, set);
        }
    }
    
    private static String setToStr(final Set<String> stringSet){
        if(null == stringSet || stringSet.size() <= 0) return "";
        else return Joiner.on(":").join(stringSet);
    }
    
    private static Set<String> strToSet(final String str){
        if(null == str || str.length() <= 0) return new HashSet<>(0);
        else //noinspection Guava
            return FluentIterable.from(Splitter.on(":").split(str)).toSet();
    }
    
    private final AppPreferences mPref;
    private final String mKeyBoundedEver, mKeyTargetPkgs;
}
