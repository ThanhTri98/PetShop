package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petmarket2020.Adapters.VP_PostAdapter;
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.Fragments.AddImageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PostActivity extends AppCompatActivity {
    private static List<String> listTitle;
    private TextView tvTitle;
    private MyViewPager vpg;
    private ImageView ivBack;
    private PostModel postModel;
    @SuppressLint("StaticFieldLeak")
    private static PostController postController;
    private static HashMap<String, Object> dataMap;
    private static HashMap<String, Bitmap> picturesMap;
    private static Set<String> imageRemoved;
    public static final String KEY_LOADED = "0", KEY_POST_TYPE = "1", KEY_PET_TYPE = "2", KEY_BREEDS = "3",
            KEY_TITLE = "4", KEY_PRICE = "5", KEY_DURATION = "6", KEY_GENDER = "7", KEY_INJECT = "8",
            KEY_HEALTH = "9", KEY_PE_AGE = "10", KEY_OLD_IMAGE = "11", KEY_POST_ID = "12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWidget();
        setUpViewPager();
        setListener();
        initListTitle();
        imageRemoved = new HashSet<>();
        dataMap = new HashMap<>();
        picturesMap = new HashMap<>();
        postController = new PostController(this);
        postModel = (PostModel) getIntent().getSerializableExtra("postModel");
        if (postModel != null) initDataPost();
    }

    private void initDataPost() {
        dataMap.put(KEY_POST_ID, postModel.getPostId());
        dataMap.put(KEY_POST_TYPE, postModel.getPoType());
        dataMap.put(KEY_PET_TYPE, postModel.getPeType());
        dataMap.put(KEY_BREEDS, postModel.getBreed());
        dataMap.put(KEY_TITLE, postModel.getTitle());
        dataMap.put(KEY_PRICE, postModel.getPrice());
        dataMap.put(KEY_DURATION, postModel.getLimitDay());
        dataMap.put(KEY_GENDER, postModel.getGender());
        dataMap.put(KEY_INJECT, postModel.getInjectStatus());
        dataMap.put(KEY_HEALTH, postModel.getHealthGuarantee());
        dataMap.put(KEY_PE_AGE, postModel.getPeAge());
        dataMap.put(KEY_OLD_IMAGE, postModel.getImages());
    }

    public static PostController getPostController() {
        return postController;
    }

    private void initListTitle() {
        listTitle = new ArrayList<>();
        listTitle.add("Bạn muốn đăng tin gì?");
        listTitle.add("Loại thú cưng");
        listTitle.add("Chọn giống?");
        listTitle.add("Tiêu đề");
        listTitle.add("Thời hạn");
        listTitle.add("Thông tin thêm");
        listTitle.add("Thêm hình ảnh");
        listTitle.add("Xem trước bài đăng");
        listTitle.add("Tin đăng thành công");

    }

    public static String getTitle(int position) {
        return listTitle.get(position);
    }


    private void getWidget() {
        vpg = findViewById(R.id.vpg);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setListener() {
        ivBack.setOnClickListener(v -> {
            int currentPage = vpg.getCurrentItem();
            if (currentPage > 0) {
                tvTitle.setText(listTitle.get(currentPage - 1));
                vpg.setCurrentItem(currentPage - 1);
            } else {
                finish();
            }
        });
    }

    private void setUpViewPager() {
        VP_PostAdapter VPPostAdapter = new VP_PostAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpg.setAdapter(VPPostAdapter);
    }

    public static void addOrUpdateData(String key, Object data) {
        dataMap.put(key, data);
    }

    public static boolean isLoaded() {
        return dataMap.containsKey(KEY_LOADED);
    }

    public static void setLoadingStatus(boolean is) {
        if (!is)
            dataMap.put(KEY_LOADED, 0);
        else
            dataMap.remove(KEY_LOADED);

    }

    public static Object getData(String key) {
        return dataMap.get(key);
    }

    public static HashMap<String, Object> getAllData() {
        return dataMap;
    }

    //Images
    public static void addImage(String key, Bitmap image) {
        picturesMap.put(key, image);
    }

    public static void removeImage(String key) {
        picturesMap.remove(key);
    }

    public static void addImageRemoved(String key) {
        imageRemoved.add(key);
    }

    public static Set<String> getImageRemoved() {
        return imageRemoved;
    }

    public static HashMap<String, Bitmap> getAllImages() {
        return picturesMap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            AddImageFragment addImageFragment = (AddImageFragment) Objects
                    .requireNonNull(vpg.getAdapter()).instantiateItem(vpg, vpg.getCurrentItem());
            addImageFragment.onActivityResult(requestCode, resultCode, data);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}