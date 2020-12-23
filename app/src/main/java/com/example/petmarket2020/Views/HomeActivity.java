package com.example.petmarket2020.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
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
    //-- Bottom
    BottomNavigationView botNav;
    FloatingActionButton fabPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWidget();
        setListerner();
        initNavBottom();
        initData();
    }

    private void getWidget() {
        //-- Top
        //  ++ Search
        svHome = findViewById(R.id.svHome);
        ivBack = findViewById(R.id.ivBack);
        //  ++ Slider
        imgSlider = findViewById(R.id.imgSlider);
        //-- Middle
        //  ++ Category
        rvCategoryDog = findViewById(R.id.rvCategoryDog);
        rvCategoryCat = findViewById(R.id.rvCategoryCat);
        //  ++ Poster
        rvPoster = findViewById(R.id.rvPoster);
        //-- Bottom
        // FloatingActionButton
        fabPost = findViewById(R.id.fabPost);
        // BottomNavigationView
        botNav = findViewById(R.id.botNav);
        initNavBottom();
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
        RV_PetCategoryAdapter homeRVPetCategoryAdapter = new RV_PetCategoryAdapter(this, itemHomePetCategoryList);
        rvCategoryDog.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCategoryDog.setHasFixedSize(true);
        rvCategoryDog.setAdapter(homeRVPetCategoryAdapter);

        rvCategoryCat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
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
        RV_PosterAdapter homeRVPosterAdapter = new RV_PosterAdapter(this, itemHomePosterList);
        rvPoster.setLayoutManager(new GridLayoutManager(this, 2));
        rvPoster.setAdapter(homeRVPosterAdapter);
        //-- Bottom

    }

    private void setListerner() {
        //-- Top
        //  ++ Search
        svHome.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ivBack.setVisibility(View.VISIBLE);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svHome.clearFocus();
                Utils.HiddenKeyboard(HomeActivity.this);
                v.setVisibility(View.GONE);
            }
        });
        //  ++ Slider

        //-- Middle
        //  ++ Category
        //  ++ Poster
        //-- Bottom
        fabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initNavBottom() {
        botNav.setBackground(null);
        botNav.getMenu().getItem(2).setEnabled(false);
        float radius = 100f;
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);

        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) bottomAppBar.getBackground();
        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, radius)
                        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                        .build());
    }

}