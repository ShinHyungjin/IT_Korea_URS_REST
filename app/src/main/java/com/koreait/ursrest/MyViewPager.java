package com.koreait.ursrest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyViewPager extends FragmentPagerAdapter {
    Fragment[] fragments = new Fragment[2];

    public MyViewPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        fragments[0] = new MainFragment();
        fragments[1] = new StoreFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}