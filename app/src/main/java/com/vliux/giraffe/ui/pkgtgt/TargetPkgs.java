package com.vliux.giraffe.ui.pkgtgt;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.ArrayMap;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.vliux.giraffe.AppSettings;
import com.vliux.giraffe.util.Apps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import static android.content.pm.PackageManager.*;
import static com.vliux.giraffe.util.Apps.*;

/**
 * Created by vliux on 2017/7/24.
 * @author vliux
 */

class TargetPkgs {
    enum Type {
        SELECTED, UNSELECTED
    }
    
    TargetPkgs(final Context context, final AppSettings appSettings) {
        mContext = context;
        mAppSettings = appSettings;
    }
    
    Map<Type, List<AppDesc>> get(){
        //TODO
        final List<PackageInfo> pkgInfos =
                mContext.getPackageManager().getInstalledPackages(GET_META_DATA | MATCH_UNINSTALLED_PACKAGES);
        final Set<String> targetPkgs = mAppSettings.getTargetPkgs();
        final Map<Type, List<AppDesc>> result = new ArrayMap<>(2);
        result.put(Type.SELECTED, ofSelected(pkgInfos, targetPkgs));
        result.put(Type.UNSELECTED, ofUnselected(pkgInfos, targetPkgs));
        return result;
    }
    
    private List<AppDesc> ofSelected(final List<PackageInfo> pkgInfos, final Set<String> targetPkgs){
        final boolean targetAll = AppSettings.targetAllPkgs(targetPkgs);
        return FluentIterable.from(pkgInfos)
                        .filter(input -> targetAll || filterSelected(input, targetPkgs))
                        .transform(new Function<PackageInfo, AppDesc>() {
                            @Nullable
                            @Override
                            public AppDesc apply(@Nullable PackageInfo input) {
                                return Apps.ofDesc(mContext, input.packageName);
                            }
                        })
                        .filter(input -> null != input)
                        .toSortedList(mComparator);
    }
    
    private List<AppDesc> ofUnselected(final List<PackageInfo> pkgInfos, final Set<String> targetPkgs){
        final boolean targetAll = AppSettings.targetAllPkgs(targetPkgs);
        return FluentIterable.from(pkgInfos)
                .filter(input -> !targetAll && filterUnselected(input, targetPkgs))
                .transform(new Function<PackageInfo, AppDesc>() {
                    @Nullable
                    @Override
                    public AppDesc apply(@Nullable PackageInfo input) {
                        return Apps.ofDesc(mContext, input.packageName);
                    }
                })
                .filter(input -> null != input)
                .toSortedList(mComparator);
    }
    
    private boolean filterSelected(final PackageInfo pkgInfo, final Set<String> targetPkgs){
        return null != pkgInfo.applicationInfo &&
                targetPkgs.contains(pkgInfo.packageName);
    }
    
    private boolean filterUnselected(final PackageInfo pkgInfo, final Set<String> targetPkgs){
        return null != pkgInfo.applicationInfo &&
                !targetPkgs.contains(pkgInfo.packageName);
    }
    
    private Comparator<AppDesc> mComparator = (o1, o2) -> {
        final int s1 = o1.sysApp ? 1 : 0;
        final int s2 = o2.sysApp ? 1 : 0;
        final int delta = s1 - s2;
        return 0 != delta ? delta : o1.label.compareTo(o2.label);
    };
    
    private Context mContext;
    private AppSettings mAppSettings;
}
