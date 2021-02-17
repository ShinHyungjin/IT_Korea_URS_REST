package com.koreait.ursrest.fragment.body;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.adpater.MenusAdapter;
import com.koreait.ursrest.domain.MenuCart;
import com.koreait.ursrest.domain.TableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TableMapFragment extends Fragment {
    public static View view;
    public static MenusAdapter menusAdapter;
    public static TableMap tableMap;
    public static MainActivity mainActivity;
    public static Button[] bt = new Button[42];
    public static int[] bt_id = {R.id.t1_1, R.id.t1_2, R.id.t1_3, R.id.t1_4, R.id.t1_5, R.id.t1_6,
            R.id.t2_1, R.id.t2_2, R.id.t2_3, R.id.t2_4, R.id.t2_5, R.id.t2_6,
            R.id.t3_1, R.id.t3_2, R.id.t3_3, R.id.t3_4, R.id.t3_5, R.id.t3_6,
            R.id.t4_1, R.id.t4_2, R.id.t4_3, R.id.t4_4, R.id.t4_5, R.id.t4_6,
            R.id.t5_1, R.id.t5_2, R.id.t5_3, R.id.t5_4, R.id.t5_5, R.id.t5_6,
            R.id.t6_1, R.id.t6_2, R.id.t6_3, R.id.t6_4, R.id.t6_5, R.id.t6_6,
            R.id.t7_1, R.id.t7_2, R.id.t7_3, R.id.t7_4, R.id.t7_5, R.id.t7_6};

    public static ArrayList<String> tableList = new ArrayList<String>();
    public static HashMap<Integer, String> tableSeatMap = new HashMap<Integer, String>();

    public TableMapFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_table, container, false);

        System.out.println("TableFragment onCreateView 실행 ");

        int count = 0;
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 7; j++) {
                final int a = count++;
                final int row = i;
                final int col = j;
                bt[a] = view.findViewById(bt_id[a]);
                bt[a].setId((row * 10) + col);
                bt[a].setText(String.valueOf(a));

                bt[a].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("나 눌렀어? t" + row + "_" + col + "\ta : " + a + "\t내 ID : " + view.getId());
                        System.out.println("나 버튼 아이디 : " + bt[a].getId());
                        if(tableSeatMap.containsKey(Integer.parseInt(bt[a].getText().toString()))) {
                            //tableList.remove(row+"_"+col);
                            tableSeatMap.remove(Integer.parseInt(bt[a].getText().toString()));
                            if (tableMap.getMap_index().substring(a, a + 1).equals("f")) {
                                bt[a].setBackgroundColor(Color.parseColor("#3a78c3"));
                                //bt[i].setBackgroundResource(R.drawable.four);
                            } else if (tableMap.getMap_index().substring(a, a + 1).equals("e")) {
                                bt[a].setBackgroundColor(Color.parseColor("#b9dea0"));
                                //bt[i].setBackgroundResource(R.drawable.two);
                            }
                        } else {
                            //tableList.add(row+"_"+col);
                            tableSeatMap.put(Integer.parseInt(bt[a].getText().toString()),row+"_"+col);
                            bt[a].setBackgroundColor(Color.parseColor("#e6cac4"));
                        }
                    }
                });
            }
        }

        Button bt_next = view.findViewById(R.id.bt_next);
        Button bt_back = view.findViewById(R.id.bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.customViewPager.setCurrentItem(2, true);
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableSeatMap.size() <= 0) {
                    Toast.makeText(getContext(), "테이블을 선택해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    Message message = new Message();

                    Bundle bundle = new Bundle();

                    //bundle.putStringArrayList("tableList", tableList);
                    bundle.putSerializable("tableSeatMap", tableSeatMap);

                    message.setData(bundle);
                    mainActivity.finalpaymentHandler.sendMessage(message);
                }
            }
        });

        return view;
    }

    public void setTableMapReset() {
        int count = 0;
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 7; j++) {
                final int a = count++;
                final int row = i;
                final int col = j;

                bt[a].setEnabled(true);
                bt[a].setCursorVisible(true);
                bt[a].setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

    public void setTable() {
        System.out.println("넘겨받은 tableMap : " + tableMap.getMap_index() + "\ttableMapIndex 구성...");

        String[] arr = tableMap.getUnavailable().split(",");
        String unavail = null;
        int tabletext = 1;

        List<String> unavailList = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].equals("null")) {
                unavailList.add(String.valueOf(arr[i].charAt(0)) + String.valueOf(arr[i].charAt(2)));
            }
        }

        for (int i = 0; i < 42; i++) {
            boolean flag = true;
            for (int j = 0; j < unavailList.size(); j++) {

                if (bt[i].getId() == Integer.parseInt(unavailList.get(j))) {

                    System.out.println("bt ID : " + bt[i].getId() +"\tunavailList : " + unavailList.get(j));

                    // bt[i].setBackgroundResource(R.drawable.unavail);
                    bt[i].setBackgroundColor(Color.parseColor("#472b34"));
                    bt[i].setEnabled(false);
                    bt[i].setCursorVisible(false);
                    bt[i].setText("X");
                    bt[i].setTextColor(Color.parseColor("#ffffff"));
                    tabletext++;
                    unavailList.remove(j);
                    flag = false;
                    break;
                }
            }

            if (flag) {
                if (tableMap.getMap_index().substring(i, i + 1).equals("f")) {
                    bt[i].setBackgroundColor(Color.parseColor("#3a78c3"));
                    bt[i].setText(String.valueOf(tabletext++));
                    //bt[i].setBackgroundResource(R.drawable.four);
                } else if (tableMap.getMap_index().substring(i, i + 1).equals("e")) {
                    bt[i].setBackgroundColor(Color.parseColor("#b9dea0"));
                    bt[i].setText(String.valueOf(tabletext++));
                    //bt[i].setBackgroundResource(R.drawable.two);
                } else if (tableMap.getMap_index().substring(i, i + 1).equals("_")) {
                    bt[i].setBackgroundColor(Color.parseColor("#e8e8e8"));
                    //bt[i].setBackgroundResource(R.drawable.nulltable);
                    bt[i].setText("");
                    bt[i].setEnabled(false);
                    bt[i].setCursorVisible(false);
                }
            }
        }
    }
}