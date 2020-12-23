package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.petmarket2020.R;
import com.example.petmarket2020.Utils.Utils;
import com.example.petmarket2020.Views.PostActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class TitleFragment extends Fragment {
    private TextInputLayout tilTitle;
    private EditText etTitle;
    private static final int TEXT_LENGTH = 70;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        tilTitle = view.findViewById(R.id.tilTitle);

        etTitle = view.findViewById(R.id.etTitle);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    int length = s.toString().trim().length();
                    if (length < 11) tilTitle.setError("Tiêu đề > 10 kí tự");
                    else tilTitle.setError(null);
                } else {
                    tilTitle.setError("Vui lòng nhập tiêu đề");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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