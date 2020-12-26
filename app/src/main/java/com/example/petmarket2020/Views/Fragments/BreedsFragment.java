package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;
import com.example.petmarket2020.Utils.Utils;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class BreedsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breeds, container, false);
        RadioGroup radioGroup = view.findViewById(R.id.rdGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.rdbtnPug == checkedId) {
                    Log.e("Trang 4", "cho pug");
                } else if (R.id.rdbtnPoodle == checkedId) {
                    Log.e("Trang 4", "cho Poodle");
                } else {
                    Log.e("Trang 4", "meo");
                }
            }

        });
        int childCount = radioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            (radioGroup.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostActivity.vpg.setCurrentItem(3);
                    PostActivity.tvTitle.setText(PostActivity.listTitle.get(3));
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        Utils.HiddenKeyboard(Objects.requireNonNull(getActivity()));
        PostActivity.bab.setVisibility(View.GONE);
        super.onResume();
    }
}