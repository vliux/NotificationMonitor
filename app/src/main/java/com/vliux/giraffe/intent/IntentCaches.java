package com.vliux.giraffe.intent;

import android.app.PendingIntent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.vliux.giraffe.Constants;

/**
 * Created by vliux on 2017/7/22.
 */

class IntentCaches {
    private final LruCache<Uri, PendingIntent> mIntents = new LruCache<>(Constants.INTENT_CACHE_SIZE);

    public void add(@NonNull final Uri uri, final PendingIntent pendingIntent){
        mIntents.put(uri, pendingIntent);
    }

    @Nullable
    public PendingIntent get(@NonNull final Uri uri){
        return mIntents.get(uri);
    }
}
