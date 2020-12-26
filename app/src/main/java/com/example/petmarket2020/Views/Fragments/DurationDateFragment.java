package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;

public class DurationDateFragment extends Fragment {
    private AutoCompleteTextView actv;
    private static final  String[] option = {"7 ngày", "10 ngày", "15 ngày"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duration_date, container, false);
        actv = view.findViewById(R.id.actv);

        actv.setText(option[0], false);
        actv.setAdapter(new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, option));
        actv.setOnClickListener(v -> actv.showDropDown());
        return view;
    }

}