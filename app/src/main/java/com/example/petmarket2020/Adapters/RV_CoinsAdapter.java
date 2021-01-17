package com.example.petmarket2020.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Models.CoinsModel;
import com.example.petmarket2020.R;

import java.util.List;

public class RV_CoinsAdapter extends RecyclerView.Adapter<RV_CoinsAdapter.CoinsHolder> {
    private final List<CoinsModel> coinsModelList;

    public RV_CoinsAdapter(List<CoinsModel> coinsModelList, RV_CoinsAdapter.IItemOnClick iItemOnClick) {
        this.iItemOnClick = iItemOnClick;
        this.coinsModelList = coinsModelList;
    }

    private final RV_CoinsAdapter.IItemOnClick iItemOnClick;

    public interface IItemOnClick {
        void itemClick(long value);
    }

    @NonNull
    @Override
    public CoinsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coins, parent, false);
        return new CoinsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinsHolder holder, int position) {
        CoinsModel coinsModel = coinsModelList.get(position);
        String value = Utils.formatCurrencyVN(coinsModel.getValue());
        String title = "Nạp " + value.substring(0, value.length() - 2);
        String title2 = "Giá " + value;
        holder.tvCoinTitle.setText(title);
        holder.tvCoin.setText(title2);
        holder.itemView.setOnClickListener(v -> iItemOnClick.itemClick(coinsModel.getValue()));
    }

    @Override
    public int getItemCount() {
        return coinsModelList.size();
    }

    public static class CoinsHolder extends RecyclerView.ViewHolder {
        private final TextView tvCoinTitle;
        private final TextView tvCoin;

        public CoinsHolder(@NonNull View itemView) {
            super(itemView);
            tvCoinTitle = itemView.findViewById(R.id.tvCoinTitle);
            tvCoin = itemView.findViewById(R.id.tvCoin);
        }
    }
}
