package com.example.girdimagesearch;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class EndlessScrollListener implements OnScrollListener {
	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 12;
	// The total number of items in the dataset after the last load
	private AtomicInteger loadingOffset = new AtomicInteger(0);
	

	public EndlessScrollListener() {
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	// This happens many times a second during a scroll, so be wary of the code you place here.
	// We are given a few useful parameters to help us work out if we need to load some more data,
	// but first we check if we are waiting for the previous load to finish.
	@Override
	public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount) {
		if ((loadingOffset.get() - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			onLoadMore(loadingOffset.get());
		}
	}

	// Defines the process for actually loading more data based on page
	public abstract void onLoadMore(int totalItemsCount);

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Don't take any action on changed
	}
	
	protected void increaseLoadingOffset(int offset){
		this.loadingOffset.addAndGet(offset);
	}
	
	public void resetOffset(){
		this.loadingOffset.set(0);
	}
}