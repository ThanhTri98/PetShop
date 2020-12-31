package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;
import com.example.petmarket2020.HelperClass.Utils;

import java.util.Objects;

public class AdditionalInfoFragment extends Fragment {
    private AutoCompleteTextView actv;
    private static final String[] option = {"Dưới 60 ngày", "Từ 60 ngày tới 1 tuổi", "Từ 1 tuổi tới 2 tuổi", "Trên 2 tuổi"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additional_info, container, false);
        actv = view.findViewById(R.id.actv);

        actv.setText(option[0], false);
        actv.setAdapter(new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, option));
        actv.setOnClickListener(v -> actv.showDropDown());
        return view;
    }

    @Override
    public void onResume() {
        Utils.hiddenKeyboard(Objects.requireNonNull(getActivity()));
        super.onResume();
    }
}