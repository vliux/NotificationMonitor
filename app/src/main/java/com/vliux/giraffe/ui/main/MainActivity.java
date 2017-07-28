package com.vliux.giraffe.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import android.view.ViewStub;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vliux.giraffe.BuildConfig;
import com.vliux.giraffe.R;
import com.vliux.giraffe.data.NotificationRecord;
import com.vliux.giraffe.data.NotificationRecordStorage;
import com.vliux.giraffe.guide.UserGuideManager;
import com.vliux.giraffe.listener.TraceServiceNotifier;
import com.vliux.giraffe.listener.TracerEnsurer;
import com.vliux.giraffe.ui.pkgtgt.AppSelectActivity;
import com.vliux.giraffe.util.Analytics;
import com.vliux.giraffe.util.Apps;
import com.vliux.giraffe.util.DateUtil;
import com.vliux.giraffe.util.TextViews;
import com.vliux.giraffe.view.AboutView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.vliux.giraffe.util.NotifPermission.*;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FloatingActionMenu mFab;
    private FloatingActionButton mFabBind, mFabAdd;
    private Adapter mAdapter;
    private NotificationRecordStorage mStorage;
    private Toolbar mToolbar;
    private List<NotificationRecord> mRecords = new ArrayList<>();
    private boolean mShowAsMerged = true;
    private View mEmptyView; //if not null, empty view is showing
    
    public static void start(final Context context, final boolean newTask){
        final Intent intent = new Intent(context, MainActivity.class);
        if(newTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Analytics.logAppOpen();
        UserGuideManager.showUserGuideIfNeeded(this, false);
        
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mStorage = new NotificationRecordStorage(MainActivity.this);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        
        mFab = (FloatingActionMenu) findViewById(R.id.fab);
        mFabBind = (FloatingActionButton)findViewById(R.id.fab_bind);
        mFabBind.setOnClickListener(v -> {showBindingDialog(); mFab.close(false);});
        mFabAdd = (FloatingActionButton)findViewById(R.id.fab_add);
        mFabAdd.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, AppSelectActivity.class)); mFab.close(false);});
        
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter();
        updateList();
        mRecyclerView.setAdapter(mAdapter);
        TraceServiceNotifier.registerNotificationUpdated(this, mNotifChangedReceiver);
        
        TracerEnsurer.ensureServiceRunning(this);
        TracerEnsurer.scheduleEnsureRunning(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TraceServiceNotifier.unregister(this, mNotifChangedReceiver);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        updateList();
        TracerEnsurer.checkPermissionAsync(this, findViewById(R.id.parent));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(BuildConfig.DEBUG){
            menu.findItem(R.id.action_dbg).setEnabled(true);
        }
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_merge_split:
                Analytics.logMenuEvent("action_merge_split");
                return toggleShowAsMerged(item);
            case R.id.action_guide:
                UserGuideManager.showUserGuideIfNeeded(this, true);
                Analytics.logMenuEvent("action_guide");
                return true;
            case R.id.action_about:
                Analytics.logMenuEvent("action_about");
                return showAbout();
            case R.id.action_dbg:
                return debugOp();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void updateList(){
        if(mShowAsMerged){
            mAdapter.setRecords(mStorage.getMerged(mRecords), mRecords);
        }else {
            final List<NotificationRecord> records = mStorage.getRaw();
            mAdapter.setRecords(records.size(), records);
        }
        // set empty view if needed
        if (mAdapter.getItemCount() <= 0) {
            if(null == mEmptyView) {
                final ViewStub stub = (ViewStub) findViewById(R.id.stub_empty);
                mEmptyView = stub.inflate();
            }
        } else if (null != mEmptyView) {
            mEmptyView.setVisibility(View.GONE);
        }
    }
    
    private boolean toggleShowAsMerged(final MenuItem menuItem){
        if(mShowAsMerged) mRecords.clear();
        mShowAsMerged = !mShowAsMerged;
        updateList();
        menuItem.setIcon(mShowAsMerged ? R.drawable.ic_call_split_black_24dp : R.drawable.ic_call_merge_black_24dp);
        menuItem.setTitle(mShowAsMerged ? R.string.split_notif : R.string.merge_notif);
        return true;
    }
    
    private boolean showBindingDialog(){
        if(null != intent(this)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(R.string.guide_explain_2)
                    .setCancelable(true)
                    .setPositiveButton(R.string.goto_bind, (dialog, which) -> request(MainActivity.this));
            builder.show();
        }else showUnsupported(this);
        return true;
    }

    private boolean showAbout(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                //.setTitle(R.string.about)
                .setCancelable(true)
                .setView(new AboutView(this));
        builder.create().show();
        return true;
    }
    
    private boolean debugOp(){
        startActivity(new Intent(this, AppSelectActivity.class));
        return true;
    }
    
    private BroadcastReceiver mNotifChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateList();
        }
    };
    
    private void setSubTitle(final int recordNum){
        mToolbar.setSubtitle(String.format(getString(R.string.main_subtitle), recordNum));
    }
    
    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<NotificationRecord> mRecords;
    
        public Adapter() {
            super();
        }
        
        public void setRecords(final int rawCount, final List<NotificationRecord> records){
            mRecords = records;
            setSubTitle(rawCount);
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
            holder.mTvTitle.setText(record.getTitle());
            holder.mTvContent.setText(record.getText());
            DateUtil.setDate(MainActivity.this, holder.mTvTime, record.getTime());
            loadIconAsync(record.getPkg(), holder.mTvApp);
    
            final Intent intent = Apps.ofLauncher(MainActivity.this, record.getPkg());
            if(null != intent) {
                holder.mContainer.setOnClickListener(v -> {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(intent);
                    Analytics.logClickListItem(record.getPkg());
                });
            }
        }
    
        @Override
        public int getItemCount() {
            return null != mRecords ? mRecords.size() : 0;
        }
        
        private void loadIconAsync(final String pkg, final TextView tv){
            //new AppInfoAsyncTask(MainActivity.this, tv, pkg).execute();
            Observable.create((ObservableOnSubscribe<Apps.AppDesc>) e -> {
                final Apps.AppDesc desc = Apps.ofDesc(MainActivity.this, pkg);
                if(null != desc) {
                    e.onNext(desc);
                    e.onComplete();
                }else e.onError(null);
            }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Apps.AppDesc>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }
    
                @Override
                public void onNext(@NonNull final Apps.AppDesc appDesc) {
                    tv.setText(appDesc.label);
                    TextViews.setLeftDrawable(tv, appDesc);
                }
    
                @Override
                public void onError(@NonNull Throwable e) {
                    tv.setText("");
                    tv.setCompoundDrawables(null, null, null, null);
                }
    
                @Override
                public void onComplete() {
                }
            });
        }
    }
    
    private class ViewHolder extends RecyclerView.ViewHolder {
        private View mContainer;
        private TextView mTvTitle;
        private TextView mTvContent;
        private TextView mTvApp;
        private TextView mTvTime;
        
        public ViewHolder(View itemView) {
            super(itemView);
            mContainer = itemView;
            mTvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            mTvContent = (TextView)itemView.findViewById(R.id.tv_content);
            mTvApp = (TextView)itemView.findViewById(R.id.tv_app);
            mTvTime = (TextView)itemView.findViewById(R.id.tv_time);
        }
    }
    
    /*private static class AppInfoAsyncTask extends AsyncTask<Void, Integer, Apps.AppDesc>{
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
            final Activity activity = mActivity.getBoolean();
            if(null != activity) {
                return Apps.ofDesc(activity, mPkg);
            }
            return null;
        }
    
        @Override
        protected void onPostExecute(final Apps.AppDesc appDesc) {
            final TextView tv = mTv.getBoolean();
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
    }*/
}
