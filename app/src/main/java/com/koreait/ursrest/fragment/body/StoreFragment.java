package com.koreait.ursrest.fragment.body;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.adpater.StoreAdapter;

public class StoreFragment extends Fragment {
    public MainActivity mainActivity;
    public ListView storeList;
    public StoreAdapter storeAdapter;
    Button bt_back;

    public StoreFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_store, container, false);

        System.out.println("StoreFragment onCreateView 실행 ");

        bt_back = view.findViewById(R.id.bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.customViewPager.setCurrentItem(0,true);
            }
        });

        storeList = view.findViewById(R.id.storeList);
        storeAdapter = new StoreAdapter(mainActivity, this);
        storeList.setAdapter(storeAdapter);

        return view;
    }
}
