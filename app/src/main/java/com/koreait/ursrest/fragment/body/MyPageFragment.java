package com.koreait.ursrest.fragment.body;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.ursrest.R;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.activity.RegistActivity;
import com.koreait.ursrest.activity.WebViewActivity;
import com.koreait.ursrest.domain.Member;
import com.koreait.ursrest.repository.MemberDAO;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyPageFragment extends Fragment {
    String TAG = this.getClass().getName();

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    Button bt_update;
    TextView address;

    String g ;
    int gmember_id;
    MainActivity mainActivity;
    MemberDAO memberDAO;
    public Handler testHandler;
    Member member;
    Calendar myCalendar = Calendar.getInstance();

    public MyPageFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    public MyPageFragment() { }

    private void updateLabel(EditText et_date) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);


        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mypage, container, false);
        memberDAO = new MemberDAO(this);

        TextView name = view.findViewById(R.id.join_name);
        TextView member_id = view.findViewById(R.id.join_password);
        TextView id = view.findViewById(R.id.join_id);
        TextView email = view.findViewById(R.id.join_email);
        TextView tel = view.findViewById(R.id.join_tel);
        EditText birthday = view.findViewById(R.id.join_birthday);
        address = view.findViewById(R.id.et_address);
        TextView et_address_detail = view.findViewById(R.id.et_address_detail);
        Spinner gender = view.findViewById(R.id.join_gender);
        Button button = view.findViewById(R.id.button);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                g = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(mainActivity, WebViewActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                }
            });
        }


        id.setEnabled(false);

        DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(birthday);
            }
        };



        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        Thread thread = new Thread(){
            @Override
            public void run() {
                memberDAO.getDetail(mainActivity.getIntent().getIntExtra("member_id",1));
            }
        };

        thread.start();



        testHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                member = (Member) bundle.get("member");
                gmember_id = member.getMember_id();
                name.setText(member.getUser_name());
                id.setText(member.getUser_id());
                email.setText(member.getUser_email_id()+"@"+member.getUser_email_server());
                tel.setText(member.getUser_phone());


                Date date1 = new Date(Long.parseLong(member.getUser_birthday().toString()));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                birthday.setText(dateFormat.format(date1));


                updateLabel(birthday);

                String[] add = member.getUser_location().split(",");

                if(add.length >= 3) {
                    address.setText(add[0] +","+ add[1]);
                    et_address_detail.setText(add[2]);
                }else{
                    address.setText(member.getUser_location());
                }

                if(member.getUser_gender().equals("male")){
                    gender.setSelection(0);
                }else{
                    gender.setSelection(1);
                }


            }
        };

        bt_update = view.findViewById(R.id.edit_buttons);

        bt_update.setOnClickListener(e->{
            Member upMember = new Member();

            upMember.setUser_name(name.getText().toString());
            upMember.setUser_birthday(birthday.getText().toString());
            upMember.setMember_id(gmember_id);

            if(g.equals("남자")){g = "male";}else{g="female";}
            upMember.setUser_gender(g);

            String[] emaila = email.getText().toString().split("@");
            upMember.setUser_email_id(emaila[0]);
            upMember.setUser_email_server(emaila[1]);
            upMember.setUser_location(address.getText().toString()+","+et_address_detail.getText().toString());
            upMember.setUser_phone(tel.getText().toString());

            Thread thread1 = new Thread(){
                @Override
                public void run() {
                    memberDAO.updateMember(upMember);

                }
            };

            thread1.start();
            mainActivity.customViewPager.setCurrentItem(0, false);

        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        address.setText(data);
                    }
                }
                break;
        }
    }


}