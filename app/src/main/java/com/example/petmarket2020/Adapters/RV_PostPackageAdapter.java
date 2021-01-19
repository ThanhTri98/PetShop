package com.example.petmarket2020.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Models.PostPackageModel;
import com.example.petmarket2020.R;

import java.util.List;

public class RV_PostPackageAdapter extends RecyclerView.Adapter<RV_PostPackageAdapter.PostPackageHolder> {
    private final List<PostPackageModel> postPackageModelList;

    public RV_PostPackageAdapter(List<PostPackageModel> postPackageModelList, RV_PostPackageAdapter.IItemOnClick iItemOnClick) {
        this.iItemOnClick = iItemOnClick;
        this.postPackageModelList = postPackageModelList;
    }

    private final RV_PostPackageAdapter.IItemOnClick iItemOnClick;

    public interface IItemOnClick {
        void itemClick(PostPackageModel postPackageModel);
    }

    @NonNull
    @Override
    public PostPackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_package, parent, false);
        return new PostPackageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostPackageHolder holder, int position) {
        PostPackageModel postPackageModel = postPackageModelList.get(position);
//        String pkgId = postPackageModel.getPkgId();
        String title = postPackageModel.getTitle();
        String description = postPackageModel.getDescription();
        long price = postPackageModel.getPrice();
        holder.tvTitle.setText(title);
        holder.tvDesc.setText(description);
        holder.tvPrice.setText(Utils.formatCurrencyVN(price) + " / ngày");
        holder.itemView.setOnClickListener(v -> iItemOnClick.itemClick(postPackageModel));
    }

    @Override
    public int getItemCount() {
        return postPackageModelList.size();
    }

    public static class PostPackageHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvPrice;
        private final TextView tvDesc;

        public PostPackageHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}
