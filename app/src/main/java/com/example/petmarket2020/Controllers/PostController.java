package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.petmarket2020.DAL.PostDAL;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.Interfaces.IPost;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.Map;
import java.util.Objects;

public class PostController {
    private static final String KEY_DOG = "dog", KEY_CAT = "cat";
    private final Activity activity;
    private final PostDAL postDAL;

    public PostController(Activity activity) {
        this.postDAL = new PostDAL();
        this.activity = activity;
    }

    public void getPetBreeds(String type, RadioGroup radioGroup, MyViewPager vpg, TextView tvTitle) {
        if (!PostActivity.isLoaded()) {
            radioGroup.removeAllViews();
            if (PostActivity.getData(type) != null) {
                initView((Map<Integer, String>) Objects.requireNonNull(PostActivity.getData(type)), radioGroup, tvTitle, vpg);
                return;
            }
            postDAL.getPetBreeds(type, new IPost() {
                @Override
                public void sendData(Object objData) {
                    if (objData != null) {
                        Map<Integer, String> dataResp = (Map<Integer, String>) objData;
                        if (type.equals(KEY_DOG) && PostActivity.getData(KEY_DOG) == null) {
                            PostActivity.addData(KEY_DOG, dataResp);
                        } else if (type.equals(KEY_CAT) && PostActivity.getData(KEY_CAT) == null) {
                            PostActivity.addData(KEY_CAT, dataResp);
                        }
                        initView(dataResp, radioGroup, tvTitle, vpg);
                    }
                }
            });
        } else {
            if (radioGroup.getChildCount() == 0) {
                radioGroup.removeAllViews();
                initView((Map<Integer, String>) Objects.requireNonNull(PostActivity.getData((String) PostActivity.getData(PostActivity.KEY_PET_TYPE))), radioGroup, tvTitle, vpg);
            }
        }

    }

    private void initView(Map<Integer, String> dataResp, RadioGroup radioGroup, TextView tvTitle, MyViewPager vpg) {
        RadioButton radioButton;
        ViewGroup.LayoutParams layoutParams = new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}},
                new int[]{Color.WHITE, Color.BLACK}
        );
        for (Map.Entry<Integer, String> data : dataResp.entrySet()) {
            radioButton = new RadioButton(activity);
            setListener(data.getValue(), radioButton, tvTitle, vpg);
            radioButton.setId(data.getKey());
            radioButton.setText(data.getValue());
            radioButton.setTextColor(colorStateList);
            radioButton.setBackgroundResource(R.drawable.bg_rd_selector);
            radioButton.setTextSize(15);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setButtonDrawable(R.drawable.custom_radio_button);
            radioButton.setPadding(45, 25, 45, 25);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bg_rd_ic_end_selector, 0);
            radioGroup.addView(radioButton);
        }
        PostActivity.setLoadingStatus(false);
    }

    private void setListener(final String value, RadioButton radioButton, TextView tvTitle, MyViewPager vpg) {
        radioButton.setOnClickListener(v -> {
            PostActivity.addData(PostActivity.KEY_BREEDS, value);
            int currentIndex = vpg.getCurrentItem();
            vpg.setCurrentItem(currentIndex + 1);
            tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
        });
    }



}
