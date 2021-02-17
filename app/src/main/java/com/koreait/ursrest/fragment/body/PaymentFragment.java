package com.koreait.ursrest.fragment.body;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.adpater.MenusAdapter;
import com.koreait.ursrest.adpater.PaymentAdapter;
import com.koreait.ursrest.repository.MemberDAO;

public class PaymentFragment extends Fragment {
    MainActivity mainActivity;
    public ListView receiptListView;
    public PaymentAdapter paymentAdapter;


    public PaymentFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_payment, container, false);

        receiptListView = view.findViewById(R.id.receiptList);
        paymentAdapter = new PaymentAdapter(mainActivity,this);
        receiptListView.setAdapter(paymentAdapter);

        System.out.println("PaymentFragment 실행");

        return view;
    }
}