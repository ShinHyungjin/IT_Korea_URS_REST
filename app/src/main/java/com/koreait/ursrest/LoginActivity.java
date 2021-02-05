package com.koreait.ursrest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();
    EditText id, pwd;
    Button btn;
    String loginId, loginPwd;
    int member_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText)findViewById(R.id.inputId);
        pwd = (EditText)findViewById(R.id.inputPwd);
        btn = (Button)findViewById(R.id.loginBtn);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd",null);

        Thread thread = new Thread(){
            @Override
            public void run() {

                MemberDAO memberDAO = new MemberDAO(LoginActivity.this);
                Member member = new Member();
                Log.d(TAG, id.getText().toString());
                Log.d(TAG, pwd.getText().toString());

                if(loginId !=null && loginPwd != null) {
                    member.setUser_id(loginId);
                    member.setUser_password(loginPwd);
                    member_id = memberDAO.login(member);
                    if(member_id != 0) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, loginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                                member.setMember_id(member_id);
                            }
                        });
                        //
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("member_id",member_id);
                        startActivity(intent);
                        finish();
                    }
                }
                //id와 pwd가 null이면 Mainactivity가 보여짐.
                else if(loginId == null && loginPwd == null){
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Thread thread2 = new Thread() {
                                @Override
                                public void run() {
                                    member.setUser_id(id.getText().toString());
                                    member.setUser_password(pwd.getText().toString());
                                    member_id = memberDAO.login(member);
                                    if ( member_id != 0) {
                                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                        //아이디가 '부르곰'이고 비밀번호가 '네이버'일 경우 SharedPreferences.Editor를 통해
                                        //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                                        SharedPreferences.Editor autoLogin = auto.edit();
                                        autoLogin.putString("inputId", id.getText().toString());
                                        autoLogin.putString("inputPwd", pwd.getText().toString());
                                        //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                                        autoLogin.commit();
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, id.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();
                                                member.setMember_id(member_id);
                                            }
                                        });
                                        //
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("member_id",member_id);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            };
                            thread2.start();
                        }
                    });

                }
            }
        };
        thread.start();

    }
}