package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.NotiController;
import com.example.petmarket2020.R;

public class MainNotiFragment extends Fragment {
    RecyclerView rvNoti;
    NotiController notiController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notiController = new NotiController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_noti, container, false);
        rvNoti = view.findViewById(R.id.rvNoti);
        rvNoti.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvNoti.setHasFixedSize(true);
        notiController.getNoti(rvNoti);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}