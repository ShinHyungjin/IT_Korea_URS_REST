package com.koreait.ursrest.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.MenuCart;
import com.koreait.ursrest.domain.Receipt;
import com.koreait.ursrest.domain.Store;
import com.koreait.ursrest.fragment.body.FinalPaymentFragment;
import com.koreait.ursrest.fragment.body.PaymentFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FinalPaymentAdapter extends BaseAdapter {
    public static HashMap<Integer, MenuCart> menuCartMap = new HashMap<Integer, MenuCart>();
    public static List<MenuCart> menuCartList = new ArrayList<MenuCart>();
    public static ArrayList<String> tableList = new ArrayList<String>();
    public boolean flag = true;

    LayoutInflater layoutInflater;
    MainActivity mainActivity;
    FinalPaymentFragment finalPaymentFragment;
    public static HashMap<Integer, String> tableSeatMap = new HashMap<Integer, String>();
    DecimalFormat myFormatter;

    public FinalPaymentAdapter(MainActivity mainActivity, FinalPaymentFragment finalPaymentFragment){
        this.mainActivity = mainActivity;
        this.finalPaymentFragment = finalPaymentFragment;

        layoutInflater = mainActivity.getLayoutInflater();
        myFormatter = new DecimalFormat("###,###");
    }


    @Override
    public int getCount() {
        return menuCartMap.size();
    }

    @Override
    public Object getItem(int position) {
        return menuCartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuCartList.get(position).getMenu_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(flag) {
            menuCartList.clear();
            Iterator<Integer> it = menuCartMap.keySet().iterator();
            while(it.hasNext()) {
                int key = it.next();
                MenuCart menuCart = menuCartMap.get(key);
                menuCartList.add(menuCart);
            }
            flag = false;
        }
        MenuCart menuCart = menuCartList.get(position);

        View view = null;

        if(convertView == null){
            view = layoutInflater.inflate(R.layout.item_paymenu,parent,false);
        }else{
            view = convertView;
        }

        TextView menu_name = view.findViewById(R.id.menu_name2);
        TextView menu_stock = view.findViewById(R.id.menu_stock2);
        TextView menu_oneprice = view.findViewById(R.id.menu_oneprice2);
        TextView menu_totalprice = view.findViewById(R.id.menu_totalprice2);

        menu_name.setText(menuCart.getMenu_name());
        menu_stock.setText(menuCart.getMenu_stock());
        menu_oneprice.setText(myFormatter.format(menuCart.getMenu_price()));
        menu_totalprice.setText(myFormatter.format(menuCart.getQuantity()*menuCart.getMenu_price()));

        return view;
    }
}