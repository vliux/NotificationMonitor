package com.vliux.giraffe.ui.pkgtgt;

import android.content.Context;

import com.vliux.giraffe.util.AppSettings;

import java.util.List;
import java.util.Map;

import static com.vliux.giraffe.util.Apps.*;

/**
 * Created by vliux on 2017/7/24.
 */

class TargetPkgs {
    enum Type {
        SELECTED, UNSELECTED
    }
    
    TargetPkgs(final Context context) {
        mAppSettings = new AppSettings(context);
    }
    
    Map<Type, List<AppDesc>> get(){
        //TODO
        return null;
    }
    
    private AppSettings mAppSettings;
}
