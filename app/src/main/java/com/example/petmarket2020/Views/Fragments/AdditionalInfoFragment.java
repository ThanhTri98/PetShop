package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
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
    private static final String DELIM = "|";
    private AutoCompleteTextView actv;
    private String[] option;
    private TextView tvTitle;
    private MyViewPager vpg;
    private RadioGroup rgGender, rgInject, rgHealthy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpg = Objects.requireNonNull(getActivity()).findViewById(R.id.vpg);
        tvTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
        option = new String[]{"Dưới 60 ngày", "Từ 60 ngày tới 1 tuổi", "Từ 1 tuổi tới 2 tuổi", "Trên 2 tuổi"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additional_info, container, false);
        rgGender = view.findViewById(R.id.rgGender);
        rgInject = view.findViewById(R.id.rgInject);
        rgHealthy = view.findViewById(R.id.rgHealthy);
        actv = view.findViewById(R.id.actv);
        actv.setText(option[0], false);
        actv.setAdapter(new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, option));
        actv.setOnClickListener(v -> actv.showDropDown());


        view.findViewById(R.id.bab).setOnClickListener(v -> {
            int currentIndex = vpg.getCurrentItem();
            vpg.setCurrentItem(currentIndex + 1);
            tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            String data = getValue(new RadioGroup[]{rgGender, rgInject, rgHealthy}) + actv.getText().toString();
            PostActivity.addData(PostActivity.KEY_INFO, data);
        });
        return view;
    }

    private String getValue(RadioGroup[] radioGroups) {
        StringBuilder result = new StringBuilder();
        for (RadioGroup radioGroup : radioGroups) {
            int childCount = radioGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (radioButton.isChecked()) {
                    result.append(radioButton.getText().toString()).append(DELIM);
                    break;
                }
            }
        }
        return result.toString();
    }

    @Override
    public void onResume() {
        Utils.hiddenKeyboard(Objects.requireNonNull(getActivity()));
        super.onResume();
    }
}