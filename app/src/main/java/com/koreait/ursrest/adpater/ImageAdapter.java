package com.koreait.ursrest.adpater;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.koreait.ursrest.fragment.image.ImageFragment1;
import com.koreait.ursrest.fragment.image.ImageFragment2;
import com.koreait.ursrest.fragment.image.ImageFragment3;

public class ImageAdapter extends FragmentPagerAdapter {
    Fragment[] fragments = new Fragment[3];

    public ImageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        fragments[0] = new ImageFragment1();
        fragments[1] = new ImageFragment2();
        fragments[2] = new ImageFragment3();
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