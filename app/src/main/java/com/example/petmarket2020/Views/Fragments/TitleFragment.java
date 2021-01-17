package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Objects;

public class TitleFragment extends Fragment {
    private TextView tvError;
    private int len = 0;
    private TextView tvTitle;
    private MyViewPager vpg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpg = Objects.requireNonNull(getActivity()).findViewById(R.id.vpg);
        tvTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        tvError = view.findViewById(R.id.tvError);
        EditText etTitle = view.findViewById(R.id.etTitle);
        if (TextUtils.isEmpty(etTitle.getText().toString()) && PostActivity.getData(PostActivity.KEY_TITLE) != null) {
            etTitle.setText((String) PostActivity.getData(PostActivity.KEY_TITLE));
        }
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    len = s.toString().trim().length();
                    if (len < 11) tvError.setText(getString(R.string.TT10Char));
                    else tvError.setText(null);
                } else {
                    tvError.setText(getString(R.string.TTPleaseType));
                }
            }
        });
        view.findViewById(R.id.bab).setOnClickListener(v -> {
            if (TextUtils.isEmpty(tvError.getText().toString())) {
                PostActivity.addData(PostActivity.KEY_TITLE, etTitle.getText().toString());
                int currentIndex = vpg.getCurrentItem();
                vpg.setCurrentItem(currentIndex + 1);
                tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            } else {
                etTitle.requestFocus();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hiddenKeyboard(Objects.requireNonNull(getActivity()));
    }
}