package com.vliux.giraffe.listener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

import com.vliux.giraffe.R;
import com.vliux.giraffe.util.NotifPermission;

import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.*;

/**
 * Created by vliux on 2017/7/21.
 * @author vliux
 */

public class TracerEnsurer {
    
    /**
     * Try to ensure that the notification listener service is running, if permission has been granted.
     * @param context
     */
    public static void ensureServiceRunning(final Context context){
        if(isPermissionGranted(context)) {
            final PackageManager pm = context.getPackageManager();
            final ComponentName cn = new ComponentName(context, NotificationTracerService.class);
            pm.setComponentEnabledSetting(cn, COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
            pm.setComponentEnabledSetting(cn, COMPONENT_ENABLED_STATE_ENABLED, DONT_KILL_APP);
        }
    }
    
    /*static void tryEnsure(final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final AppSettings appSettings = new AppSettings(context);
            if (appSettings.boundedEver())
                try {
                    NotificationListenerService.requestRebind(
                            new ComponentName(context.getPackageName(), NotificationTracerService.class.getName()));
                }catch (final Throwable e){
                    Log.w("TracerEnsurer", "failed to request rebind", e);
                    Analytics.logError(e);
                }
        }
    }*/
    
    public static void checkPermissionAsync(final Activity activity, final View snackbarParent){
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            e.onNext(isPermissionGranted(activity));
            //e.onNext(Services.isServiceRunning(activity, NotificationTracerService.class));
            e.onComplete();
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
    
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if(!aBoolean) {
                    Snackbar.make(snackbarParent, R.string.guide_explain_2, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.goto_bind, v -> NotifPermission.request(activity))
                            .show();
                }
            }
    
            @Override
            public void onError(@NonNull Throwable e) {
            }
    
            @Override
            public void onComplete() {
            }
        });
    }
    
    /**
     * Check whether the notification listening permission has been granted.
     */
    private static boolean isPermissionGranted(final Context context){
        final Set<String> pkgs = NotificationManagerCompat.getEnabledListenerPackages(context);
        if(null != pkgs && pkgs.contains(context.getPackageName())) return true;
        else return false;
    }
    
}
