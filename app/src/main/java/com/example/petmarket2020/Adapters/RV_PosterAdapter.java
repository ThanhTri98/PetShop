package com.example.petmarket2020.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RV_PosterAdapter extends RecyclerView.Adapter<RV_PosterAdapter.MyViewHolder> {
    private static final Locale localeVN = new Locale("vi", "VN");
    private static final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    private Context context;
    private List<PosterItem> listItems;

    public RV_PosterAdapter(Context context, List<PosterItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_home_poster, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int image = listItems.get(position).getImage();
        String title = listItems.get(position).getTitle();
        long price = listItems.get(position).getPrice();
        String address = listItems.get(position).getAddress();
        String date = listItems.get(position).getDate();
        holder.imageView.setImageResource(image);
        holder.tvTitle.setText(title);
        holder.tvPrice.setText(formatCurrency(price));
        holder.tvAddress.setText(address);
        holder.tvDate.setText(date);

    }

    private String formatCurrency(long price) {
        return currencyVN.format(price) + " đ";
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imgFav;
        TextView tvTitle, tvPrice, tvAddress, tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imgFav = itemView.findViewById(R.id.imgFav);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgFav.setOnClickListener(v -> {
                if (imgFav.getTag().toString().equals("1")) {
                    imgFav.setTag("2");
                    imgFav.setImageResource(R.drawable.ic_item_tym_checked);
                    Toast.makeText(itemView.getContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                } else {
                    imgFav.setTag("1");
                    imgFav.setImageResource(R.drawable.ic_item_tym);
                }
            });
        }
    }
}
