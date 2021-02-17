package com.koreait.ursrest.fragment.body;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.adpater.MenusAdapter;
import com.koreait.ursrest.domain.MenuCart;
import com.koreait.ursrest.repository.TableMapDAO;

import java.util.Iterator;

public class MenusFragment extends Fragment {
    public MainActivity mainActivity;
    public ListView menuListView;
    public MenusAdapter menusAdapter;
    public TableMapDAO tableMapDAO;
    public int store_id;

    public MenusFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.tableMapDAO = new TableMapDAO(mainActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menulist,container,false);

        System.out.println("MenuFragment 실행 ");

        menuListView = view.findViewById(R.id.menuList);
        menusAdapter = new MenusAdapter(mainActivity,this);
        menuListView.setAdapter(menusAdapter);

        Button bt_next = view.findViewById(R.id.bt_next);
        Button bt_back = view.findViewById(R.id.bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.customViewPager.setCurrentItem(1, true);

            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<Integer> menuCartMap = menusAdapter.menuCartMap.keySet().iterator();
                while(menuCartMap.hasNext()) {
                    int menuCart_id = menuCartMap.next();
                    MenuCart menuCart = menusAdapter.menuCartMap.get(menuCart_id);
                    store_id = menuCart.getStore_id();
                    //System.out.println("MenuCart_id : " + menuCart.getMenu_id() + "\tName : " + menuCart.getMenu_name() + "\tQuantity : " + menuCart.getQuantity() + "\tTotalPrice : " + menuCart.getMenu_price() + "\tStore_id : "+ menuCart.getStore_id());
                    //System.out.println("store_id : " + store_id);
                }

                if(menusAdapter.menuCartMap.size() <= 0) {
                    Toast.makeText(getContext(),"최소 1개 이상의 메뉴를 추가하세요", Toast.LENGTH_SHORT).show();
                } else {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            tableMapDAO.selectAll(store_id);
                        }
                    };
                    thread.start();
                }
            }
        });


        return view;
    }
}
