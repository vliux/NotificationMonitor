package com.example.vliux.notificationlistener.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    public static AppDesc of(@NonNull final Context context, @NonNull final String pkg){
        try {
            final PackageManager packageManager = context.getPackageManager();
            final PackageInfo pkgInfo = packageManager.getPackageInfo(pkg, PackageManager.GET_META_DATA);
            final Drawable icon = pkgInfo.applicationInfo.loadIcon(packageManager);
            final String label = (String)pkgInfo.applicationInfo.loadLabel(packageManager);
            final AppDesc appDesc = new AppDesc(icon, null != label ? label : pkg);
            return appDesc;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
