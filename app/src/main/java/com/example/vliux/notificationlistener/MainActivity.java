package com.example.vliux.notificationlistener;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.StringDef;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mTv = (TextView)findViewById(R.id.tv);
        mTv.setMovementMethod(new ScrollingMovementMethod());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    
    public void onClick(final View view){
        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    public void onPostNotificationClick(final View view){
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentTitle("TITLE=" + System.currentTimeMillis())
                .setContentText("CONTENT=" + System.currentTimeMillis())
                .build();
        notificationManager.notify(100, notification);
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotificationEvent(final Event event){
        mTv.append(Html.fromHtml("\n" + String.format("<b>p=%s,g=%s,</b>", event.pkg, event.group, event.key)));
        mTv.append(Html.fromHtml("\n" + String.format("<b>t=%s,k=%s</b>", new Date(event.time).toString(), event.key)));
        mTv.append(event.text);
    }
}
