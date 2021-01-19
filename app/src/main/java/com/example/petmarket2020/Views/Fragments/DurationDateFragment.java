package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class DurationDateFragment extends Fragment {
    //    private static final String DELIM = "@";
    private AutoCompleteTextView actv;
    private String[] option;
    private EditText etPrice;
    private TextView tvTitle;
    private MyViewPager vpg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpg = Objects.requireNonNull(getActivity()).findViewById(R.id.vpg);
        tvTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duration_date, container, false);
        actv = view.findViewById(R.id.actv);
        option = new String[]{"7 ngày", "10 ngày", "15 ngày"};
        etPrice = view.findViewById(R.id.etPrice);
        // init old data
        String price = String.valueOf(PostActivity.getData(PostActivity.KEY_PRICE));
        price = price.equals("null") ? null : price;
        String duration = String.valueOf(PostActivity.getData(PostActivity.KEY_DURATION));
        duration = duration.equals("null") ? null : duration;
        if (duration != null && !duration.contains("ngày")) {
            duration += " ngày";
        }
        if (!TextUtils.isEmpty(price)) etPrice.setText(price);
        if (!TextUtils.isEmpty(duration))
            actv.setText(duration);
        else actv.setText(option[0], false);
        actv.setAdapter(new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, option));
        actv.setOnClickListener(v -> actv.showDropDown());
        view.findViewById(R.id.bab).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etPrice.getText().toString())) {
                PostActivity.addOrUpdateData(PostActivity.KEY_PRICE, etPrice.getText().toString());
                PostActivity.addOrUpdateData(PostActivity.KEY_DURATION, actv.getText().toString());
                int currentIndex = vpg.getCurrentItem();
                vpg.setCurrentItem(currentIndex + 1);
                tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            } else {
                etPrice.requestFocus();
            }
        });
        return view;
    }

}