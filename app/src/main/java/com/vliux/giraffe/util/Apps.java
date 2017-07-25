package com.vliux.giraffe.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.pm.PackageManager.*;

/**
 * Created by vliux on 17/7/6.
 * @author vliux
 */

public class Apps {
    public static class AppDesc {
        public final String pkg;
        public final Drawable icon;
        public final String label;
    
        private AppDesc(final String pkg, final Drawable icon, final String label) {
            this.pkg = pkg;
            this.icon = icon;
            this.label = label;
        }
    }
    
    @NonNull
    public static AppDesc ofDesc(@NonNull final Context context, @NonNull final String pkg){
        try {
            final PackageManager packageManager = context.getPackageManager();
            final PackageInfo pkgInfo = packageManager.getPackageInfo(pkg, GET_META_DATA | MATCH_UNINSTALLED_PACKAGES);
            if(null != pkgInfo.applicationInfo) {
                final Drawable icon = pkgInfo.applicationInfo.loadIcon(packageManager);
                final String label = (String) pkgInfo.applicationInfo.loadLabel(packageManager);
                return new AppDesc(pkg, icon, null != label ? label : pkg);
            }else return null;
        } catch (final NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundExp " + pkg, e);
            return new AppDesc(pkg, null, pkg);
        }
    }
    
    @Nullable
    public static Integer ofUid(@NonNull final Context context, @NonNull final String pkg){
        final PackageManager packageManager = context.getPackageManager();
        try {
            final PackageInfo pkgInfo = packageManager.getPackageInfo(pkg, GET_META_DATA);
            if(null != pkgInfo.applicationInfo) return pkgInfo.applicationInfo.uid;
            else return null;
        } catch (final NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundExp " + pkg, e);
            return null;
        }
    }
    
    public static boolean systemApp(@NonNull final Context context, @NonNull final String pkg){
        final PackageManager packageManager = context.getPackageManager();
        try {
            final PackageInfo pkgInfo = packageManager.getPackageInfo(pkg, GET_META_DATA);
            if (null != pkgInfo.applicationInfo) return (pkgInfo.applicationInfo.flags & FLAG_MASK_SYS_APP) != 0;
        }catch (final NameNotFoundException e){
            Log.e(TAG, "NameNotFoundExp " + pkg, e);
        }
        return false;
    }

    @Nullable
    public static Intent ofLauncher(@NonNull final Context context, @NonNull final String pkg){
        final PackageManager packageManager = context.getPackageManager();
        return packageManager.getLaunchIntentForPackage(pkg);
    }
    
    private static final String TAG = "Giraffe";
    private static final int FLAG_MASK_SYS_APP = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
}
