package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.util.Log;
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

public class WaitingFragment extends Fragment {
    private RecyclerView rvWaiting;
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
        View view = inflater.inflate(R.layout.fragment_waiting, container, false);
        rvWaiting = view.findViewById(R.id.rvWaiting);
        rvWaiting.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        pgBar = view.findViewById(R.id.pgBar);
        postController.getPostByStatus(0, rvWaiting, pgBar, new IControlData() {
            @Override
            public void responseData(Object data) {
                MainMeFragment.setBadgeWaiting((Integer) data);
                Log.e("setBadgeWaiting", String.valueOf(data));

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        rvWaiting.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
//        List<PostModel> postModelList = new ArrayList<>();
//        PostModel postModel = new PostModel("PO1", "", 0, false, 10, 10, 10, 200000, "Mèo Xiêm", "", "", "", "", "", "20/12/2020 14:35",
//                "Mồn lèo lèo lèo lèo lèo lèo lèo lèo lèo lèo lèo"
//                , 0, "", "", Arrays.asList("images/PO16101915940350.jpg", "images/PO16101915940351.jpg"));
//        postModelList.add(postModel);
//        postModelList.add(postModel);
//        postModelList.add(postModel);
//        postModelList.add(postModel);
//        postModelList.add(postModel);
//        postModelList.add(postModel);
//        postModelList.add(postModel);
//        RV_PostManageAdapter postManageAdapter = new RV_PostManageAdapter(postModelList, 3);
//        rv.setAdapter(postManageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}