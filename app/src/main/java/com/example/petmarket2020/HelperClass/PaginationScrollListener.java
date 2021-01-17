package com.example.petmarket2020.HelperClass;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private final LinearLayoutManager linearLayoutManager;

    public PaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int totalItem = linearLayoutManager.getItemCount();
        int visible = linearLayoutManager.getChildCount();
        int firstVisible = linearLayoutManager.findFirstVisibleItemPosition();
        if (isLoading() || isLastPage()) return;
        if (firstVisible >= 0 && (visible + firstVisible) >= totalItem) {
            loadMoreItem();
        }
    }

    public abstract void loadMoreItem();

    public abstract boolean isLoading();

    public abstract boolean isLastPage();
}
