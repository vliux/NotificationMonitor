package com.vliux.giraffe.ui.pkgtgt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vliux.giraffe.R;
import com.vliux.giraffe.util.Apps;
import com.vliux.giraffe.util.TextViews;

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

/**
 * Created by vliux on 2017/7/24.
 */

public class AppSelectActivity extends AppCompatActivity {
    private TargetPkgs mTargetPkgs;
    private SectionedRecyclerViewAdapter mAdapter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.select_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTargetPkgs = new TargetPkgs(this);
        mAdapter = new SectionedRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);
        updateList();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTargetPkgs.close();
    }
    
    private void updateList(){
        Observable.create((ObservableOnSubscribe<Map<TargetPkgs.Type, List<Apps.AppDesc>>>) e -> {
            final Map<TargetPkgs.Type, List<Apps.AppDesc>> data = mTargetPkgs.get();
            if(null != data) {
                e.onNext(data); e.onComplete();
            }else e.onError(null);
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<TargetPkgs.Type, List<Apps.AppDesc>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }
                
                    @Override
                    public void onNext(@NonNull Map<TargetPkgs.Type, List<Apps.AppDesc>> typeListMap) {
                        onDataArrived(typeListMap);
                    }
                
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                
                    @Override
                    public void onComplete() {
                    }
                });
    }
    
    private void onDataArrived(final Map<TargetPkgs.Type, List<Apps.AppDesc>> data){
        if(data.containsKey(SELECTED))
            mAdapter.addSection(new Section("Selected", data.get(SELECTED)));
        if(data.containsKey(UNSELECTED))
            mAdapter.addSection(new Section("Unselected", data.get(UNSELECTED)));
        mAdapter.notifyDataSetChanged();
    }
    
    class Section extends StatelessSection {
        private final List<Apps.AppDesc> mAppDescs;
        private final String mTitle;
        
        public Section(final String title, final List<Apps.AppDesc> appDescs) {
            super(R.layout.header_app_select, R.layout.item_app_select);
            mTitle = title;
            mAppDescs = appDescs;
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
            final SelectViewHolder selectViewHolder = (SelectViewHolder)holder ;
            final Apps.AppDesc appDesc = mAppDescs.get(position);
            selectViewHolder.mAppTv.setText(appDesc.label);
            TextViews.setLeftDrawable(selectViewHolder.mAppTv, appDesc);
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
