package com.example.petmarket2020.Views.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainHomeFragment extends Fragment {
    //-- Top
    //  ++ Search
    SearchView svHome;
    ImageView ivBack;
    //  ++ Slider
    ImageSlider imgSlider;
    //-- Middle
    //  ++ Category
    RecyclerView rvCategoryDog;
    RecyclerView rvCategoryCat;
    //  ++ Poster
    RecyclerView rvPoster;
    RecyclerView rvHot;

    private RelativeLayout rlBar;
    private PostController postController;
    private NestedScrollView nsv;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postController = new PostController(getActivity());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getWidget(view);
        setListener();
        postController.getPostHome(rvPoster, rlBar, nsv, rvHot);
        postController.getPetType(rvCategoryDog, rvCategoryCat);
        return view;
    }

    private void getWidget(View v) {
        //-- Top
        //  ++ Search
        svHome = v.findViewById(R.id.svHome);
        ivBack = v.findViewById(R.id.ivBack);
        //  ++ Slider
        imgSlider = v.findViewById(R.id.imgSlider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner4, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner5, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner6, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner7, ScaleTypes.CENTER_CROP));
        imgSlider.setImageList(slideModels);
        //-- Middle
        //  ++ Category
        rvCategoryDog = v.findViewById(R.id.rvCategoryDog);
        rvCategoryCat = v.findViewById(R.id.rvCategoryCat);
        //  ++ Poster
        rvPoster = v.findViewById(R.id.rvPoster);
        rvHot = v.findViewById(R.id.rvHot);
        rlBar = v.findViewById(R.id.rlBar);
        nsv = v.findViewById(R.id.nsv);
        swipeRefreshLayout = v.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);


    }

    private void setListener() {
        //-- Top
        //  ++ Search
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            postController.refreshData();
            postController.getPostHome(rvPoster, rlBar, nsv, rvHot);
        }, 500));
        svHome.setOnQueryTextFocusChangeListener((v, hasFocus) -> ivBack.setVisibility(View.VISIBLE));
        ivBack.setOnClickListener(v -> {
            svHome.clearFocus();
            Utils.hiddenKeyboard(Objects.requireNonNull(getActivity()));
            v.setVisibility(View.GONE);
        });
        //  ++ Slider

        //-- Middle
        //  ++ Category
        //  ++ Poster
    }


}
