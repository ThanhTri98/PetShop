package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class PetTypeFragment extends Fragment {
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
                PostActivity.addOrUpdateData(PostActivity.KEY_PET_TYPE, dog);
            } else {
                if (!Objects.equals(PostActivity.getData(PostActivity.KEY_PET_TYPE), cat))
                    PostActivity.setLoadingStatus(true);
                PostActivity.addOrUpdateData(PostActivity.KEY_PET_TYPE, cat);
            }
        });
        int childCount = radioGroup.getChildCount();
        boolean is = false;
        // init default Data
        String petType = (String) PostActivity.getData(PostActivity.KEY_PET_TYPE);
        for (int i = 0; i < childCount; i++) {
            RadioButton rdBtn = (RadioButton) radioGroup.getChildAt(i);
            if (!is && !TextUtils.isEmpty(petType)) {
                petType = petType.equals("cat") ? "Mèo" : "Chó";
                Log.e("PÉT TYPE",petType);
                if (rdBtn.getText().toString().equals(petType)) {
                    radioGroup.check(rdBtn.getId());
                    is = true;
                }
            }
            rdBtn.setOnClickListener(v -> {
                int currentIndex = vpg.getCurrentItem();
                vpg.setCurrentItem(currentIndex + 1);
                tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            });
        }
        return view;
    }
}