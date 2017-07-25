package com.vliux.giraffe.ui.pkgtgt;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.vliux.giraffe.R;
import com.vliux.giraffe.util.AppSettings;
import com.vliux.giraffe.util.Apps;
import com.vliux.giraffe.util.TextViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_select_title);
        getSupportActionBar().setSubtitle(R.string.app_select_subtitle);
        
        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setOnClickListener(v -> finish());
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
        mAdapter.notifyDataSetChanged();
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
    
        private boolean mOnBind = false;
        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            mOnBind = true;
            final SelectViewHolder selectViewHolder = (SelectViewHolder)holder ;
            final AppDesc appDesc = mAppDescs.get(position);
            selectViewHolder.mAppTv.setText(appDesc.label);
            TextViews.setLeftDrawable(selectViewHolder.mAppTv, appDesc);
            
            selectViewHolder.mCb.setChecked(mType == TargetPkgs.Type.SELECTED);
            selectViewHolder.mCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(mOnBind) return;
                switch (mType){
                    case SELECTED:
                        mAppSettings.removeFromSet(getString(R.string.pref_target_pkgs), appDesc.pkg);
                        onSwapItem(appDesc, UNSELECTED);
                        break;
                    case UNSELECTED:
                        mAppSettings.addToSet(getString(R.string.pref_target_pkgs), appDesc.pkg);
                        onSwapItem(appDesc, SELECTED);
                        break;
                    default:
                        return;
                }
            });
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
}
