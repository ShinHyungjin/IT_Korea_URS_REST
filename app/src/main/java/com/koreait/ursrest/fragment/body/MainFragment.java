package com.koreait.ursrest.fragment.body;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.adpater.ImageAdapter;
import com.koreait.ursrest.repository.StoreDAO;

import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment {
    String TAG = this.getClass().getName();

    private ViewPager viewPager;
    private ImageAdapter imageAdapter;
    MainActivity mainActivity;

    ImageButton[] imageButton = new ImageButton[6];
    int[] id = {R.id.koreaimage, R.id.chinaiamge, R.id.japaneseimage, R.id.latiniamge, R.id.westernimage, R.id.fastfoodimage};

    int currentPage = 0;
    Timer timer = null;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000; // time in milliseconds between successive task executions.

    StoreDAO storeDAO;

    public MainFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.storeDAO = new StoreDAO(mainActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mainviewpager, container, false);

        System.out.println("MainFragment 실행");

        viewPager = view.findViewById(R.id.viewPager);
        imageAdapter = new ImageAdapter(this.getChildFragmentManager(), 0);
        viewPager.setAdapter(imageAdapter);

        for (int i = 0; i < 6; i++) {
            final int a = i;
            imageButton[i] = view.findViewById(id[i]);
            imageButton[a].setOnClickListener(e -> {
                Log.d(TAG, "a : " + a);
                getStore(a);
            });
        }

        Handler handler = new Handler();
        Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == 3) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        if (timer == null) { // BottomBar의 Main을 여러번 누르면 아래의 타이머가 연속적으로 생성 및 호출되어 애니메이션이 이상함 = 싱글톤
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);
        }
        return view;
    }

    public void getStore(int imageId) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int id = imageId + 1;
                switch (id) {
                    case 1: {
                        storeDAO.selectAll(id);
                        break;
                    }
                    case 2: {
                        storeDAO.selectAll(id);
                        break;
                    }
                    case 3: {
                        storeDAO.selectAll(id);
                        break;
                    }
                    case 4: {
                        storeDAO.selectAll(id);
                        break;
                    }
                    case 5: {
                        storeDAO.selectAll(id);
                        break;
                    }
                    case 6: {
                        storeDAO.selectAll(id);
                        break;
                    }
                }
            }
        };
        thread.start();
    }
}