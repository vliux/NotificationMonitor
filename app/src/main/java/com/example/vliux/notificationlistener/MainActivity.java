package com.example.vliux.notificationlistener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vliux.notificationlistener.data.NotificationRecord;
import com.example.vliux.notificationlistener.data.NotificationRecordStorage;
import com.example.vliux.notificationlistener.guide.UserGuideManager;
import com.example.vliux.notificationlistener.util.Apps;
import com.example.vliux.notificationlistener.util.NotifPermission;

import java.lang.ref.WeakReference;
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
        if(UserGuideManager.showUserGuideIfNeeded(this))
            finish();
        
        setContentView(R.layout.activity_main);
        Toolbar toolBar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        mStorage = new NotificationRecordStorage(MainActivity.this);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_bind:
                showBindingDialog();
                return true;
            case R.id.action_setting:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void showBindingDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.goto_bind_msg)
                .setCancelable(true)
                .setPositiveButton(R.string.goto_bind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotifPermission.request(MainActivity.this);
                    }
                });
        builder.show();
    }
    
    private void showSettings(){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private BroadcastReceiver mNotifChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.setRecords(mStorage.get());
        }
    };
    
    /*public void onPostNotificationClick(final View view){
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentTitle("TITLE=" + System.currentTimeMillis())
                .setContentText("CONTENT=" + System.currentTimeMillis())
                .build();
        notificationManager.notify(100, notification);
    }*/
    
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
            holder.mTvTime.setText(new Date(record.time).toString());
            loadIconAsync(record.pkg, holder.mTvApp);
        }
    
        @Override
        public int getItemCount() {
            return null != mRecords ? mRecords.size() : 0;
        }
        
        private void loadIconAsync(final String pkg, final TextView tv){
            new AppInfoAsyncTask(MainActivity.this, tv, pkg).execute();
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
    
    private static class AppInfoAsyncTask extends AsyncTask<Void, Integer, Apps.AppDesc>{
        private WeakReference<Activity> mActivity;
        private WeakReference<TextView> mTv;
        private String mPkg;
        
        AppInfoAsyncTask(final Activity activity, final TextView tv, final String pkg){
            mActivity = new WeakReference<Activity>(activity);
            mTv = new WeakReference<TextView>(tv);
            mPkg = pkg;
        }
        
        @Override
        protected Apps.AppDesc doInBackground(Void... params) {
            final Activity activity = mActivity.get();
            if(null != activity) {
                return Apps.of(activity, mPkg);
            }
            return null;
        }
    
        @Override
        protected void onPostExecute(final Apps.AppDesc appDesc) {
            final TextView tv = mTv.get();
            if(null != tv){
                tv.setText(appDesc.label);
                if(null != appDesc.icon){
                    tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            appDesc.icon.setBounds(0, 0,
                                    tv.getMeasuredHeight(),
                                    tv.getMeasuredHeight());
                            tv.setCompoundDrawables(appDesc.icon, null, null, null);
                        }
                    });
                    tv.requestLayout();
                }
            }
        }
    }
}
