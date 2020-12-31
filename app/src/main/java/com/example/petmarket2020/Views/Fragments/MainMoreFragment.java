package com.example.petmarket2020.Views.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.Users;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.LoginActivity;
import com.example.petmarket2020.Views.ProfileActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class MainMoreFragment extends Fragment implements View.OnClickListener {
    private ShimmerFrameLayout sflName, sflAvatar;
    private TextView tvAction, tvViewProfile, tvMyCoins;
    private ImageView imgAvatar;
    private LinearLayout llProfile, llCoins, llLogout;
    private SessionManager sessionManager;

    private static Users users;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        getWidget(view);
        setListener();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sessionManager.isLogin()) {
                updateUI(true);
                new MyAsyncTask().execute();
        }
    }

    private void updateUI(boolean is) {
        if (is) {
            llCoins.setVisibility(View.VISIBLE);
            tvMyCoins.setVisibility(View.GONE);
            tvViewProfile.setVisibility(View.VISIBLE);
        } else {
            llCoins.setVisibility(View.GONE);
            llLogout.setVisibility(View.GONE);
            tvMyCoins.setVisibility(View.VISIBLE);
            tvViewProfile.setVisibility(View.GONE);
            tvAction.setText(R.string.MAction);
            imgAvatar.setImageResource(R.drawable.ic_login_user);
        }
    }

    private void getWidget(View v) {
        sflName = v.findViewById(R.id.sflName);
        sflAvatar = v.findViewById(R.id.sflAvatar);

        imgAvatar = v.findViewById(R.id.imgAvatar);

        tvAction = v.findViewById(R.id.tvAction);
        tvViewProfile = v.findViewById(R.id.tvViewProfile);
        tvMyCoins = v.findViewById(R.id.tvMyCoins);

        llCoins = v.findViewById(R.id.llCoins);
        llProfile = v.findViewById(R.id.llProfile);
        llLogout = v.findViewById(R.id.llLogout);
    }

    private void setListener() {
        llProfile.setOnClickListener(this);
        llLogout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llProfile:
                if (users != null) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra(NodeRootDB.USERS, users);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.llLogout:
                updateUI(false);
                users = null;
                sessionManager.logOut();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<Void, Object, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imgAvatar.setVisibility(View.GONE);
            sflAvatar.setVisibility(View.VISIBLE);
            sflAvatar.startShimmer();

            tvAction.setVisibility(View.GONE);
            sflName.setVisibility(View.VISIBLE);
            sflName.startShimmer();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            users = sessionManager.getUserDetail();
            publishProgress(users.getFullName(), users.getAvatar());
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... objects) {
            super.onProgressUpdate(objects);
            tvAction.setText((String) objects[0]);
            if (objects[1] == null) {
                imgAvatar.setImageResource(R.drawable.ic_login_user);
            } else {
                Glide.with(MainMoreFragment.this).load(FirebaseStorage.getInstance().getReference(objects[1].toString())).into(imgAvatar);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sflAvatar.stopShimmer();
            sflAvatar.setVisibility(View.GONE);
            imgAvatar.setVisibility(View.VISIBLE);

            sflName.stopShimmer();
            sflName.setVisibility(View.GONE);
            tvAction.setVisibility(View.VISIBLE);
            llLogout.setVisibility(View.VISIBLE);

        }
    }
}
