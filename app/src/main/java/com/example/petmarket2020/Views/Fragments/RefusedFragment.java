package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.RV_PostManageAdapter;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RefusedFragment extends Fragment {
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refused, container, false);
        rv = view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        List<PostModel> postModelList = new ArrayList<>();
        PostModel postModel = new PostModel("PO1", "", 0, false, 10, 10, 10, 200000, "Mèo Xiêm", "", "", "", "", "", "20/12/2020 14:35",
                "Mồn lèo lèo lèo lèo lèo lèo lèo lèo lèo lèo lèo"
                , 0, "", "", Arrays.asList("images/PO16101915940350.jpg", "images/PO16101915940351.jpg"));
        postModelList.add(postModel);
        postModelList.add(postModel);
        postModelList.add(postModel);
        postModelList.add(postModel);
        postModelList.add(postModel);
        postModelList.add(postModel);
        postModelList.add(postModel);
        RV_PostManageAdapter postManageAdapter = new RV_PostManageAdapter(postModelList, 2);
        rv.setAdapter(postManageAdapter);
    }
}