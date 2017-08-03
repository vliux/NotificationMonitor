package com.vliux.giraffe.ui.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by vliux on 2017/8/3.
 * Loading more data when RecyclerView is scrolling to end.
 */

class IncrementalLoader {
    private final MainActivity.Adapter mAdapter;
    private final RecyclerView mRecyclerView;
    private final LinearLayoutManager mLayoutMgr;
    private int mLastVisibleItem = RecyclerView.NO_POSITION;
    
    IncrementalLoader(final RecyclerView rv, final MainActivity.Adapter mAdapter, final LinearLayoutManager lm) {
        this.mRecyclerView = rv;
        this.mAdapter = mAdapter;
        this.mLayoutMgr = lm;
    }
    
    void setup(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.d(TAG, "onScrollStateChanged() " + newState);
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount()){
                    Log.d(TAG, "will do loading...");
                }
            }
    
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "onScrolled() " + dx + "," + dy);
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutMgr.findLastVisibleItemPosition();
            }
        });
    }
    
    private static final String TAG = "IncrementalLogder";
    
}
