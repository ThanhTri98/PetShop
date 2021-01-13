package com.example.petmarket2020.Views.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.petmarket2020.Controllers.MainMoreController;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.CoinsActivity;
import com.example.petmarket2020.Views.LoginActivity;
import com.example.petmarket2020.Views.ProfileActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.storage.FirebaseStorage;

public class MainMoreFragment extends Fragment implements View.OnClickListener {
    private ShimmerFrameLayout sflName, sflAvatar;
    private TextView tvAction, tvViewProfile, tvMyCoins;
    private ImageView imgAvatar;
    private LinearLayout llProfile, llCoinsPoint, llLogout, llCoins;
    private static UsersModel usersModel;
    private MainMoreController mainMoreController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainMoreController = new MainMoreController(getActivity());
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
        if (mainMoreController.checkLogin()) {
            updateUI(true);
            new MyAsyncTask().execute();
        }
    }

    private void updateUI(boolean is) {
        if (is) {
            llCoinsPoint.setVisibility(View.VISIBLE);
            tvMyCoins.setVisibility(View.GONE);
            tvViewProfile.setVisibility(View.VISIBLE);
        } else {
            llCoinsPoint.setVisibility(View.GONE);
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

        llCoinsPoint = v.findViewById(R.id.llCoinsPoint);
        llCoins = v.findViewById(R.id.llCoins);
        llProfile = v.findViewById(R.id.llProfile);
        llLogout = v.findViewById(R.id.llLogout);
    }

    private void setListener() {
        llProfile.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        llCoins.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llProfile:
                if (usersModel != null) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra(NodeRootDB.USERS, usersModel);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.llLogout:
                updateUI(false);
                usersModel = null;
                mainMoreController.logout();
                break;
            case R.id.llCoins:
                startActivity(new Intent(getActivity(), CoinsActivity.class));
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
            usersModel = mainMoreController.getUserDetail();
            publishProgress(usersModel.getFullName(), usersModel.getAvatar());
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
