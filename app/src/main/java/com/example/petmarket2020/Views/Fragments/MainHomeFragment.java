package com.example.petmarket2020.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.petmarket2020.Adapters.RV_PetCategoryAdapter;
import com.example.petmarket2020.Adapters.RV_PosterAdapter;
import com.example.petmarket2020.Adapters.items.PetCategoryItem;
import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainHomeFragment extends Fragment {
    private Context context;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();
        getWidget(view);
        setListener();
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
    }

    private void setListener() {
        //-- Top
        //  ++ Search
        svHome.setOnQueryTextFocusChangeListener((v, hasFocus) -> ivBack.setVisibility(View.VISIBLE));
        ivBack.setOnClickListener(v -> {
            svHome.clearFocus();
            Utils.HiddenKeyboard(getActivity());
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
        //  +++ Category
        List<PetCategoryItem> itemHomePetCategoryList = new ArrayList<>();
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Chó Alaska"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.dog2, "Chó Pitpull"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Chó Husky"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.dog1, "Chó Phú Quốc"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Chó Campuchia"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo mướp"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo bí"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo mun"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo Anh"));
        itemHomePetCategoryList.add(new PetCategoryItem(R.drawable.cat1, "Mèo Lào"));
        RV_PetCategoryAdapter homeRVPetCategoryAdapter = new RV_PetCategoryAdapter(context, itemHomePetCategoryList);
        rvCategoryDog.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        rvCategoryDog.setHasFixedSize(true);
        rvCategoryDog.setAdapter(homeRVPetCategoryAdapter);

        rvCategoryCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        rvCategoryCat.setHasFixedSize(true);
        rvCategoryCat.setAdapter(homeRVPetCategoryAdapter);
        //  ++ Poster
        List<PosterItem> itemHomePosterList = new ArrayList<>();
        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 23000000, "TP. Hồ Chí Minh", "06/11/2020"));
        itemHomePosterList.add(new PosterItem(R.drawable.dog1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 45000000, "Bình Định", "06/11/2020"));
        itemHomePosterList.add(new PosterItem(R.drawable.dog2, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 244000000, "Hà Nậu", "06/11/2020"));
        itemHomePosterList.add(new PosterItem(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 69000000, "Campuchia", "06/11/2020"));
//        itemHomePosterList.add(new Item_Home_Poster(R.drawable.dog1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 42000000, "Quận Cam", "06/11/2020"));
//        itemHomePosterList.add(new Item_Home_Poster(R.drawable.cat1, "Mèo nhân sư, mèo không lông chân lùn tai xoăn đực", 425000000, "TP. Hồ Chí Minh", "06/11/2020"));
        RV_PosterAdapter homeRVPosterAdapter = new RV_PosterAdapter(context, itemHomePosterList);
        rvPoster.setLayoutManager(new GridLayoutManager(context, 2));
        rvPoster.setAdapter(homeRVPosterAdapter);
        //-- Bottom

    }
}
