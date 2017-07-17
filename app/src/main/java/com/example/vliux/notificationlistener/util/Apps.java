package com.example.vliux.notificationlistener.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by vliux on 17/7/6.
 */

public class Apps {
    public static class AppDesc {
        public final Drawable icon;
        public final String label;
    
        public AppDesc(Drawable icon, String label) {
            this.icon = icon;
            this.label = label;
        }
    }
    
    @Nullable
    public static AppDesc ofDesc(@NonNull final Context context, @NonNull final String pkg){
        try {
            final PackageManager packageManager = context.getPackageManager();
            final PackageInfo pkgInfo = packageManager.getPackageInfo(pkg, PackageManager.GET_META_DATA);
            if(null != pkgInfo.applicationInfo) {
                final Drawable icon = pkgInfo.applicationInfo.loadIcon(packageManager);
                final String label = (String) pkgInfo.applicationInfo.loadLabel(packageManager);
                final AppDesc appDesc = new AppDesc(icon, null != label ? label : pkg);
                return appDesc;
            }else return null;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundExp " + pkg, e);
            return null;
        }
    }
    
    @Nullable
    public static Integer ofUid(@NonNull final Context context, @NonNull final String pkg){
        final PackageManager packageManager = context.getPackageManager();
        try {
            final PackageInfo pkgInfo = packageManager.getPackageInfo(pkg, PackageManager.GET_META_DATA);
            if(null != pkgInfo.applicationInfo) return pkgInfo.applicationInfo.uid;
            else return null;
        } catch (final PackageManager.NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundExp " + pkg, e);
            return null;
        }
    }
    
    private static final String TAG = "Giraffe";
}
