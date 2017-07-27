package com.vliux.giraffe.ui.pkgtgt;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.vliux.giraffe.R;
import com.vliux.giraffe.AppSettings;
import com.vliux.giraffe.util.Apps;
import com.vliux.giraffe.util.TextViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.vliux.giraffe.ui.pkgtgt.TargetPkgs.Type.*;
import static com.vliux.giraffe.util.Apps.*;

/**
 * Created by vliux on 2017/7/24.
 */

public class AppSelectActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private AppSettings mAppSettings;
    private TargetPkgs mTargetPkgs;
    private SectionedRecyclerViewAdapter mAdapter;
    private List<AppDesc> mDataSelected, mDataUnselected;
    private FloatingActionButton mFab;
    private Switch mSwAllApps;
    private boolean mOnBind = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_select_title);
        getSupportActionBar().setSubtitle(R.string.app_select_subtitle);
        
        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setOnClickListener(v -> {
            onSubmitChanges();
            finish();
        });
        mAppSettings = new AppSettings(this);
        mTargetPkgs = new TargetPkgs(this, mAppSettings);
        
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.select_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SectionedRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);
        updateList();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_select, menu);
        final boolean b = super.onCreateOptionsMenu(menu);
        initSwitch(menu);
        return b;
    }
    
    private void onSubmitChanges(){
        if(mDataSelected.size() > 0 && mDataUnselected.size() <= 0) mAppSettings.setTargetAllPkgs();
        else if(mDataSelected.size() <= 0 && mDataUnselected.size() > 0) mAppSettings.setTargetNoPkg();
        else {
            final Set<String> pkgs = FluentIterable.from(mDataSelected).transform(new Function<AppDesc, String>() {
                @javax.annotation.Nullable
                @Override
                public String apply(@javax.annotation.Nullable AppDesc input) {
                    return input.pkg;
                }
            }).toSet();
            mAppSettings.setTargetPkgs(pkgs);
        }
    }
    
    private void initSwitch(final Menu menu){
        final MenuItem menuItem = menu.findItem(R.id.menu_all_apps);
        if(null != menuItem){
            mSwAllApps = (Switch)menuItem.getActionView().findViewById(R.id.toolbar_sw);
            final Set<String> pkgs = mAppSettings.getTargetPkgs();
            mSwAllApps.setChecked(AppSettings.targetAllPkgs(pkgs));
            mSwAllApps.setOnCheckedChangeListener(mAllAppsSwitchListener);
        }
    }
    
    private void updateList(){
        final ProgressDialog dlg = showProgressDlg();
        Observable.create((ObservableOnSubscribe<Map<TargetPkgs.Type, List<AppDesc>>>) e -> {
            final Map<TargetPkgs.Type, List<AppDesc>> data = mTargetPkgs.get();
            if(null != data) {
                e.onNext(data); e.onComplete();
            }else e.onError(null);
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<TargetPkgs.Type, List<AppDesc>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }
                
                    @Override
                    public void onNext(@NonNull Map<TargetPkgs.Type, List<AppDesc>> typeListMap) {
                        onDataArrived(typeListMap);
                    }
                
                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(!isDestroyed() && !isFinishing()) dlg.dismiss();
                    }
                
                    @Override
                    public void onComplete() {
                        if(!isDestroyed() && !isFinishing()) dlg.dismiss();
                    }
                });
    }
    
    private ProgressDialog showProgressDlg(){
        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setIndeterminate(true);
        dlg.setMessage(getString(R.string.load_installed_apps));
        dlg.show();
        return dlg;
    }
    
    private void onDataArrived(final Map<TargetPkgs.Type, List<Apps.AppDesc>> data){
        if(data.containsKey(SELECTED)) {
            mDataSelected = new ArrayList<>(data.get(SELECTED));
            mAdapter.addSection(new Section(mDataSelected, SELECTED));
        }
        if(data.containsKey(UNSELECTED)) {
            mDataUnselected = new ArrayList<>(data.get(UNSELECTED));
            mAdapter.addSection(new Section(mDataUnselected, UNSELECTED));
        }
        refreshSwitch();
        mAdapter.notifyDataSetChanged();
    }
    
    private void onSwapItem(final AppDesc appDesc, final TargetPkgs.Type targetType){
        switch (targetType){
            case SELECTED:
                mDataUnselected.remove(appDesc);
                mDataSelected.add(appDesc);
                break;
            case UNSELECTED:
                mDataSelected.remove(appDesc);
                mDataUnselected.add(appDesc);
                break;
            default:
                return;
        }
        refreshSwitch();
        mAdapter.notifyDataSetChanged();
    }
    
    private void onSwapItems(final TargetPkgs.Type targetType){
        switch (targetType){
            case SELECTED:
                mDataSelected.addAll(mDataUnselected);
                mDataUnselected.clear();
                break;
            case UNSELECTED:
                mDataUnselected.addAll(mDataSelected);
                mDataSelected.clear();
                break;
            default:
                return;
        }
        mAdapter.notifyDataSetChanged();
    }
    
    private void refreshSwitch(){
        if(mDataSelected.size() <= 0) {
            setCheckedWithoutListener(mSwAllApps, false, mAllAppsSwitchListener);
        }else if(mDataUnselected.size() <= 0){
            setCheckedWithoutListener(mSwAllApps, true, mAllAppsSwitchListener);
        }else {
            setCheckedWithoutListener(mSwAllApps, false, mAllAppsSwitchListener);
        }
    }
    
    class Section extends StatelessSection {
        private final List<Apps.AppDesc> mAppDescs;
        private final TargetPkgs.Type mType;
        
        public Section(final List<Apps.AppDesc> appDescs, final TargetPkgs.Type type) {
            super(type == SELECTED ? R.layout.header_app_selected : R.layout.header_app_unselected,
                    R.layout.item_app_select);
            mAppDescs = appDescs;
            mType = type;
        }
    
        @Override
        public int getContentItemsTotal() {
            return null != mAppDescs ? mAppDescs.size() : 0;
        }
    
        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new SelectViewHolder(view);
        }
        
        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            mOnBind = true;
            final SelectViewHolder selectViewHolder = (SelectViewHolder)holder ;
            final AppDesc appDesc = mAppDescs.get(position);
            selectViewHolder.mAppTv.setText(appDesc.label);
            TextViews.setLeftDrawable(selectViewHolder.mAppTv, appDesc);
            
            selectViewHolder.mCb.setTag(R.id.appdesc, appDesc);
            setCheckedWithoutListener(selectViewHolder.mCb, mType == TargetPkgs.Type.SELECTED, mItemCheckedListener);
            selectViewHolder.mCb.setOnCheckedChangeListener(mItemCheckedListener);
            mOnBind = false;
        }
    }
    
    private class SelectViewHolder extends RecyclerView.ViewHolder {
        private TextView mAppTv;
        private CheckBox mCb;
        
        public SelectViewHolder(View itemView) {
            super(itemView);
            mAppTv = (TextView)itemView.findViewById(R.id.select_tv);
            mCb = (CheckBox)itemView.findViewById(R.id.select_cb);
        }
    }
    
    private final CompoundButton.OnCheckedChangeListener mAllAppsSwitchListener = (buttonView, isChecked) -> {
        if(isChecked) {
            onSwapItems(TargetPkgs.Type.SELECTED);
        }else {
            onSwapItems(TargetPkgs.Type.UNSELECTED);
        }
    };
    
    private final CompoundButton.OnCheckedChangeListener mItemCheckedListener = (buttonView, isChecked) -> {
        if(mOnBind) return;
        final AppDesc appDesc = (AppDesc)buttonView.getTag(R.id.appdesc);
        if(null != appDesc) {
            if (isChecked) {
                onSwapItem(appDesc, TargetPkgs.Type.SELECTED);
            } else {
                onSwapItem(appDesc, TargetPkgs.Type.UNSELECTED);
            }
        }
    };
    
    private static void setCheckedWithoutListener(final CompoundButton cb, final boolean checked, final CompoundButton.OnCheckedChangeListener listener){
        cb.setOnCheckedChangeListener(null);
        cb.setChecked(checked);
        cb.setOnCheckedChangeListener(listener);
    }
}
