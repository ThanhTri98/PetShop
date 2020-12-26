package com.example.petmarket2020.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.R;

import java.util.List;

public class RV_LocationAdapter extends RecyclerView.Adapter<RV_LocationAdapter.MyViewHolder> {
    private final Context context;
    private final List<String> listItems;

    public RV_LocationAdapter(Context context, List<String> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_location, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvLocation.setText(listItems.get(position));
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}
