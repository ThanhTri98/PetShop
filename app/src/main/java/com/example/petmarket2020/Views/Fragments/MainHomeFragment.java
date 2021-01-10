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
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;

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
    private RelativeLayout rlBarHot;
    private PostController postController;
    private NestedScrollView nsv;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postController = new PostController(getActivity());
        postController.setNode(NodeRootDB.POST);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getWidget(view);
        setListener();
        postController.postDownload(rvPoster, rlBar, nsv, rvHot, rlBarHot);
//        initData();
        return view;
    }

    private void getWidget(View v) {
        //-- Top
        //  ++ Search
        svHome = v.findViewById(R.id.svHome);
        ivBack = v.findViewById(R.id.ivBack);
        //  ++ Slider
        imgSlider = v.findViewById(R.id.imgSlider);
        //-- Middle
        //  ++ Category
        rvCategoryDog = v.findViewById(R.id.rvCategoryDog);
        rvCategoryCat = v.findViewById(R.id.rvCategoryCat);
        //  ++ Poster
        rvPoster = v.findViewById(R.id.rvPoster);
        rvHot = v.findViewById(R.id.rvHot);
        rlBar = v.findViewById(R.id.rlBar);
        rlBarHot = v.findViewById(R.id.rlBarHot);
        nsv = v.findViewById(R.id.nsv);
        swipeRefreshLayout = v.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);


    }

    private void setListener() {
        //-- Top
        //  ++ Search
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            postController = new PostController(getActivity());
            postController.setNode(NodeRootDB.POST);
            postController.postDownload(rvPoster, rlBar, nsv, rvHot, rlBarHot);
        }, 2000));
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

    private void initData() {
        //-- Top
        //  +++ Search

        //  +++ Slider

//        List<SlideModel> slideModels = new ArrayList<>();
//        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner2, ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner3, ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner4, ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner5, ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner6, ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.banner7, ScaleTypes.CENTER_CROP));
//        imgSlider.setImageList(slideModels);
        //-- Middle
        //  +++ Category
//        List<PetCategoryItem> itemHomePetCategoryList = new ArrayList<>();
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Chó Alaska"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.dog2, "Chó Pitpull"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Chó Husky"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.dog1, "Chó Phú Quốc"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Chó Campuchia"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo mướp"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo bí"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo mun"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo Anh"));
//        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo Lào"));
//        RV_PetCategoryAdapter homeRVPetCategoryAdapter = new RV_PetCategoryAdapter(context, itemHomePetCategoryList);
//        rvCategoryDog.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
//        rvCategoryDog.setHasFixedSize(true);
//        rvCategoryDog.setAdapter(homeRVPetCategoryAdapter);
//
//        rvCategoryCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
//        rvCategoryCat.setHasFixedSize(true);
//        rvCategoryCat.setAdapter(homeRVPetCategoryAdapter);
        //  ++ Poster
//        List<PosterItem> itemHomePosterList = new ArrayList<>();
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 23000000, "TP. Hồ Chí Minh", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.dog1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 45000000, "Bình Định", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.dog2, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 244000000, "Hà Nậu", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 69000000, "Campuchia", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.dog1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 42000000, "Quận Cam", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 425000000, "TP. Hồ Chí Minh", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 425000000, "TP. Hồ Chí Minh", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 425000000, "TP. Hồ Chí Minh", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 425000000, "TP. Hồ Chí Minh", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 425000000, "TP. Hồ Chí Minh", "06/11/2020"));
//        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 425000000, "TP. Hồ Chí Minh", "06/11/2020"));
//        RV_PosterAdapter homeRVPosterAdapter = new RV_PosterAdapter(context, itemHomePosterList);
//        rvPoster.setLayoutManager(new GridLayoutManager(context, 2));
//        rvPoster.setAdapter(homeRVPosterAdapter);
        //-- Bottom
    }
}
