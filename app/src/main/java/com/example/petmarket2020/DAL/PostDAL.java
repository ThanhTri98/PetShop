package com.example.petmarket2020.DAL;

import androidx.annotation.NonNull;

import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Interfaces.IPost;
import com.example.petmarket2020.Models.PetTypeModel;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.Models.RankingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostDAL {
    private DatabaseReference mRef;
    private StorageReference mStorageRef;

    public PostDAL() {
        if (mRef == null)
            mRef = FirebaseDatabase.getInstance().getReference();
        if (mStorageRef == null)
            mStorageRef = FirebaseStorage.getInstance().getReference(NodeRootDB.STORAGE_IMAGES);
    }

    public void getPetBreeds(String type, IPost iPost) {
        mRef.child(NodeRootDB.PET_TYPE).child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<Integer, String> dataResp = new LinkedHashMap<>();
                    Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                    for (DataSnapshot dataSnapshot : dataSnapshotIterable) {
                        dataResp.put(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getKey()))
                                , (String) dataSnapshot.child("name").getValue());
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
        mRef.child(NodeRootDB.POST).child(postModel.getPostId()).setValue(postModel)
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

    private List<DataSnapshot> listNormal;
    private List<DataSnapshot> listHot;
    private List<String> listKeysHot;

    public void getPostHome(int typeRequest, IPost iPost) {
        if (listNormal != null && listHot != null && listKeysHot != null) {
            processPostDownload(typeRequest, iPost);
        } else {
            mRef.child(NodeRootDB.POST).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> snapshots = snapshot.getChildren();
                    listNormal = new ArrayList<>();
                    snapshots.forEach(listNormal::add);
                    Collections.reverse(listNormal);
                    processTypePostList(typeRequest, iPost);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void processTypePostList(int typeRequest, IPost iPost) {
        mRef.child(NodeRootDB.HOT_POST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listKeysHot = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    listKeysHot.add(ds.getKey());
                }
                listHot = new ArrayList<>();
                listNormal.forEach(dataSnapshot -> {
                    if (listKeysHot.contains(dataSnapshot.getKey()))
                        listHot.add(dataSnapshot);
                });
                processPostDownload(typeRequest, iPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void processPostDownload(int typeRequest, IPost iPost) {
//        typeRequest = 1 normalList, = 2 hotList, = 0 cả 2 :_)
        if (typeRequest == 0) { //
            List<PosterItem> itemsNormal = new ArrayList<>();
            while (!this.listNormal.isEmpty() && itemsNormal.size() < 4) {
                DataSnapshot ds = this.listNormal.remove(0);
                PosterItem posterItem = ds.getValue(PosterItem.class);
                if (posterItem != null) {
                    if (listKeysHot.contains(posterItem.getPostId()))
                        posterItem.setHot(true);
                    itemsNormal.add(posterItem);
                }
            }
            List<PosterItem> itemsHot = new ArrayList<>();
            while (!this.listHot.isEmpty() && itemsHot.size() < 4) {
                DataSnapshot ds = this.listHot.remove(0);
                PosterItem posterItem = ds.getValue(PosterItem.class);
                if (posterItem != null) {
                    posterItem.setHot(true);
                    itemsHot.add(posterItem);
                }
            }
            iPost.sendData(itemsNormal, itemsHot);
        } else if (typeRequest == 1) {
            List<PosterItem> itemsNormal = new ArrayList<>();
            while (!this.listNormal.isEmpty() && itemsNormal.size() < 4) {
                DataSnapshot ds = this.listNormal.remove(0);
                PosterItem posterItem = ds.getValue(PosterItem.class);
                if (posterItem != null) {
                    if (listKeysHot.contains(posterItem.getPostId()))
                        posterItem.setHot(true);
                    itemsNormal.add(posterItem);
                }
            }
            iPost.sendData(itemsNormal);
        } else { // typeRequest=2
            List<PosterItem> itemsHot = new ArrayList<>();
            while (!this.listHot.isEmpty() && itemsHot.size() < 4) {
                DataSnapshot ds = this.listHot.remove(0);
                PosterItem posterItem = ds.getValue(PosterItem.class);
                if (posterItem != null) {
                    posterItem.setHot(true);
                    itemsHot.add(posterItem);
                }
            }
            iPost.sendData(itemsHot);
        }

    }

    public void postDetail(String postId, IPost iPost) {
        mRef.child(NodeRootDB.POST).child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Object> dataResult = new ArrayList<>();
                    PostModel postModel = snapshot.getValue(PostModel.class);
                    if (postModel != null) {
                        dataResult.add(postModel);
                        String poster = postModel.getPoster();
                        mRef.child(NodeRootDB.USERS).child(poster).child("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void updateViewsCount(String postId, long views) {
        mRef.child(NodeRootDB.POST).child(postId).child("viewCounts")
                .setValue(views);
    }

    private Iterable<DataSnapshot> samePostList;

    public void getSamePosts(String peType, long price, IPost iPost) {
        if (samePostList == null) {
            mRef.child(NodeRootDB.POST).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    samePostList = snapshot.getChildren();
                    processSamePost(peType, price, iPost);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            processSamePost(peType, price, iPost);
        }
    }

    private void processSamePost(String peType, long price, IPost iPost) {
        long range = 500000; //500 ngan
        int i = 0;
        List<PosterItem> posterItems = new ArrayList<>();
        for (DataSnapshot data : this.samePostList) {
            String peType1 = Objects.requireNonNull(data.child("peType").getValue()).toString();
            long price1 = Long.parseLong(Objects.requireNonNull(data.child("price").getValue()).toString());
            if (peType1.equals(peType) && price1 <= price + range && price - range <= price1) {
                PosterItem posterItem = data.getValue(PosterItem.class);
                if (posterItem != null) {
                    posterItems.add(data.getValue(PosterItem.class));
                    i++;
                    if (i == 4) break;
                }
            }
        }
        iPost.sendData(posterItems);
    }

    public void getPetType(IPost iPost) {
        mRef.child(NodeRootDB.PET_TYPE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<PetTypeModel> catList = new ArrayList<>();
                List<PetTypeModel> dogList = new ArrayList<>();
                Iterable<DataSnapshot> snapshotCat = snapshot.child("cat").getChildren();
                Iterable<DataSnapshot> snapshotDog = snapshot.child("dog").getChildren();
                snapshotCat.forEach(dataSnapshot -> {
                    PetTypeModel petTypeModel = dataSnapshot.getValue(PetTypeModel.class);
                    if (petTypeModel != null) {
                        catList.add(petTypeModel);
                    }
                });
                snapshotDog.forEach(dataSnapshot -> {
                    PetTypeModel petTypeModel = dataSnapshot.getValue(PetTypeModel.class);
                    if (petTypeModel != null) {
                        dogList.add(petTypeModel);
                    }
                });
                catList.remove(catList.size() - 1);
                dogList.remove(dogList.size() - 1);
                Object[] objRs = new Object[]{catList, dogList};
                iPost.sendData(objRs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void rankingProcess(String postId, String comment, int rate, String userId, IControlData iControlData) {
        RankingModel rankingModel = new RankingModel(postId, userId, rate, Utils.getCurrentDate(true), comment);
        mRef.child(NodeRootDB.RANKINGS).child(postId).child(userId).setValue(rankingModel).addOnCompleteListener(task -> {
            iControlData.isSuccessful(task.isSuccessful());
        });
    }
}
