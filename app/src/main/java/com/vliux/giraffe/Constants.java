package com.vliux.giraffe;

/**
 * Created by vliux on 2017/7/8.
 * @author vliux
 */

public class Constants {
    public static final String TAG = "giraffe";
    public static final int INTENT_CACHE_SIZE = 30;

    public static class Item {
        public static final String DOT = "•";
    }
    
    public static class Settings {
        public static final boolean NOTIF_SRV_BOUND = false;
        public static final boolean DEFAULT_WECHAT_ONLY = true;
        public static final boolean DEFAULT_SYS_APPS = false;
    }
    
    public static class Pkgs {
        public static final String WECHAT = "com.tencent.mm";
    }
    
    public static class IntentExtras {
        public static final String PKG = "pkg";
    }
}
