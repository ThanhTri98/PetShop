package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class AdditionalInfoFragment extends Fragment {
    private AutoCompleteTextView actv;
    private TextView tvTitle;
    private MyViewPager vpg;
    private RadioGroup rgGender, rgInject, rgHealthy;

    private String oldGender = "Không rõ";
    private String oldInject = "Không";
    private String oldHealth = "Không";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpg = Objects.requireNonNull(getActivity()).findViewById(R.id.vpg);
        tvTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additional_info, container, false);
        rgGender = view.findViewById(R.id.rgGender);
        rgInject = view.findViewById(R.id.rgInject);
        rgHealthy = view.findViewById(R.id.rgHealthy);
        actv = view.findViewById(R.id.actv);
        String[] option = new String[]{"Dưới 60 ngày", "Từ 60 ngày tới 1 tuổi", "Từ 1 tuổi tới 2 tuổi", "Trên 2 tuổi"};
        actv.setAdapter(new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, option));
        // init old data
        String  peAge = (String) PostActivity.getData(PostActivity.KEY_PE_AGE);
        if (!TextUtils.isEmpty(peAge))
            actv.setText(peAge);
        else
            actv.setText(option[0], false);
        // Radio group
        String gender = (String) PostActivity.getData(PostActivity.KEY_GENDER);
        if (!TextUtils.isEmpty(gender)) {
            oldGender = gender;
            setOldValueRadioButton(rgGender, gender);
        }
        String inject = (String) PostActivity.getData(PostActivity.KEY_INJECT);
        if (!TextUtils.isEmpty(inject)) {
            oldInject = inject;
            setOldValueRadioButton(rgInject, inject);
        }
        String health = (String) PostActivity.getData(PostActivity.KEY_HEALTH);
        if (!TextUtils.isEmpty(health)) {
            oldHealth = health;
            setOldValueRadioButton(rgHealthy, health);
        }
        setRDGroupListener();
        actv.setOnClickListener(v -> actv.showDropDown());
        view.findViewById(R.id.bab).setOnClickListener(v -> {
            int currentIndex = vpg.getCurrentItem();
            vpg.setCurrentItem(currentIndex + 1);
            tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            PostActivity.addOrUpdateData(PostActivity.KEY_GENDER, oldGender);
            PostActivity.addOrUpdateData(PostActivity.KEY_INJECT, oldInject);
            PostActivity.addOrUpdateData(PostActivity.KEY_HEALTH, oldHealth);
            PostActivity.addOrUpdateData(PostActivity.KEY_PE_AGE, actv.getText().toString());
        });
        return view;
    }

    private void setOldValueRadioButton(RadioGroup radioGroup, String oldValue) {
        int childCount = radioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.getText().toString().equals(oldValue)) {
                radioButton.setChecked(true);
                break;
            }
        }
    }

    private void setRDGroupListener() {
        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdMale) oldGender = "Đực";
            else if (checkedId == R.id.rdFeMale) oldGender = "Cái";
            else oldGender = "Không rõ";
        });
        rgInject.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdYes1) oldInject = "Có";
            else oldInject = "Không";
        });
        rgHealthy.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdYes2) oldHealth = "Có";
            else oldHealth = "Không";
        });
    }

    @Override
    public void onResume() {
        Utils.hiddenKeyboard(Objects.requireNonNull(getActivity()));
        super.onResume();
    }
}