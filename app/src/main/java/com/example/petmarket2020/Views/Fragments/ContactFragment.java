package com.example.petmarket2020.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.LocationActivity;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class ContactFragment extends Fragment {
    private EditText etTP, etQH;
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
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        getWidget(view);
        setListener();

        view.findViewById(R.id.bab).setOnClickListener(v -> {
            int currentIndex = vpg.getCurrentItem();
            vpg.setCurrentItem(currentIndex + 1);
            tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
        });
        return view;
    }

    private void getWidget(View view) {
        etTP = view.findViewById(R.id.etTP);
        etQH = view.findViewById(R.id.etQH);
    }

    private void setListener() {
        etTP.setOnClickListener(v -> startActivity(new Intent(getActivity(), LocationActivity.class)));
    }

}