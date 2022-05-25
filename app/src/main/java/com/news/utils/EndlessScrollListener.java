package com.news.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RefreshList refreshList;

    public EndlessScrollListener(RefreshList refreshList) {
        this.refreshList = refreshList;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        visibleItemCount = manager.getChildCount();
        totalItemCount = manager.getItemCount();
        pastVisiblesItems = manager.findFirstVisibleItemPosition();


        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                refreshList.onRefresh();
            }
        }
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public interface RefreshList {
        void onRefresh();
    }
}