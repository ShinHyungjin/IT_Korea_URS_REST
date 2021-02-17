package com.koreait.ursrest.adpater;

import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.fragment.body.FinalPaymentFragment;
import com.koreait.ursrest.fragment.body.MainFragment;
import com.koreait.ursrest.fragment.body.MenusFragment;
import com.koreait.ursrest.fragment.body.MyPageFragment;
import com.koreait.ursrest.fragment.body.PaymentFragment;
import com.koreait.ursrest.fragment.body.StoreFragment;
import com.koreait.ursrest.fragment.body.TableMapFragment;

public class BodyAdapter extends FragmentStatePagerAdapter {
    public Fragment[] fragments = new Fragment[7];
    MainActivity mainActivity;

    public BodyAdapter(@NonNull FragmentManager fm, int behavior, MainActivity mainActivity) {
        super(fm, behavior);
        this.mainActivity = mainActivity;

        fragments[0] = new MainFragment(mainActivity);
        fragments[1] = new StoreFragment(mainActivity);
        fragments[2] = new MenusFragment(mainActivity);
        fragments[3] = new TableMapFragment(mainActivity);
        fragments[4] = new FinalPaymentFragment(mainActivity);

        fragments[5] = new MyPageFragment(mainActivity);
        fragments[6] = new PaymentFragment(mainActivity);

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