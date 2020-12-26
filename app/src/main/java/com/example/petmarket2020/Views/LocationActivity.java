package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.RV_LocationAdapter;
import com.example.petmarket2020.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocationActivity extends AppCompatActivity {
    private EditText etLocation;
    private ImageView ivBack;
    private RecyclerView rvLocation;
    private final Activity activity = this;
    private RV_LocationAdapter adapter;
    List<String> locations;
    List<String> locationsTmp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getWidget();
        initRecyclerView(rvLocation);
        ((TextView) findViewById(R.id.tvLocation)).setText("Chọn Tỉnh/Thành phố");
        ivBack.setOnClickListener(v -> finish());
        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    locations.clear();
                    locations.addAll(locationsTmp);
                } else {
                    locations.addAll(reSearch(s.toString()));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private List<String> reSearch(final String keyword) {
        locations.clear();
        List<String> result = locationsTmp.stream().filter(item -> item.toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        return result.isEmpty() ? locationsTmp : result;
    }

    private void getWidget() {
        etLocation = findViewById(R.id.etLocation);
        rvLocation = findViewById(R.id.rvLocation);
        ivBack = findViewById(R.id.ivBack);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        locations = new ArrayList<>();
        locations.add("Hà Nội");
        locations.add("TP. Hồ Chí Minh");
        locations.add("Bình Định");
        locations.add("Đà Nẵng");
        locations.add("Phú Yên");
        locations.add("Quảng Bình");
        locations.add("Quảng Trị");
        locations.add("Huế");
        locations.add("Gia Lai");
        locations.add("Đăk Lăk");
        locations.add("Campuchia");
        locations.add("Thái Lan");
        locations.add("Lào");
        locations.add("Nhật Bản");
        locations.add("Ấn Độ");
        locations.add("Méo Meo Mèo Meo");
        locationsTmp = new ArrayList<>(locations);
        adapter = new RV_LocationAdapter(activity, locations);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}