package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petmarket2020.Adapters.VP_MainAdapter;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {
    private MyViewPager vpg;
    //-- Bottom
    private BottomNavigationView botNav;
    private FloatingActionButton fabPost;


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
            if (new SessionManager(HomeActivity.this).isLogin()) {
                startActivity(new Intent(HomeActivity.this, PostActivity.class));
            } else {
                showSBMargin(findViewById(R.id.cdl));
            }
        });
    }

    private void showSBMargin(View v) {
        Snackbar sb = Snackbar.make(v, "Đăng nhập để tiếp tục đăng tin", Snackbar.LENGTH_LONG)
                .setAction("ĐĂNG NHẬP", v1 -> {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                });
        sb.setActionTextColor(Color.CYAN);
        sb.setAnchorView(findViewById(R.id.vTmp));
        sb.show();
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