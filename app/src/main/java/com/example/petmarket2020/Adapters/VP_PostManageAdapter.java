package com.example.petmarket2020.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.petmarket2020.Views.Fragments.HiddenFragment;
import com.example.petmarket2020.Views.Fragments.RefusedFragment;
import com.example.petmarket2020.Views.Fragments.SellingFragment;
import com.example.petmarket2020.Views.Fragments.WaitingFragment;

public class VP_PostManageAdapter extends FragmentStateAdapter {

    public VP_PostManageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SellingFragment();
            case 1:
                return new HiddenFragment();
            case 2:
                return new RefusedFragment();
            default:
                return new WaitingFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
