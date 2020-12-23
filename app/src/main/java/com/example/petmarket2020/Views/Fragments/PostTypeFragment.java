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

public class PostTypeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_type, container, false);
        RadioGroup radioGroup = view.findViewById(R.id.rdGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.rdbtnSell == checkedId) {
                    Log.e("Trang 1", "can ban");
                } else {
                    Log.e("Trang 1", "can mua");
                }
                PostActivity.vpg.setCurrentItem(1);
                PostActivity.tvTitle.setText(PostActivity.listTitle.get(1));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PostActivity.tvTitle.setText(PostActivity.listTitle.get(0));
    }
}