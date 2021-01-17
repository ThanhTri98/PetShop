package com.example.petmarket2020.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.Models.PetTypeModel;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RV_PetCategoryAdapter extends RecyclerView.Adapter<RV_PetCategoryAdapter.MyViewHolder> {
    private final List<PetTypeModel> listItems;
    private final StorageReference storageReference;

    public RV_PetCategoryAdapter(List<PetTypeModel> listItems) {
        this.listItems = listItems;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pet_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PetTypeModel petTypeModel = listItems.get(position);
        String name = petTypeModel.getName();
        String image = petTypeModel.getImage();
        Glide.with(holder.ivPet).load(storageReference.child(image))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivPet);
        holder.tvPet.setText(name);
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
