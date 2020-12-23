package com.example.petmarket2020.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.LocationActivity;

public class ContactFragment extends Fragment {
    private EditText etTP, etQH;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        getWidget(view);
        setListener();
        return view;
    }

    private void getWidget(View view) {
        etTP = view.findViewById(R.id.etTP);
        etQH = view.findViewById(R.id.etQH);
    }

    private void setListener() {
        etTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LocationActivity.class));
            }
        });
    }

}