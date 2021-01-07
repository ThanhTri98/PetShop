package com.petmarkets2020.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petmarkets2020.model.Comments;

@Service
public class CommentServices {
	public static final String COL_NAME = "Comments";
	Comments comment = null;

	public Comments getComments(String userId) throws InterruptedException, ExecutionException {
		FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
		DatabaseReference databaseReference = firebaseDatabase.getReference(COL_NAME);
		databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
      
			@Override
			public void onDataChange(DataSnapshot snapshot) {
			comment = snapshot.getValue(Comments.class);
			System.out.println(comment.toString());
			}

			@Override
			public void onCancelled(DatabaseError error) {

			}
		});
//	
		System.out.println(databaseReference);
		return comment;
	}

}
