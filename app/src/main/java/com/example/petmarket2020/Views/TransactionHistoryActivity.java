package com.example.petmarket2020.Views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.CoinsController;
import com.example.petmarket2020.R;

public class TransactionHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        CoinsController coinsController = new CoinsController(this);
        RecyclerView rvTrans = findViewById(R.id.rvTrans);
        rvTrans.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvTrans.setHasFixedSize(true);
        coinsController.getTransaction(rvTrans);
    }
}