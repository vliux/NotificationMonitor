package com.vliux.giraffe.intent;

import android.app.PendingIntent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

import static com.vliux.giraffe.Constants.IntentCache.*;

/**
 * Created by vliux on 2017/7/22.
 * @author vliux
 */

public class IntentCaches {
    
    public static IntentCaches get(){
        return Holder.sCache;
    }

    public void add(@NonNull String pkg, @NonNull final Uri uri, final PendingIntent pendingIntent){
        mIntents.put(uri, pendingIntent);
        mPkgsMap.put(pkg, uri);
    }

    @Nullable
    public PendingIntent getAndRemove(@NonNull final String pkg, @NonNull final Uri uri){
        final PendingIntent result = mIntents.remove(uri);
        mPkgsMap.remove(pkg, uri);
        return result;
    }
    
    public void remove(@NonNull final String pkg){
        final Collection<Uri> uris = mPkgsMap.get(pkg);
        if(uris.size() > 0) {
            for (final Uri uri : uris) {
                mIntents.remove(uri);
            }
            mPkgsMap.removeAll(pkg);
        }
    }
    
    public void removeAll(){
        mPkgsMap.clear();
        mIntents.evictAll();
    }
    
    int size(){
        return mIntents.size();
    }
    
    private final LruCache<Uri, PendingIntent> mIntents = new LruCache<>(PKG_NUM * SUB_ITEM_NUM);
    private final Multimap<String, Uri> mPkgsMap = HashMultimap.create(PKG_NUM, SUB_ITEM_NUM);
            
    private static class Holder {
        private static final IntentCaches sCache = new IntentCaches();
    }
}
