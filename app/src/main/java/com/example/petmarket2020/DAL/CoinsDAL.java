package com.example.petmarket2020.DAL;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.CoinsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoinsDAL {
    private final DatabaseReference mRef;

    public CoinsDAL() {
        mRef = FirebaseDatabase.getInstance().getReference(NodeRootDB.COINS_PACKAGE);
    }

    public void getCoinsPackage(IControlData iControlData) {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CoinsModel> coinsModels = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CoinsModel coinsModel = dataSnapshot.getValue(CoinsModel.class);
                    if (coinsModel != null)
                        coinsModels.add(coinsModel);
                }
                iControlData.responseData(coinsModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
