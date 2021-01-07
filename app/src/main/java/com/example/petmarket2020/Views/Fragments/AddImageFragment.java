package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class AddImageFragment extends Fragment {
    private TextView tvTitle, tvError;
    private MyViewPager vpg;
    private LinearLayout llImages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpg = Objects.requireNonNull(getActivity()).findViewById(R.id.vpg);
        tvTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_image, container, false);
        llImages = view.findViewById(R.id.llImages);
        tvError = view.findViewById(R.id.tvError);
        view.findViewById(R.id.flAddImage).setOnClickListener(v -> {
            if (!addImage(llImages)) {
                tvError.setText(getString(R.string.AIText02));
                new Handler().postDelayed(() -> {
                    tvError.setText("");
                }, 1000);
            } else {
                tvError.setText("");
            }

        });
        view.findViewById(R.id.bab).setOnClickListener(v -> {
            if (llImages.getChildCount() > 1) {
                int currentIndex = vpg.getCurrentItem();
                vpg.setCurrentItem(currentIndex + 1);
                tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            } else {
                tvError.setText(getString(R.string.AIText2));
            }
        });
        return view;
    }

    private boolean addImage(LinearLayout llImages) {
        int size = llImages.getChildCount();
        if (size == 6) {
            return false;
        }
        View view = getLayoutInflater().inflate(R.layout.image_layout, null);
        ImageView ivDelete = view.findViewById(R.id.ivDelete);
        ivDelete.setTag(size);
        ivDelete.setOnClickListener(v -> {
            Toast.makeText(getContext(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
            llImages.removeView(view);
        });
        llImages.addView(view);
        return true;
    }

}