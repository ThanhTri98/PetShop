package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.petmarket2020.Adapters.VP_MainAdapter;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Utils.MyViewPager;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

public class HomeActivity extends AppCompatActivity {
    private MyViewPager vpg;
    //-- Bottom
    BottomNavigationView botNav;
    FloatingActionButton fabPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWidget();
        setUpViewPager();
        setListener();
        initNavBottom();
    }

    private void setUpViewPager() {
        VP_MainAdapter vp_mainAdapter = new VP_MainAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpg.setAdapter(vp_mainAdapter);
    }

    private void getWidget() {
        vpg = findViewById(R.id.vpg);
        //-- Bottom
        // FloatingActionButton
        fabPost = findViewById(R.id.fabPost);
        // BottomNavigationView
        botNav = findViewById(R.id.botNav);
        initNavBottom();
    }


    @SuppressLint("NonConstantResourceId")
    private void setListener() {
        botNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mHome:
                    vpg.setCurrentItem(0);
                    break;
                case R.id.mMe:
                    vpg.setCurrentItem(1);
                    break;
                case R.id.mNoti:
                    vpg.setCurrentItem(2);
                    break;
                case R.id.mMore:
                    vpg.setCurrentItem(3);
                    break;
            }
            return true;
        });
        //-- Bottom
        fabPost.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PostActivity.class);
            startActivity(intent);
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