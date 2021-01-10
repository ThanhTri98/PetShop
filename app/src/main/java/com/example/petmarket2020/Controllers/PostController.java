package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.RV_PosterAdapter;
import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.DAL.PostDAL;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.Interfaces.IPost;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void setNode(String node) {
        postDAL.setRef(node);
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

    public void postUpload(PostModel postModel, HashMap<String, byte[]> mapImage, RelativeLayout rlBar, MyViewPager vpg, TextView tvTitles) {
        rlBar.setVisibility(View.VISIBLE);
        postDAL.postUpload(postModel, mapImage, new IPost() {
            @Override
            public void isSuccessful(boolean isSu) {
                if (isSu) {
                    int nextIndex = vpg.getCurrentItem() + 1;
                    vpg.setCurrentItem(nextIndex);
                    tvTitles.setText(PostActivity.getTitle(nextIndex));
                } else {
                    Toast.makeText(activity, "Đã xảy ra lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
                rlBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private RV_PosterAdapter homeRVPosterAdapter;
    private boolean isLoading = true;

    public void postDownload(RecyclerView rvPoster, RelativeLayout rlBar, NestedScrollView nestedScrollView, RecyclerView rvHot, RelativeLayout rlBarHot) {
        rlBar.setVisibility(View.VISIBLE);
        List<PosterItem> posterItemList = new ArrayList<>();
//        rlBarHot.setVisibility(View.VISIBLE);
        rvPoster.setLayoutManager(new GridLayoutManager(activity, 2));
//        rvHot.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        postDAL.postDownload(new IPost() {
            @Override
            public void sendData(Object objData) {
                List<PosterItem> posterItems = (List<PosterItem>) objData;
                posterItemList.addAll(posterItems);
                homeRVPosterAdapter = new RV_PosterAdapter(posterItems);
                rvPoster.setAdapter(homeRVPosterAdapter);
//                rvHot.setAdapter(homeRVPosterAdapter);
                rlBar.setVisibility(View.GONE);
                isLoading = false;
            }
        });
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                if (!isLoading) {
                    rlBar.setVisibility(View.VISIBLE);
                    isLoading = true;
                    postDAL.postDownload(new IPost() {
                        @Override
                        public void sendData(Object objData) {
                            List<PosterItem> posterItems = (List<PosterItem>) objData;
                            posterItemList.addAll(posterItems);
                            new Handler().postDelayed(() -> {
                                        if (!posterItems.isEmpty()) {
                                            homeRVPosterAdapter.updateData(posterItemList);
                                            isLoading = false;
                                        }
                                        rlBar.setVisibility(View.GONE);
                                    }, 500
                            );
                        }
                    });
                }
            }
        });
    }
}