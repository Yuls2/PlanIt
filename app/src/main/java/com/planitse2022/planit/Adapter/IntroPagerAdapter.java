package com.planitse2022.planit.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.planitse2022.planit.IntroItemFragment;

public class IntroPagerAdapter extends FragmentPagerAdapter {
    private int size = 5;
    public IntroPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull @Override public Fragment getItem(int position) {
        return IntroItemFragment.newInstance(position);
    }
    @Override public int getCount() {
        return size;
    }
}