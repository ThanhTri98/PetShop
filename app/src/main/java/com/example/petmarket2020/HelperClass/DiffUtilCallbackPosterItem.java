package com.example.petmarket2020.HelperClass;

import androidx.recyclerview.widget.DiffUtil;

import com.example.petmarket2020.Adapters.items.PosterItem;

import java.util.List;

public class DiffUtilCallbackPosterItem extends DiffUtil.Callback {
    private final List<PosterItem> oldList;
    private final List<PosterItem> newList;

    public DiffUtilCallbackPosterItem(List<PosterItem> oldList, List<PosterItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getPostId().equals(newList.get(newItemPosition).getPostId());

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final PosterItem oldItem = oldList.get(oldItemPosition);
        final PosterItem newItem = newList.get(newItemPosition);
        return oldItem.equals(newItem);
    }
}
