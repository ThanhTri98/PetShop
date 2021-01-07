package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;

import java.util.Objects;

public class ViewPostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_post, container, false);
        view.findViewById(R.id.bab).setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).finish();
        });
        return view;
    }
}