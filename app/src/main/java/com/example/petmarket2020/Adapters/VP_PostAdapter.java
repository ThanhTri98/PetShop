package com.example.petmarket2020.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petmarket2020.Views.Fragments.AddImageFragment;
import com.example.petmarket2020.Views.Fragments.AdditionalInforFragment;
import com.example.petmarket2020.Views.Fragments.BreedsFragment;
import com.example.petmarket2020.Views.Fragments.ContactFragment;
import com.example.petmarket2020.Views.Fragments.DurationDateFragment;
import com.example.petmarket2020.Views.Fragments.PetTypeFragment;
import com.example.petmarket2020.Views.Fragments.PostTypeFragment;
import com.example.petmarket2020.Views.Fragments.TitleFragment;
import com.example.petmarket2020.Views.Fragments.ViewPostFragment;
import com.example.petmarket2020.Views.PostActivity;

public class VP_PostAdapter extends FragmentStatePagerAdapter {

    public VP_PostAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PostTypeFragment();
            case 1:
                return new PetTypeFragment();
            case 2:
                return new BreedsFragment();
            case 3:
                return new TitleFragment();
            case 4:
                return new DurationDateFragment();
            case 5:
                return new AdditionalInforFragment();
            case 6:
                return new AddImageFragment();
            case 7:
                return new ContactFragment();
            default:
                return new ViewPostFragment();
        }
    }

    @Override
    public int getCount() {
        return 9;
    }
}
