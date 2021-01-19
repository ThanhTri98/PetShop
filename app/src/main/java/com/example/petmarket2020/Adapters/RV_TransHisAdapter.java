package com.example.petmarket2020.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Models.TransactionHistoryModel;
import com.example.petmarket2020.R;

import java.util.List;

public class RV_TransHisAdapter extends RecyclerView.Adapter<RV_TransHisAdapter.TransHisHolder> {
    private final List<TransactionHistoryModel> transactionHistoryModels;

    public RV_TransHisAdapter(List<TransactionHistoryModel> transactionHistoryModels) {
        this.transactionHistoryModels = transactionHistoryModels;
    }

    @NonNull
    @Override
    public TransHisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_history, parent, false);
        return new TransHisHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransHisHolder holder, int position) {
        TransactionHistoryModel transactionHistoryModel = transactionHistoryModels.get(position);
        String status = transactionHistoryModel.getStatus();
        holder.tvStatus.setText(status);
        holder.tvTime.setText(transactionHistoryModel.getTime());
        holder.tvPayments.setText(transactionHistoryModel.getPayments());
        holder.tvAmount.setText(Utils.formatCurrencyVN(transactionHistoryModel.getAmount()));
        if (!status.equals("Thành công")) {
            holder.tvStatus.setText("Thất bại");
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.tvStatus.getContext(), R.color.colorRedDark));
            holder.itemView.setBackgroundResource(R.color.colorRedDarkTransparent);
            holder.ivStatus.setImageResource(R.drawable.ic_cancel);
        }
    }

    @Override
    public int getItemCount() {
        return transactionHistoryModels.size();
    }

    public static class TransHisHolder extends RecyclerView.ViewHolder {
        private final TextView tvStatus;
        private final TextView tvPayments;
        private final TextView tvAmount;
        private final TextView tvTime;
        private final ImageView ivStatus;

        public TransHisHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPayments = itemView.findViewById(R.id.tvPayments);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivStatus = itemView.findViewById(R.id.ivStatus);
        }
    }
}
