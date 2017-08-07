package com.vliux.giraffe.intent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.vliux.giraffe.ui.main.MainActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by vliux on 2017/8/7.
 */
@RunWith(AndroidJUnit4.class)
public class IntentCachesTest {
    private IntentCaches ic;
    
    @Before
    public void resetCache(){
        ic = IntentCaches.get();
        ic.removeAll();
    }
    
    @Test
    public void testAdd(){
        ic.add("a.b.c.d", Uri.parse("content://a.b.c.d/1"), get());
        Assert.assertEquals(ic.size(), 1);
        ic.add("a.b.c.d", Uri.parse("content://a.b.c.d/2"), get());
        Assert.assertEquals(ic.size(), 2);
        ic.add("9.8.8.1.1", Uri.parse("content://a.b.c.d/3"), get());
        ic.add("9.8.8.1.1", Uri.parse("content://a.b.c.d/4"), get());
        ic.add("g.fewa.f", Uri.parse("content://g.fewa.f/1"), get());
        Assert.assertEquals(ic.size(), 5);
        ic.add("g.fewa.f", Uri.parse("content://g.fewa.f/1"), get());
        Assert.assertEquals(ic.size(), 5);
    }
    
    @Test
    public void testRemove(){
        ic.add("a.b.c.d", Uri.parse("content://a.b.c.d/1"), get());
        ic.add("a.b.c.d", Uri.parse("content://a.b.c.d/2"), get());
        ic.add("a.b.c.d", Uri.parse("content://a.b.c.d/3"), get());
        ic.add("a.b.c.d", Uri.parse("content://a.b.c.d/4"), get());
        ic.add("1.2.3", Uri.parse("content://1.2.3/9"), get());
        Assert.assertEquals(ic.size(), 5);
        ic.remove("a.b.c.d");
        Assert.assertEquals(ic.size(), 1);
        ic.removeAll();
        Assert.assertEquals(ic.size(), 0);
    }
    
    private PendingIntent get(){
        final Context context = InstrumentationRegistry.getTargetContext();
        return PendingIntent.getActivity(context,
                0,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
