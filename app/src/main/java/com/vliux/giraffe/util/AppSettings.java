package com.vliux.giraffe.util;

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
    }
    
    public void set(@NonNull final String key, final boolean b){
        mPref.put(key, b);
    }
    
    public boolean getBoolean(@NonNull final String key, final boolean defaultValue){
        return mPref.getBoolean(key, defaultValue);
    }
    
    public void set(@NonNull final String key, @Nullable final Set<String> stringSet){
        mPref.put(key, setToStr(stringSet));
    }
    
    @NonNull
    public Set<String> getStringSet(@NonNull final String key){
        return strToSet(mPref.getString(key, null));
    }
    
    public void addToSet(@NonNull final String key, @NonNull final String value){
        final Set<String> set = new HashSet<>(getStringSet(key));
        set.add(value);
        set(key, set);
    }
    
    public void removeFromSet(@NonNull final String key, @NonNull final String value){
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
}
