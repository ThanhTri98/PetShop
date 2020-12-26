package com.example.petmarket2020.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petmarket2020.Views.Fragments.MainHomeFragment;
import com.example.petmarket2020.Views.Fragments.MainMoreFragment;

public class VP_MainAdapter extends FragmentStatePagerAdapter {

    public VP_MainAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainHomeFragment();
            case 1:
            case 2:
            default:
                return new MainMoreFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
