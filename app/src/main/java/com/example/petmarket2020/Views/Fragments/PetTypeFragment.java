package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class PetTypeFragment extends Fragment {
    private static final String TAG = "PetTypeFragment";
    private TextView tvTitle;
    private MyViewPager vpg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
        vpg = getActivity().findViewById(R.id.vpg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_type, container, false);
        RadioGroup radioGroup = view.findViewById(R.id.rdGroup);
        String dog = "dog", cat = "cat";
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (R.id.rdbtnDog == checkedId) {
                if (!Objects.equals(PostActivity.getData(PostActivity.KEY_PET_TYPE), dog))
                    PostActivity.setLoadingStatus(true);
                PostActivity.addData(PostActivity.KEY_PET_TYPE, dog);
            } else {
                if (!Objects.equals(PostActivity.getData(PostActivity.KEY_PET_TYPE), cat))
                    PostActivity.setLoadingStatus(true);
                PostActivity.addData(PostActivity.KEY_PET_TYPE, cat);
            }
        });
        int childCount = radioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            (radioGroup.getChildAt(i)).setOnClickListener(v -> {
                int currentIndex = vpg.getCurrentItem();
                vpg.setCurrentItem(currentIndex + 1);
                tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            });
        }
        return view;
    }
}