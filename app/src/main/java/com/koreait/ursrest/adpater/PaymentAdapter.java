package com.koreait.ursrest.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.Receipt;
import com.koreait.ursrest.domain.Store;
import com.koreait.ursrest.fragment.body.PaymentFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends BaseAdapter {

    public List<Receipt> receiptList = new ArrayList<Receipt>();
    public List<Store> storeList = new ArrayList<Store>();
    LayoutInflater layoutInflater;
    MainActivity mainActivity;
    PaymentFragment paymentFragment;
    DecimalFormat myFormatter;


    public PaymentAdapter(MainActivity mainActivity, PaymentFragment paymentFragment){
        this.mainActivity = mainActivity;
        this.paymentFragment = paymentFragment;
        layoutInflater = mainActivity.getLayoutInflater();
        myFormatter = new DecimalFormat("###,###");
    }


    @Override
    public int getCount() {
        return receiptList.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Receipt receipt = receiptList.get(position);
        Store store = storeList.get(position);
        View view = null;

        if(convertView == null){
            view = layoutInflater.inflate(R.layout.item_receipt,parent,false);
        }else{
            view = convertView;
        }

        TextView store_name = view.findViewById(R.id.Rstore_name);
        TextView receipt_date = view.findViewById(R.id.receipt_date);
        TextView total_price = view.findViewById(R.id.total_price);
        TextView Rstore_location = view.findViewById(R.id.Rstore_location);
        ImageView store_image = view.findViewById(R.id.Rimg);

        Glide.with(mainActivity).load("http://192.168.0.71:8888/resources/data/store/" + store.getMember_id()+"M."+store.getStore_image()).into(store_image);

        store_name.setText(store.getStore_name());
        receipt_date.setText(receipt.getReceipt_regdate().substring(0,11));
        total_price.setText(myFormatter.format(receipt.getReceipt_totalamount()));
        Rstore_location.setText(store.getStore_location());
        store_image.setImageBitmap(store.getBitmap_image());

        return view;
    }
}