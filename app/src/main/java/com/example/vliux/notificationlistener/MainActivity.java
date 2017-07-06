package com.example.vliux.notificationlistener;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vliux.notificationlistener.data.NotificationRecord;
import com.example.vliux.notificationlistener.data.NotificationRecordStorage;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private Adapter mAdapter;
    private NotificationRecordStorage mStorage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorage = new NotificationRecordStorage(MainActivity.this);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensureListening();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter(mStorage.get());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        }, 1000L);
        NotificationChangedNotifier.register(this, mNotifChangedReceiver);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationChangedNotifier.unregister(this, mNotifChangedReceiver);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.setRecords(mStorage.get());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    private void ensureListening(){
        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    private BroadcastReceiver mNotifChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.setRecords(mStorage.get());
        }
    };
    
    public void onPostNotificationClick(final View view){
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentTitle("TITLE=" + System.currentTimeMillis())
                .setContentText("CONTENT=" + System.currentTimeMillis())
                .build();
        notificationManager.notify(100, notification);
    }
    
    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<NotificationRecord> mRecords;
    
        public Adapter(final List<NotificationRecord> records) {
            super();
            mRecords = records;
        }
        
        public void setRecords(final List<NotificationRecord> records){
            mRecords = records;
            notifyDataSetChanged();
        }
    
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("vliux", "onCreateViewHolder()");
            final View cardView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_notif, parent, false);
            return new ViewHolder(cardView);
        }
    
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d("vliux", "onBindViewHolder()");
            final NotificationRecord record = mRecords.get(position);
            holder.mTvTitle.setText(record.title);
            holder.mTvContent.setText(record.text);
            holder.mTvApp.setText(record.pkg);
            holder.mTvTime.setText(new Date(record.time).toString());
        }
    
        @Override
        public int getItemCount() {
            return null != mRecords ? mRecords.size() : 0;
        }
    }
    
    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;
        private TextView mTvContent;
        private TextView mTvApp;
        private TextView mTvTime;
        
        public ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            mTvContent = (TextView)itemView.findViewById(R.id.tv_content);
            mTvApp = (TextView)itemView.findViewById(R.id.tv_app);
            mTvTime = (TextView)itemView.findViewById(R.id.tv_time);
        }
    }
}
