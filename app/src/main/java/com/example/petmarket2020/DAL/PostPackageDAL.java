package com.example.petmarket2020.DAL;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.HotPotModel;
import com.example.petmarket2020.Models.PostPackageModel;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.UsersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostPackageDAL {
    private DatabaseReference mRef;
    private SessionManager sessionManager;

    public PostPackageDAL(Activity activity) {
        if (mRef == null)
            mRef = FirebaseDatabase.getInstance().getReference();
        if (sessionManager == null) sessionManager = new SessionManager(activity);
    }

    public void getPostPackage(IControlData iControlData) {
        mRef.child(NodeRootDB.POST_PACKAGE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<PostPackageModel> postPackageModels = new ArrayList<>();
                snapshot.getChildren()
                        .forEach(dataSnapshot -> postPackageModels.add(dataSnapshot.getValue(PostPackageModel.class)));
                iControlData.responseData(postPackageModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean checkMoney(double price) {
        UsersModel usersModel = sessionManager.getUserSession();
        if (usersModel == null) return false;
        return usersModel.getCoins() >= price;
    }

    public void paymentProcess(double totalPrice, String postId, PostPackageModel postPackageModel, int day, IControlData iControlData) {
        UsersModel usersModel = sessionManager.getUserSession();
        if (usersModel != null) {
            long currentPrice = usersModel.getCoins();
            long newPrice = (long) (currentPrice - totalPrice);
            mRef.child(NodeRootDB.USERS).child(usersModel.getUid()).child("coins")
                    .setValue(newPrice).addOnCompleteListener(task -> {
                HotPotModel hotPotModel = new HotPotModel();
                hotPotModel.setDays(day);
                hotPotModel.setPkgId(postPackageModel.getPkgId());
                hotPotModel.setPostId(postId);
                hotPotModel.setStartTime(Utils.getCurrentDate(true));
                mRef.child(NodeRootDB.HOT_POST).child(postId).setValue(hotPotModel);
                iControlData.isSuccessful(task.isSuccessful());
            });
        }
    }
}
