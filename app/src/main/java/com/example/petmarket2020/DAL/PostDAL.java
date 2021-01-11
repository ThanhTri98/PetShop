package com.example.petmarket2020.DAL;

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

    private Iterable<DataSnapshot> mSnapshots;

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
        if (mSnapshots != null) {
            processPostDownload(iPost, mSnapshots);
        } else {
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mSnapshots = snapshot.getChildren();
                    processPostDownload(iPost, mSnapshots);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void processPostDownload(IPost iPost, Iterable<DataSnapshot> dataSnapshots) {
        int i = 0;
        List<PosterItem> posterItems = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshots) {
            PosterItem posterItem = ds.getValue(PosterItem.class);
            // ds.getValue sẽ xóa item đã get ra khỏi Iterable<DataSnapshot>
            assert posterItem != null;
            posterItems.add(posterItem);
            i++;
            if (i == 4) break;
        }
        iPost.sendData(posterItems);
    }

    public void postDetail(String postId, IPost iPost) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(NodeRootDB.POST).child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Object> dataResult = new ArrayList<>();
                    PostModel postModel = snapshot.getValue(PostModel.class);
                    if (postModel != null) {
                        dataResult.add(postModel);
                        String poster = postModel.getPoster();
                        reference.child(NodeRootDB.USERS).child(poster).child("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                    dataResult.add(snapshot.getValue());
                                iPost.sendData(dataResult);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
