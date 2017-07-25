package com.vliux.giraffe.ui.pkgtgt;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.vliux.giraffe.R;
import com.vliux.giraffe.util.AppSettings;
import com.vliux.giraffe.util.Apps;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

/**
 * Created by vliux on 2017/7/25.
 */
@RunWith(AndroidJUnit4.class)
public class TargetPkgsTest {
    private AppSettings mAppSettings;
    
    @Before
    public void resetEnv(){
        final Context context = InstrumentationRegistry.getTargetContext();
        mAppSettings = new AppSettings(context);
        mAppSettings.set(context.getString(R.string.pref_target_pkgs), null);
    }
    
    @Test
    public void testEmptySelection(){
        final Context context = InstrumentationRegistry.getTargetContext();
        final TargetPkgs targetPkgs = new TargetPkgs(context, new AppSettings(context));
        final Map<TargetPkgs.Type, List<Apps.AppDesc>> result = targetPkgs.get();
        commonAssert(result);
        Assert.assertEquals(result.get(TargetPkgs.Type.SELECTED).size(), 0);
        Assert.assertTrue(result.get(TargetPkgs.Type.UNSELECTED).size() > 0);
    }
    
    private void commonAssert(final Map<TargetPkgs.Type, List<Apps.AppDesc>> result){
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 2);
        Assert.assertTrue(result.containsKey(TargetPkgs.Type.SELECTED));
        Assert.assertTrue(result.containsKey(TargetPkgs.Type.UNSELECTED));
    }
}
