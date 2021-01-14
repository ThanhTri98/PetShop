package com.example.petmarket2020.DAL;

import androidx.annotation.NonNull;

import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.CoinsModel;
import com.example.petmarket2020.Models.TransactionHistory;
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
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void getCoinsPackage(IControlData iControlData) {
        mRef.child(NodeRootDB.COINS_PACKAGE).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void payProcess(String uId, long coins, String payments, boolean isSuccess, IControlData iControlData) {
        String id = String.valueOf(System.currentTimeMillis());
        TransactionHistory transactionHistory = new TransactionHistory(coins, payments, isSuccess ? "Thành công" : "Thất bại");
        mRef.child(NodeRootDB.TRANS_HIS).child(uId).child(id).setValue(transactionHistory);
        if (isSuccess) {
            mRef.child(NodeRootDB.USERS).child(uId).child("coins").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long currentCoins = (long) snapshot.getValue();
                    long coinsUpdate = currentCoins + coins;
                    mRef.child(NodeRootDB.USERS).child(uId).child("coins").setValue(coinsUpdate)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    iControlData.responseData(coinsUpdate);
                                } else {
                                    iControlData.responseData(null);
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else {
            iControlData.responseData(null);
        }
    }
}
