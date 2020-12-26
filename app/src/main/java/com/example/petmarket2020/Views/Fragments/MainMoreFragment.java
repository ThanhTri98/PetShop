package com.example.petmarket2020.Views.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.Users;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.LoginActivity;
import com.example.petmarket2020.Views.ProfileActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

public class MainMoreFragment extends Fragment implements View.OnClickListener {
    private ShimmerFrameLayout sfl;
    private TextView tvAction, tvViewProfile, tvMyCoins;
    //    private ImageView imgAvatar;
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
        new MyAsyncTask().execute();
    }

    private void getWidget(View v) {
        sfl = v.findViewById(R.id.sfl);

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
                if (!sessionManager.checkLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("Users", users);
                    startActivity(intent);
                }
                break;
            case R.id.llLogout:
                sessionManager.logOut();
                new MyAsyncTask().execute();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<Void, String, Void> {
        private boolean isLogin;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (users != null) {
                tvAction.setText(users.getFullName());
            } else {
                tvAction.setVisibility(View.GONE);
                sfl.setVisibility(View.VISIBLE);
                sfl.startShimmer();
            }
            isLogin = sessionManager.checkLogin();
            if (isLogin) {
                llCoins.setVisibility(View.VISIBLE);
                llLogout.setVisibility(View.VISIBLE);
                tvMyCoins.setVisibility(View.GONE);
                tvViewProfile.setVisibility(View.VISIBLE);
            } else {
                llCoins.setVisibility(View.GONE);
                llLogout.setVisibility(View.GONE);
                tvMyCoins.setVisibility(View.VISIBLE);
                tvViewProfile.setVisibility(View.GONE);
                tvAction.setText(R.string.MAction);
            }
        }

        @Override
        protected Void doInBackground(Void... values) {
            if (isLogin) {
                users = sessionManager.getUserDetail();
                publishProgress(sessionManager.getUserDetail().getFullName());
            } else {
                users = null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... strings) {
            super.onProgressUpdate(strings);
            if (users != null)
                tvAction.setText(users.getFullName());

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sfl.stopShimmer();
            sfl.setVisibility(View.GONE);
            tvAction.setVisibility(View.VISIBLE);
        }
    }
}
