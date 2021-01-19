package com.example.petmarket2020.HelperClass;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener2 extends RecyclerView.OnScrollListener {
    private final GridLayoutManager gridLayoutManager;

    public PaginationScrollListener2(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int totalItem = gridLayoutManager.getItemCount();
        int visible = gridLayoutManager.getChildCount();
        int firstVisible = gridLayoutManager.findFirstVisibleItemPosition();
        if (isLoading() || isLastPage()) return;
        if (firstVisible >= 0 && (visible + firstVisible) >= totalItem) {
            loadMoreItem();
        }
    }

    public abstract void loadMoreItem();

    public abstract boolean isLoading();

    public abstract boolean isLastPage();
}
