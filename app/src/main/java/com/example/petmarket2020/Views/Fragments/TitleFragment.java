package com.example.petmarket2020.Views.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;
import com.example.petmarket2020.Utils.Utils;
import com.example.petmarket2020.Views.PostActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class TitleFragment extends Fragment {
    private TextView tvError;
    private int len = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        tvError = view.findViewById(R.id.tvError);
        EditText etTitle = view.findViewById(R.id.etTitle);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    len = s.toString().trim().length();
                    if (len < 11) tvError.setText("Tiêu đề > 10 kí tự");
                    else tvError.setText(null);
                } else {
                    tvError.setText("Vui lòng nhập tiêu đề!");
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Utils.HiddenKeyboard(Objects.requireNonNull(getActivity()));
        PostActivity.bab.setVisibility(View.VISIBLE);
        super.onResume();

    }
}