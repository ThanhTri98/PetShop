package com.example.petmarket2020.DAL;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.NotificationsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotiDAL {
    private final DatabaseReference mRef;

    public NotiDAL() {
        this.mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void getNoti(String uId, IControlData iControlData) {
        mRef.child(NodeRootDB.TRANS_NOTI).child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<NotificationsModel> notificationsModelList = new ArrayList<>();
                snapshot.getChildren().forEach(dataSnapshot -> {
                    Log.e("adaadada", dataSnapshot.toString());
                    NotificationsModel notificationsModel = dataSnapshot.getValue(NotificationsModel.class);
                    if (notificationsModel != null) notificationsModelList.add(notificationsModel);
                });
                Collections.reverse(notificationsModelList);
                iControlData.responseData(notificationsModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getInfoAdd(String postId, IControlData iControlData) {
        mRef.child(NodeRootDB.POST).child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String peType = (String) snapshot.child("peType").getValue();
                long price = Long.parseLong(String.valueOf(snapshot.child("price").getValue()));
                Object[] rs = new Object[]{peType, price};
                iControlData.responseData(rs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateIsCheck(String uId, long notiId) {
        mRef.child(NodeRootDB.TRANS_NOTI).child(uId).child(notiId + "").child("isChecked").setValue(true);
    }
}
