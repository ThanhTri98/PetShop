package com.example.petmarket2020.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petmarket2020.Views.Fragments.AddImageFragment;
import com.example.petmarket2020.Views.Fragments.AdditionalInfoFragment;
import com.example.petmarket2020.Views.Fragments.BreedsFragment;
import com.example.petmarket2020.Views.Fragments.CompleteFragment;
import com.example.petmarket2020.Views.Fragments.DurationDateFragment;
import com.example.petmarket2020.Views.Fragments.PetTypeFragment;
import com.example.petmarket2020.Views.Fragments.PostTypeFragment;
import com.example.petmarket2020.Views.Fragments.TitleFragment;
import com.example.petmarket2020.Views.Fragments.ViewPostFragment;

public class VP_PostAdapter extends FragmentStatePagerAdapter {

    public VP_PostAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

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
                return new AdditionalInfoFragment();
            case 6:
                return new AddImageFragment();
            case 7:
                return new ViewPostFragment();
            default:
                return new CompleteFragment();
        }
    }

    @Override
    public int getCount() {
        return 9;
    }
}
