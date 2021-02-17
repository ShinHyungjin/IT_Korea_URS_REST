package com.koreait.ursrest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.koreait.ursrest.adpater.FinalPaymentAdapter;
import com.koreait.ursrest.adpater.MenusAdapter;
import com.koreait.ursrest.adpater.PaymentAdapter;
import com.koreait.ursrest.adpater.StoreAdapter;
import com.koreait.ursrest.domain.MenuCart;
import com.koreait.ursrest.domain.Menus;
import com.koreait.ursrest.domain.TableMap;
import com.koreait.ursrest.fragment.body.FinalPaymentFragment;
import com.koreait.ursrest.fragment.body.MenusFragment;
import com.koreait.ursrest.fragment.body.PaymentFragment;
import com.koreait.ursrest.fragment.body.StoreFragment;
import com.koreait.ursrest.fragment.body.TableMapFragment;
import com.koreait.ursrest.repository.MemberDAO;
import com.koreait.ursrest.viewpager.CustomViewPager;
import com.koreait.ursrest.adpater.BodyAdapter;
import com.koreait.ursrest.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    public CustomViewPager customViewPager;
    public BodyAdapter bodyAdapter;
    public BottomNavigationView bottomNavigationView;
    public MemberDAO memberDAO;
    public StoreAdapter storeAdapter;
    public StoreFragment storeFragment;
    public MenusAdapter menusAdapter;
    public MenusFragment menusFragment;
    public PaymentAdapter paymentAdapter;
    public PaymentFragment paymentFragment;
    public TableMapFragment tableMapFragment;
    public FinalPaymentAdapter finalPaymentAdapter;
    public FinalPaymentFragment finalPaymentFragment;
    public int member_id;
    public String store_name;


    public Handler storeHandler,menuHandler,recieptHandler,tableMapHandler,finalpaymentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customViewPager = findViewById(R.id.customViewPager);
        bodyAdapter = new BodyAdapter(this.getSupportFragmentManager(), 0, this);
        customViewPager.setAdapter(bodyAdapter);

        customViewPager.setOffscreenPageLimit(8);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        member_id = getIntent().getIntExtra("member_id",1);

        storeHandler = new Handler(Looper.getMainLooper()){
            //쓰레드들의 부탁을 받아, 대신 UI제어!!
            public void handleMessage(@NonNull Message message) {
                Bundle bundle = message.getData();
                ArrayList storeList = (ArrayList)bundle.get("storeList");

                System.out.println("어댑터 호출, StoreList Size : " + storeList.size());
                customViewPager.setCurrentItem(1,true);

                storeFragment = new StoreFragment(MainActivity.this);
                storeAdapter = new StoreAdapter(MainActivity.this,storeFragment);

                storeAdapter.storeList = storeList; //어댑터의 List값을 변경
                storeFragment.storeList = findViewById(R.id.storeList);

                storeFragment.storeList.setAdapter(storeAdapter);

                storeAdapter.notifyDataSetChanged();//어댑터 갱신...
                storeFragment.storeList.invalidate();//리스트뷰 갱신


            }
        };

        menuHandler = new Handler(Looper.getMainLooper()){
            //쓰레드들의 부탁을 받아, 대신 UI제어!!
            public void handleMessage(@NonNull Message message) {
                Bundle bundle = message.getData();
                ArrayList menuList = (ArrayList)bundle.get("menuList");

                customViewPager.setCurrentItem(2,true);

                menusFragment = new MenusFragment(MainActivity.this);
                menusAdapter = new MenusAdapter(MainActivity.this, menusFragment);

                menusAdapter.menusList = menuList; //어댑터의 List값을 변경

                for(int i=0; i<menusAdapter.menusList.size(); i++) {
                    Menus menus = menusAdapter.menusList.get(i);
                    MenuCart menuCart = new MenuCart();

                    menuCart.setMenu_id(menus.getMenu_id());
                    menuCart.setMenu_name(menus.getMenu_name());
                    menuCart.setMenu_stock(menus.getMenu_stock());
                    menuCart.setMenu_price(menus.getMenu_price());
                    menuCart.setStore_id(menus.getStore_id());
                    menuCart.setM_image(menus.getM_image());
                    menuCart.setMenu_image(menus.getMenu_image());
                    menuCart.setQuantity(0);
                    menusAdapter.menuCartList.add(menuCart);
                }

                menusFragment.menuListView = findViewById(R.id.menuList);

                menusFragment.menuListView.setAdapter(menusAdapter);

                menusAdapter.notifyDataSetChanged();//어댑터 갱신...
                menusFragment.menuListView.invalidate();//리스트뷰 갱신
            }
        };

        recieptHandler = new Handler(Looper.getMainLooper()){
            //쓰레드들의 부탁을 받아, 대신 UI제어!!
            public void handleMessage(@NonNull Message message) {
                Bundle bundle = message.getData();
                ArrayList receiptList = (ArrayList)bundle.get("receiptList");
                ArrayList storeList = (ArrayList)bundle.get("storeList");

                customViewPager.setCurrentItem(6,false);

                paymentFragment = new PaymentFragment(MainActivity.this);
                paymentAdapter = new PaymentAdapter(MainActivity.this, paymentFragment);

                paymentAdapter.receiptList = receiptList; //어댑터의 List값을 변경
                paymentAdapter.storeList = storeList;

                paymentFragment.receiptListView = findViewById(R.id.receiptList);

                paymentFragment.receiptListView.setAdapter(paymentAdapter);

                paymentAdapter.notifyDataSetChanged();//어댑터 갱신...
                paymentFragment.receiptListView.invalidate();//리스트뷰 갱신
            }
        };

        tableMapHandler = new Handler(Looper.getMainLooper()){
            //쓰레드들의 부탁을 받아, 대신 UI제어!!
            public void handleMessage(@NonNull Message message) {
                Bundle bundle = message.getData();
                TableMap tableMap = (TableMap)bundle.get("tableMap");

                customViewPager.setCurrentItem(3,true);
                tableMapFragment.tableList.clear();
                tableMapFragment.tableSeatMap.clear();

                tableMapFragment = new TableMapFragment(MainActivity.this);
                tableMapFragment.tableMap = tableMap;
                tableMapFragment.setTableMapReset();
                tableMapFragment.setTable();

            }
        };

        finalpaymentHandler = new Handler(Looper.getMainLooper()){
            //쓰레드들의 부탁을 받아, 대신 UI제어!!
            public void handleMessage(@NonNull Message message) {
                Bundle bundle = message.getData();
                HashMap<Integer, String> tableSeatMap = (HashMap<Integer, String>)bundle.get("tableSeatMap");
                //ArrayList<String> tableList = (ArrayList<String>)bundle.get("tableList");

                customViewPager.setCurrentItem(4,true);

                finalPaymentFragment = new FinalPaymentFragment(MainActivity.this);
                finalPaymentAdapter = new FinalPaymentAdapter(MainActivity.this, finalPaymentFragment);

                finalPaymentFragment.menuCartMap = menusAdapter.menuCartMap;
                finalPaymentAdapter.menuCartMap = menusAdapter.menuCartMap;

                finalPaymentFragment.tableSeatMap = tableSeatMap;


                finalPaymentFragment.tableSeatMap = tableSeatMap;
                finalPaymentAdapter.tableSeatMap = tableSeatMap;

                //finalPaymentFragment.tableList = tableList;
                //finalPaymentAdapter.tableList = tableList;

                //finalPaymentFragment.tableList = tableMapFragment.tableList;

                finalPaymentFragment.table_seat = findViewById(R.id.table_seat);

                String tablestr = "";
                Iterator<Integer> it = tableSeatMap.keySet().iterator();
                while(it.hasNext()){
                    int key = it.next();
                    String value = tableSeatMap.get(key);
                    tablestr = tablestr + key + " / ";
                }
                tablestr = tablestr.substring(0, tablestr.length()-2) + "Seat";

                /*
                for(int i=0; i<tableList.size(); i++) {
                    if(i == tableList.size()-1) {
                        //tablestr = tableSeat.get(i) + " Seat";
                        tablestr = tablestr + tableList.get(i) + " Seat";
                    }else {
                        //tablestr = tableSeat.get(i) + " Seat /";
                        tablestr = tablestr + tableList.get(i) + " Seat /";
                    }
                }
                 */


                finalPaymentFragment.table_seat.setText(tablestr);

                finalPaymentFragment.pay_menuList = findViewById(R.id.pay_menuList2);

                finalPaymentFragment.pay_menuList.setAdapter(finalPaymentAdapter);

                finalPaymentAdapter.notifyDataSetChanged();//어댑터 갱신...
                finalPaymentFragment.pay_menuList.invalidate();//리스트뷰 갱신

            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) { //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                    case R.id.mainPage: {
                        customViewPager.setCurrentItem(0, false);
                        return true;
                    }
                    case R.id.myPage: {
                        customViewPager.setCurrentItem(5, false);
                        return true;
                    }
                    case R.id.paymentList: {
                        memberDAO = new MemberDAO(MainActivity.this);
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                memberDAO.getReceipt(member_id);
                            }
                        };
                        thread.start();
                        return true;
                    }
                    case R.id.logout: {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = auto.edit();
                        //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                        editor.clear();
                        editor.commit();

                        Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    }
                    default:
                        return false;
                }

            }
        });

    }
}