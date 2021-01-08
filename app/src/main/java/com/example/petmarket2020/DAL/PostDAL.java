package com.example.petmarket2020.DAL;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.petmarket2020.Interfaces.IPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class PostDAL {
    private DatabaseReference mRef;

    //    private final StorageReference mStorageRef;
    public void setRef(String node) {
        mRef = FirebaseDatabase.getInstance().getReference(node);
    }

    public void getPetBreeds(String type, IPost iPost) {
        mRef.child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<Integer, String> dataResp = new LinkedHashMap<>();
                    Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                    for (DataSnapshot dataSnapshot : dataSnapshotIterable) {
                        dataResp.put(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getKey()))
                                , (String) dataSnapshot.getValue());
                    }
                    iPost.sendData(dataResp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
