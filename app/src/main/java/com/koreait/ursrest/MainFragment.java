package com.koreait.ursrest;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment {
    private ViewPager viewPager;
    private ImageAdapter imageAdapter;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000; // time in milliseconds between successive task executions.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mainviewpager, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        imageAdapter = new ImageAdapter(this.getChildFragmentManager(), 0);
        viewPager.setAdapter(imageAdapter);

        Handler handler = new Handler();
        Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == 3) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        return view;
    }
    public void getStore(ViewGroup viewGroup) {
        switch (viewGroup.getId()) {
            case R.id.korea :

        }

    }
}