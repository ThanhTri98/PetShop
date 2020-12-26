package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

public class PetTypeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_type, container, false);
        RadioGroup radioGroup = view.findViewById(R.id.rdGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.rdbtnDog == checkedId) {
                    Log.e("Trang 1", "cho");
                } else {
                    Log.e("Trang 1", "meo");
                }
                PostActivity.vpg.setCurrentItem(2);
                PostActivity.tvTitle.setText(PostActivity.listTitle.get(2));
            }
        });
        return view;
    }
}