package com.koreait.ursrest.adpater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.Store;
import com.koreait.ursrest.fragment.body.StoreFragment;
import com.koreait.ursrest.repository.MenuDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreAdapter extends BaseAdapter {
    String TAG=this.getClass().getName();

    public List<Store> storeList = new ArrayList<Store>();
    LayoutInflater layoutInflater;
    MainActivity mainActivity;
    StoreFragment storeFragment;
    MenuDAO menuDAO;

    public StoreAdapter(MainActivity mainActivity, StoreFragment storeFragment){
        this.mainActivity = mainActivity;
        this.storeFragment = storeFragment;
        layoutInflater = mainActivity.getLayoutInflater();
        menuDAO = new MenuDAO(mainActivity);

    }
    //몇건?
    public int getCount() {
        return storeList.size();
    }

    //지정한 위치의 데이터 반환
    public Object getItem(int position) {
        return storeList.get(position);
    }

    //지정한 위치의 아이디 반환(식별값:개발자가 결정)
    public long getItemId(int position) {
        return 0; //pk
    }

    //지정한 위치에 들어갈 뷰 반환
    public View getView(int position, View convertView, ViewGroup parent) {
        Store store = storeList.get(position);
        View view = null;

        if(convertView==null){//최초로 등장하는 아이템
            view = layoutInflater.inflate(R.layout.item_store, parent, false);
        }else{
            view = convertView;
        }

        TextView store_name = view.findViewById(R.id.store_name);
        TextView store_reservation = view.findViewById(R.id.store_reservation);
        TextView store_openhour = view.findViewById(R.id.store_openhour);
        TextView store_closehour = view.findViewById(R.id.store_closehour);
        TextView store_location = view.findViewById(R.id.store_location);
        ImageView store_image = view.findViewById(R.id.img);

        store_name.setText(store.getStore_name());
        if(store.getStore_reservation().equals("TRUE")) {
            store_reservation.setText("예약가능");
        } else if(store.getStore_reservation().equals("FALSE")) {
            store_reservation.setText("예약불가");
        }
        store_openhour.setText(store.getStore_openhour());
        store_closehour.setText(store.getStore_closehour());
        //store_location.setText(store.getStore_location());

        String [] arr = store.getStore_location().split(",");
        store_location.setText(arr[1]);
        store_image.setImageBitmap(store.getBitmap_image());

        view.setOnClickListener(e-> {
            Log.d(TAG, "Store_id : " + store.getStore_id() + "\tStore_name : " + store.getStore_name());
            mainActivity.store_name = store.getStore_name();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    menuDAO.selectAll(store.getStore_id());
                }
            };
            thread.start();
        });

        return view;
    }
}