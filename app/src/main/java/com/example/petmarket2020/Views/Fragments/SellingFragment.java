package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.R;

public class SellingFragment extends Fragment {
    private RecyclerView rvSelling;
    private View pgBar;
    private PostController postController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postController = new PostController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selling, container, false);
        rvSelling = view.findViewById(R.id.rvSelling);
        rvSelling.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        pgBar = view.findViewById(R.id.pgBar);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        postController.getPostByStatus(1, rvSelling, pgBar, new IControlData() {
            @Override
            public void responseData(Object data) {
                MainMeFragment.setBadgeSelling((Integer) data);
            }
        });
    }
}