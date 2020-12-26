package com.example.petmarket2020.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.items.PetCategoryItem;
import com.example.petmarket2020.R;

import java.util.List;

public class RV_PetCategoryAdapter extends RecyclerView.Adapter<RV_PetCategoryAdapter.MyViewHolder> {
    private Context context;
    private List<PetCategoryItem> listItems;

    public RV_PetCategoryAdapter(Context context, List<PetCategoryItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_home_pet_category, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int imgage = listItems.get(position).getImage();
        String title = listItems.get(position).getTitle();
        holder.ivPet.setImageResource(imgage);
        holder.tvPet.setText(title);
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPet;
        TextView tvPet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPet = itemView.findViewById(R.id.ivPet);
            tvPet = itemView.findViewById(R.id.tvPet);
        }
    }
}
