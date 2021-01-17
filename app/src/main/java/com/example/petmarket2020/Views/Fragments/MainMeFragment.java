package com.example.petmarket2020.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.petmarket2020.Adapters.VP_PostManageAdapter;
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.LoginActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainMeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 vpg2;
    private RelativeLayout rlNoLogin;
    private PostController postController;
    private VP_PostManageAdapter vp_postManageAdapter;
    private TabLayoutMediator tabLayoutMediator;

    private static BadgeDrawable badgeSelling;
    private static BadgeDrawable badgeHidden;
    private static BadgeDrawable badgeRefused;
    private static BadgeDrawable badgeWaiting;
    private boolean firstStart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postController = new PostController(getActivity());
        vp_postManageAdapter = new VP_PostManageAdapter(Objects.requireNonNull(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_me, container, false);
        getWidget(view);
        vpg2.setAdapter(vp_postManageAdapter);
        tabLayoutMediator = new TabLayoutMediator(tabLayout, vpg2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Đang bán");
                    badgeSelling = tab.getOrCreateBadge();
//                    initBadge(badgeSelling, postManageItemCount.getSellingCount());
                    break;
                case 1:
                    tab.setText("Đã Ẩn");
                    badgeHidden = tab.getOrCreateBadge();
//                    initBadge(badgeHidden, postManageItemCount.getHiddenCount());
                    break;
                case 2:
                    tab.setText("Bị từ chối");
                    badgeRefused = tab.getOrCreateBadge();
//                    initBadge(badgeRefused, postManageItemCount.getRefuseCount());
                    break;
                case 3:
                    tab.setText("Chờ duyệt");
                    badgeWaiting = tab.getOrCreateBadge();
//                    initBadge(badgeWaiting, postManageItemCount.getWaitingCount());
                    break;
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (postController.checkLogin()) {
            SessionManager.PostManageItemCount postManageItemCount = postController.getPostOfUserSession();
            rlNoLogin.setVisibility(View.GONE);
            vpg2.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            if (!firstStart) {
                firstStart = true;
                tabLayoutMediator.attach();
            }
        } else {
            tabLayoutMediator.detach();
            vpg2.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            rlNoLogin.setVisibility(View.VISIBLE);
        }
    }

    private void initBadge(BadgeDrawable badgeDrawable, int firstNumber) {
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimary));
        badgeDrawable.setHorizontalOffset(8);
        badgeDrawable.setMaxCharacterCount(2);
        badgeDrawable.setVisible(firstNumber != 0);
        badgeDrawable.setNumber(firstNumber);
    }

    private void getWidget(View view) {
        view.findViewById(R.id.btnLogin).setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
        tabLayout = view.findViewById(R.id.tabLayout);
        vpg2 = view.findViewById(R.id.vpg2);
        rlNoLogin = view.findViewById(R.id.rlNoLogin);
    }

    public static void setBadgeSelling(int number) {
        if (badgeSelling != null) {
            if (number != 0)
                badgeSelling.setNumber(number);
            else
                badgeSelling.setVisible(false);
        }
    }

    public static void setBadgeHidden(int number) {
        if (badgeHidden != null) {
            if (number != 0)
                badgeHidden.setNumber(number);
            else
                badgeHidden.setVisible(false);
        }
    }

    public static void setBadgeRefused(int number) {
        if (badgeRefused != null) {
            if (number != 0)
                badgeRefused.setNumber(number);
            else
                badgeRefused.setVisible(false);
        }
    }

    public static void setBadgeWaiting(int number) {
        if (badgeWaiting != null) {
            if (number != 0)
                badgeWaiting.setNumber(number);
            else
                badgeWaiting.setVisible(false);
        }
    }
}