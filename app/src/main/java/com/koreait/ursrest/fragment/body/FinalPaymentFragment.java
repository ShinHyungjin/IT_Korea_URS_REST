package com.koreait.ursrest.fragment.body;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.adpater.FinalPaymentAdapter;
import com.koreait.ursrest.domain.MenuCart;
import com.koreait.ursrest.domain.Paybill;
import com.koreait.ursrest.repository.ReservationDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.CancelListener;
import kr.co.bootpay.CloseListener;
import kr.co.bootpay.ConfirmListener;
import kr.co.bootpay.DoneListener;
import kr.co.bootpay.ErrorListener;
import kr.co.bootpay.ReadyListener;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import lombok.SneakyThrows;

public class FinalPaymentFragment extends Fragment {
    String TAG = this.getClass().getName();
    private static final String APPICATION_ID = "Insert Your App ID";

    private int stuck = 10;
    public MainActivity mainActivity;
    public FinalPaymentAdapter finalPaymentAdapter;
    public ListView pay_menuList;
    public String tablestr = null;
    public TextView table_seat;
    public static List<MenuCart> menuCartList = new ArrayList<MenuCart>();
    public static ArrayList<String> tableList = new ArrayList<String>();
    public static HashMap<Integer, MenuCart> menuCartMap = new HashMap<Integer, MenuCart>();
    public static HashMap<Integer, String> tableSeatMap = new HashMap<Integer, String>();

    int total = 0 ;
    int menuq = 0 ;
    String table_map  ;
    String menu_ids = "";

    public FinalPaymentFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_finalpayment, container, false);

        pay_menuList = view.findViewById(R.id.pay_menuList2);
        finalPaymentAdapter = new FinalPaymentAdapter(mainActivity,this);
        pay_menuList.setAdapter(finalPaymentAdapter);

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(mainActivity, APPICATION_ID);

        //table_seat.setText(tablestr);


        Button bt_back = view.findViewById(R.id.bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.customViewPager.setCurrentItem(3, true);
            }
        });

        Button bt_payment = view.findViewById(R.id.bt_payment);

        bt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"테스트");
                total = 0 ;
                menuq = 0 ;
                menuCartList.clear();
                Iterator<Integer> it = menuCartMap.keySet().iterator();
                while(it.hasNext()) {
                    int key = it.next();
                    MenuCart menuCart = menuCartMap.get(key);
                    menuCartList.add(menuCart);

                }

                for(int i = 0 ; i < menuCartList.size(); i++){
                    total += (menuCartList.get(i).getMenu_price() * menuCartList.get(i).getQuantity());
                    menuq += menuCartList.get(i).getQuantity();
                    System.out.print("테스트 사이즈는 ? "+menuCartList.size());
                    if(i == menuCartList.size()-1) {
                        menu_ids += menuCartList.get(i).getMenu_id();
                    }else {
                        menu_ids +=   menuCartList.get(i).getMenu_id()+"," ;
                    }
                    }

                Iterator<Integer> tableit = tableSeatMap.keySet().iterator();
                System.out.println("넘겨받은 tableSeatMap의 사이즈는 " + tableSeatMap.size());
                while(tableit.hasNext()) {
                    int key = tableit.next();
                    String value = tableSeatMap.get(key);
                    table_map += "," + value;
                }

                System.out.println("table_map : " + table_map);



                /*
                for(int i = 0 ; i < tableList.size(); i++){
                    if(i == tableList.size()-1){
                        table_map += "," + tableList.get(i).toString();
                    }else {
                        table_map +=  "," + tableList.get(i).toString() ;
                    }
                }

                 */


                //        결제호출
                Bootpay.init(mainActivity)
                        .setApplicationId(APPICATION_ID) // 해당 프로젝트(안드로이드)의 application id 값
                        .setPG(PG.INICIS) // 결제할 PG 사
                        .setMethod(Method.PHONE) // 결제수단
                        .setName(mainActivity.store_name) // 결제할 상품명
                        .setOrderId("1234") // 결제 고유번호
                        .setPrice(total) // 결제할 금액
                        .addItem("마우스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                        .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                        .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                            @Override
                            public void onConfirm(@Nullable String message) {
                                if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                                Log.d(TAG, message);
                            }
                        })
                        .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다

                            @Override
                            public void onDone(@Nullable String message) {


                                try {
                                    JSONObject jsonObject = new JSONObject(message);
                                    Log.d(TAG,"데이터 : " + jsonObject.get("receipt_id"));

                                    Paybill paybill = new Paybill();
                                    paybill.setMember_id(mainActivity.member_id);
                                    paybill.setStore_id(Integer.toString(menuCartMap.get(menuCartList.get(0).getMenu_id()).getStore_id()));
                                    paybill.setReceipt_totalamount(total);
                                    paybill.setMenu_quantity(menuq);
                                    paybill.setUnavailable(table_map);
                                    paybill.setReservation_table(table_map);
                                    paybill.setMenu_ids(menu_ids);
                                    paybill.setBootpay_id(jsonObject.get("receipt_id").toString());

                                    ReservationDAO reservationDAO = new ReservationDAO(mainActivity);

                                    Thread thread = new Thread(){
                                        @Override
                                        public void run() {
                                            reservationDAO.pay(paybill);
                                        }
                                    };

                                    thread.start();

                                    mainActivity.customViewPager.setCurrentItem(0);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }






                            }
                        })
                        .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                            @Override
                            public void onReady(@Nullable String message) {
                                Log.d(TAG, message);
                            }
                        })
                        .onCancel(new CancelListener() { // 결제 취소시 호출
                            @Override
                            public void onCancel(@Nullable String message) {
                                Log.d(TAG, message);
                            }
                        })
                        .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                            @Override
                            public void onError(@Nullable String message) {
                                Log.d(TAG, message);
                            }
                        })
                        .onClose(new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d(TAG, "close");
                            }
                        })
                        .show();
            }
        });

        return view;
    }
}
