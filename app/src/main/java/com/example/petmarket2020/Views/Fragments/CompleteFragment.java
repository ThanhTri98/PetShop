package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;

import java.util.Objects;

public class CompleteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        view.findViewById(R.id.bab).setOnClickListener(v -> Objects.requireNonNull(getActivity()).finish());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).findViewById(R.id.ivBack).setVisibility(View.GONE);
    }
}