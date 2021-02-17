package com.koreait.ursrest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.koreait.ursrest.R;
import com.koreait.ursrest.domain.Member;
import com.koreait.ursrest.repository.MemberDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegistActivity extends AppCompatActivity {

    String TAG = this.getClass().getName();
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    private EditText et_address;
    private EditText et_address_detail;
    private EditText join_name;
    private EditText join_email;
    private EditText join_birthday;
    private EditText join_tel;
    private EditText join_password;
    private EditText join_id;
    private Spinner join_gender;
    private String gender;
    private ProgressBar progressBar;
    private int check = 0 ;
    private int emailcheck = 0 ;
    private MemberDAO memberDAO;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText) findViewById(R.id.join_birthday);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        memberDAO = new MemberDAO(RegistActivity.this);
        join_name = findViewById(R.id.join_name);
        join_email = findViewById(R.id.join_email);
        join_birthday = findViewById(R.id.join_birthday);
        join_tel = findViewById(R.id.join_tel);
        join_password = findViewById(R.id.join_password);
        join_id = findViewById(R.id.join_id);
        join_gender = (Spinner) findViewById(R.id.join_gender);
        progressBar = findViewById(R.id.progressBar);


        join_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegistActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        join_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        et_address = (EditText) findViewById(R.id.et_address);
        et_address_detail = findViewById(R.id.et_address_detail);

        Button btn_search = (Button) findViewById(R.id.button);

        if (btn_search != null) {
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(RegistActivity.this, WebViewActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        et_address.setText(data);
                    }
                }
                break;
        }
    }

    public void register(View view){
        Log.d(TAG,"생일은"+join_birthday.getText().toString());
        if (join_email.getText().toString().replace(" ", "").equals("") || join_name.getText().toString().replace(" ", "").equals("") || join_birthday.getText().toString().replace(" ", "").equals("")|| join_tel.getText().toString().replace(" ", "").equals("")||join_password.getText().toString().replace(" ", "").equals("")||join_id.getText().toString().replace(" ", "").equals("")||gender.replace(" ", "").equals("")) {
            Toast.makeText(RegistActivity.this, "모든 빈칸을 입력하세요", Toast.LENGTH_SHORT).show();
        }else {


            if (check == 1) {
                if (!checkEmail(join_email.getText().toString())) {
                    Toast.makeText(RegistActivity.this, "Email형식으로 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Member member = new Member();


                    member.setUser_birthday(join_birthday.getText().toString().replace("/","-"));
                    member.setUser_name(join_name.getText().toString());
                    member.setUser_id(join_id.getText().toString());
                    member.setUser_gender(gender);

                    String[] email = join_email.getText().toString().split("@");
                    member.setUser_email_id(email[0]);
                    member.setUser_email_server(email[1]);
                    member.setUser_location(et_address.getText().toString()+","+et_address_detail.getText().toString());
                    member.setUser_phone(join_tel.getText().toString());
                    member.setUser_password(join_password.getText().toString());

                    Thread thread = new Thread() {
                        @Override
                        public void run() {

                            memberDAO.RESTregist(member);

                            RegistActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(RegistActivity.this, "메일 확인후 로그인 해주세요", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(intent);
                            finish();

                        }
                    };

                    thread.start();
                }
            } else {
                Toast.makeText(RegistActivity.this, "중복 체크를 먼저 해주세요", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void checkId(View view){
        Thread thread = new Thread(){
            @Override
            public void run() {
                check = memberDAO.checkId(join_id.getText().toString());
                if(check == 0) {
                    RegistActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(RegistActivity.this, "중복 되는 아이디 입니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    RegistActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(RegistActivity.this, "사용 가능한 아이디 입니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };

        thread.start();
    }
    public void backpage(View view) {
        Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}