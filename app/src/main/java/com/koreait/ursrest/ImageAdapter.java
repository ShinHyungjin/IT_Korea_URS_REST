package com.koreait.ursrest;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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