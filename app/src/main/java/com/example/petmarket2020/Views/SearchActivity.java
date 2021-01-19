package com.example.petmarket2020.Views;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.R;

public class SearchActivity extends AppCompatActivity {
    private LinearLayout llFilter;
    private LinearLayout llSearch;
    private AutoCompleteTextView actvPet, actvArea;
    private SearchView svHome;
    private RecyclerView rvSearch;
    private PostController postController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWidget();
        String[] a1 = new String[]{"Tất cả", "Gần bạn"};
        String[] a2 = new String[]{"Chó", "Mèo"};
        String breed = getIntent().getStringExtra("breed");
        actvArea.setText(a1[0]);
        actvPet.setText(a2[0]);
        postController = new PostController(this);
        actvArea.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, a1));
        actvPet.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, a2));
        llSearch.setOnClickListener(v -> {
            postController.refreshData();
            postController.getPostFilter(rvSearch, actvArea.getText().toString(), actvPet.getText().toString(), null);
        });
        Log.e("BREDDd",breed);
        postController.getPostFilter(rvSearch, "", "", breed);

    }

    private void getWidget() {
        llFilter = findViewById(R.id.llFilter);
        actvPet = findViewById(R.id.actvPet);
        actvArea = findViewById(R.id.actvArea);
        svHome = findViewById(R.id.svHome);
        rvSearch = findViewById(R.id.rvSearch);
        llFilter = findViewById(R.id.llFilter);
        llSearch = findViewById(R.id.llSearch);
    }
}