package com.vliux.giraffe;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.vliux.giraffe.listener.NotificationTracerService;
import com.vliux.giraffe.util.Analytics;
import com.vliux.giraffe.util.NotifPermission;
import com.vliux.giraffe.util.Services;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vliux on 2017/7/21.
 * @author vliux
 */

class TracerEnsurer {
    /**
     * Ensure that the NotificationTracerListener is bound to system notification service,
     * if it has been assigned the permission.
     */
    static void tryEnsure(final Context context){
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
    }
    
    static void checkAsync(final Activity activity, final View snackbarParent){
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            e.onNext(Services.isServiceRunning(activity, NotificationTracerService.class));
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
    
}
