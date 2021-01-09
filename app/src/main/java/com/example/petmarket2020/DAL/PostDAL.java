package com.example.petmarket2020.DAL;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Interfaces.IPost;
import com.example.petmarket2020.Models.PostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostDAL {
    private DatabaseReference mRef;
    private StorageReference mStorageRef;

    public void setRef(String node) {
        mRef = FirebaseDatabase.getInstance().getReference(node);
        mStorageRef = FirebaseStorage.getInstance().getReference(NodeRootDB.STORAGE_IMAGES);
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

    public void postUpload(PostModel postModel, HashMap<String, byte[]> mapImage, IPost iPost) {
        mRef.child(postModel.getPostId()).setValue(postModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // upload image
                        iPost.isSuccessful(true);
                        mapImage.forEach((s, bytes) -> mStorageRef.child(s).putBytes(bytes));
                    } else {
                        iPost.isSuccessful(false);
                    }
                });
    }

    public void postDownload(IPost iPost) {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshots = snapshot.getChildren();
                List<PosterItem> posterItems = new ArrayList<>();
                for (DataSnapshot ds : snapshots) {
                    posterItems.add(ds.getValue(PosterItem.class));
                }
                iPost.sendData(posterItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
