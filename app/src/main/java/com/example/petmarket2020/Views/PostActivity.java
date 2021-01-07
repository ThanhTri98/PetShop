package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petmarket2020.Adapters.VP_PostAdapter;
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private static List<String> listTitle;
    private TextView tvTitle;
    private MyViewPager vpg;
    private ImageView ivBack;

    @SuppressLint("StaticFieldLeak")
    private static PostController postController;
    private static HashMap<String, Object> dataMap;
    public static final String KEY_LOADED = "0", KEY_POST_TYPE = "1", KEY_PET_TYPE = "2", KEY_BREEDS = "3", KEY_TITLE = "4", KEY_DURATION_DATE = "5", KEY_INFO = "6", KEY_AVATARS = "7", KEY_CONTACT = "8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWidget();
        setUpViewPager();
        setListener();
        initListTitle();
        dataMap = new HashMap<>();
        postController = new PostController(this);
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
        listTitle.add("Thông tin liên hệ");
        listTitle.add("Xem trước bài đăng");

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

    public static void addData(String key, Object data) {
        if (!dataMap.containsValue(data))
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
        if (dataMap.containsKey(key))
            return dataMap.get(key);
        return null;
    }

    public static HashMap<String, Object> getAllData() {
        return dataMap;
    }
}