package com.example.petmarket2020.Views.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.Adapters.SliderAdapter;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewPostFragment extends Fragment {
    private TextView tvTitles;
    private MyViewPager vpg;
    private RelativeLayout rlBar;
    private SliderView sliderView;
    private TextView tvPostType, tvTitle, tvPrice, tvBreed, tvGender, tvInject, tvHealthy, tvAge;
    private SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitles = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
        vpg = getActivity().findViewById(R.id.vpg);
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_post, container, false);
        rlBar = view.findViewById(R.id.rlBar);
        sliderView = view.findViewById(R.id.imageSlider);
        tvPostType = view.findViewById(R.id.tvPostType);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvBreed = view.findViewById(R.id.tvBreed);
        tvGender = view.findViewById(R.id.tvGender);
        tvInject = view.findViewById(R.id.tvInject);
        tvHealthy = view.findViewById(R.id.tvHealthy);
        tvAge = view.findViewById(R.id.tvAge);
        TextView tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        TextView tvArea = view.findViewById(R.id.tvArea);
        tvPhoneNumber.setText((String) sessionManager.getInfo(SessionManager.KEY_PHONE));

        String address = (String) sessionManager.getInfo(SessionManager.KEY_ADDRESS);
        int index = address.lastIndexOf(",");
        tvArea.setText(address.substring(index + 1).trim());

        view.findViewById(R.id.bab).setOnClickListener(v -> {
            // Xử lý đăng tin ở đây


            int currentIndex = vpg.getCurrentItem();
            vpg.setCurrentItem(currentIndex + 1);
            tvTitles.setText(PostActivity.getTitle(currentIndex + 1));
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, Object> hashMap = PostActivity.getAllData();
        for (Map.Entry<String, Object> data : hashMap.entrySet()) {
            switch (data.getKey()) {
                case PostActivity.KEY_POST_TYPE:
                    // cần bán -> bán
                    String poType = "[" + ((String) data.getValue()).split(" ")[1] + "]";
                    tvPostType.setText(poType);
                    break;
                case PostActivity.KEY_BREEDS:
                    tvBreed.setText((String) data.getValue());
                    break;
                case PostActivity.KEY_TITLE:
                    tvTitle.setText((String) data.getValue());
                    break;
                case PostActivity.KEY_DURATION_DATE:
                    String[] items = ((String) data.getValue()).split("@"); // 0:price, 1:time
                    tvPrice.setText(Utils.formatCurrencyVN(Double.parseDouble(items[0])));
                    break;
                case PostActivity.KEY_INFO:
                    String[] info_s = ((String) data.getValue()).split("@");
                    //0: gender, 1:inject, 2:healthy, 3: age
                    tvGender.setText(info_s[0]);
                    tvInject.setText(info_s[1]);
                    tvHealthy.setText(info_s[2]);
                    tvAge.setText(info_s[3]);
                    break;
            }
        }
        HashMap<String, Bitmap> dataMap = PostActivity.getAllImages();
        List<Bitmap> bitmaps = new ArrayList<>(dataMap.values());
        SliderAdapter sliderAdapter = new SliderAdapter(bitmaps);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.setScrollTimeInSec(2);
        sliderView.startAutoCycle();
    }
}