package com.example.vliux.notificationlistener.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Created by vliux on 17/7/6.
 */

public class Apps {
    public static class AppDesc {
        public Drawable icon;
        public String label;
    }
    public static AppDesc of(final Context context, final String pkg){
        try {
            final PackageManager packageManager = context.getPackageManager();
            final PackageInfo pkgInfo = packageManager.getPackageInfo(pkg, PackageManager.GET_META_DATA);
            final Drawable icon = pkgInfo.applicationInfo.loadIcon(packageManager);
            final String label = (String)pkgInfo.applicationInfo.loadLabel(packageManager);
            final AppDesc appDesc = new AppDesc();
            appDesc.icon = icon;
            appDesc.label = null != label ? label : pkg;
            return appDesc;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
