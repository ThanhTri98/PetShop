package com.example.petmarket2020.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.petmarket2020.Adapters.VP_PostManageAdapter;
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainMeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 vpg2;
    private VP_PostManageAdapter vp_postManageAdapter;
    private TabLayoutMediator tabLayoutMediator;
    private RelativeLayout rlNoLogin;
    private PostController postController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postController = new PostController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_me, container, false);
        getWidget(view);
        vp_postManageAdapter = new VP_PostManageAdapter(getActivity());
        vpg2.setAdapter(vp_postManageAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (postController.checkLogin()) {
            rlNoLogin.setVisibility(View.GONE);
            vpg2.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, vpg2, (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("Đang bán");
//                    BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
//                    badgeDrawable.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorVerify));
//                    badgeDrawable.setVisible(true);
//                    badgeDrawable.setNumber(20);
//                    badgeDrawable.setMaxCharacterCount(1);
                        break;
                    case 1:
                        tab.setText("Đã Ẩn");
                        break;
                    case 2:
                        tab.setText("Bị từ chối");
                        break;
                    case 3:
                        tab.setText("Chờ duyệt");
                        break;
                }
            });
            tabLayoutMediator.attach();
        } else {
            vpg2.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            rlNoLogin.setVisibility(View.VISIBLE);
        }
    }

    private void getWidget(View view) {
        view.findViewById(R.id.btnLogin).setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
        tabLayout = view.findViewById(R.id.tabLayout);
        vpg2 = view.findViewById(R.id.vpg2);
        rlNoLogin = view.findViewById(R.id.rlNoLogin);
    }
}