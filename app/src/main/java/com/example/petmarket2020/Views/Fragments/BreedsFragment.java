package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class BreedsFragment extends Fragment {
    private static final String TAG = "BreedsFragment";
    private RadioGroup radioGroup;

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
        View view = inflater.inflate(R.layout.fragment_breeds, container, false);
        radioGroup = view.findViewById(R.id.rdGroup);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hiddenKeyboard(Objects.requireNonNull(getActivity()));
        if (PostActivity.getData(PostActivity.KEY_PET_TYPE) != null) {
            String petType = (String) PostActivity.getData(PostActivity.KEY_PET_TYPE);
            PostActivity.getPostController().getPetBreeds(petType, radioGroup, vpg, tvTitle);
        }
    }

}