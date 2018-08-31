package com.arctouch.codechallenge.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by railan on 30/08/18.
 */

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    protected InfiniteScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItems= layoutManager.getItemCount();
        int topItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + topItemPosition) >= totalItems && topItemPosition >= 0) {
                loadNextPage();
            }
        }

    }

    protected abstract void loadNextPage();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}
