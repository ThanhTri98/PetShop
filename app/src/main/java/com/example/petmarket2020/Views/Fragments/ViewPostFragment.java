package com.example.petmarket2020.Views.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewPostFragment extends Fragment {
    private TextView tvTitles;
    private MyViewPager vpg;
    private RelativeLayout rlBar;
    private SliderView sliderView;
    private TextView tvPostType, tvTitle, tvPrice, tvBreed, tvGender, tvInject, tvHealthy, tvAge;
    private PostModel postModel;
    private String area;
    private HashMap<String, byte[]> mapImage;
    private UsersModel usersModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitles = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
        vpg = getActivity().findViewById(R.id.vpg);
        SessionManager sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));
        usersModel = sessionManager.getUserSession();
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
        tvPhoneNumber.setText(usersModel.getPhoneNumber());
        area = getArea();
        tvArea.setText(area);
        view.findViewById(R.id.bab).setOnClickListener(v -> {
            // Xử lý đăng tin ở đây
            postModel.setStatus(0);
            PostActivity.getPostController().postUpload(postModel,PostActivity.getImageRemoved(), mapImage, rlBar, vpg, tvTitles);
        });
        return view;
    }

    private String getArea() {
        String address = usersModel.getAddress();
        int index = address.lastIndexOf(",");
        String area = address.substring(index + 1).trim();
        if (area.contains("Thành phố"))
            area = area.replace("Thành phố", "TP.");
        return area;
    }

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, Object> hashMap = PostActivity.getAllData();
        postModel = new PostModel();
        String postId = (String) PostActivity.getData(PostActivity.KEY_POST_ID);
        if (postId == null) {
            postId = "PO" + System.currentTimeMillis();
        }
        postModel.setPoster(usersModel.getUid());
        postModel.setPostId(postId);
        postModel.setArea(area);
        postModel.setLatitude(usersModel.getLatitude());
        postModel.setLongitude(usersModel.getLongitude());
        for (Map.Entry<String, Object> data : hashMap.entrySet()) {
            switch (data.getKey()) {
                case PostActivity.KEY_PET_TYPE:
                    postModel.setPeType((String) data.getValue());
                    break;
                case PostActivity.KEY_POST_TYPE:
                    // cần bán -> bán
                    postModel.setPoType((String) data.getValue());
                    String poType = "[" + ((String) data.getValue()).split(" ")[1] + "]";
                    tvPostType.setText(poType);
                    break;
                case PostActivity.KEY_BREEDS:
                    postModel.setBreed((String) data.getValue());
                    tvBreed.setText((String) data.getValue());
                    break;
                case PostActivity.KEY_TITLE:
                    postModel.setTitle((String) data.getValue());
                    tvTitle.setText((String) data.getValue());
                    break;
                case PostActivity.KEY_PRICE:
                    String price = (String) data.getValue();
                    postModel.setPrice(Long.parseLong(price));
                    tvPrice.setText(Utils.formatCurrencyVN(Double.parseDouble(price)));
                    break;
                case PostActivity.KEY_DURATION:
                    String duration = (String) data.getValue();
                    postModel.setLimitDay(Long.parseLong(duration.split(" ")[0]));
                    break;
                case PostActivity.KEY_GENDER:
                    String gender = (String) data.getValue();
                    postModel.setGender(gender);
                    tvGender.setText(gender);
                    break;
                case PostActivity.KEY_INJECT:
                    String inject = (String) data.getValue();
                    postModel.setInjectStatus(inject);
                    tvInject.setText(inject);
                    break;
                case PostActivity.KEY_HEALTH:
                    String health = (String) data.getValue();
                    postModel.setHealthGuarantee(health);
                    tvHealthy.setText(health);
                    break;
                case PostActivity.KEY_PE_AGE:
                    String peAge = (String) data.getValue();
                    postModel.setPeAge(peAge);
                    tvAge.setText(peAge);
                    break;
            }
        }
        HashMap<String, Bitmap> dataMap = PostActivity.getAllImages();
        List<Bitmap> bitmaps = new ArrayList<>(dataMap.values());
        AtomicInteger i = new AtomicInteger();
        List<String> images = new ArrayList<>();
        mapImage = new HashMap<>();
        String finalPostId = postId;
        new ArrayList<>(dataMap.values()).forEach(bitmap -> {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            String id = finalPostId + i.getAndIncrement() + ".jpg";
            images.add("images/" + id);
            mapImage.put(id, baos.toByteArray());

        });
        postModel.setImages(images);
        SliderAdapter sliderAdapter = new SliderAdapter();
        sliderAdapter.setBitmapList(bitmaps);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.setScrollTimeInSec(2);
        sliderView.startAutoCycle();
    }
}