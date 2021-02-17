package com.koreait.ursrest.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.koreait.ursrest.R;
import com.koreait.ursrest.domain.Member;
import com.koreait.ursrest.repository.MemberDAO;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();
    EditText id, pwd;
    Button btn;
    String loginId, loginPwd;
    int member_id ;
    LinearLayout parentlinear;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        parentlinear = findViewById(R.id.parentlinear);
        Drawable drawable = parentlinear.getBackground();
        drawable.setAlpha(90);

        id = (EditText)findViewById(R.id.inputId);
        pwd = (EditText)findViewById(R.id.inputPwd);
        btn = (Button)findViewById(R.id.loginBtn);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd",null);

        id.setSelection(id.getText().length());
        pwd.setSelection(id.getText().length());

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


                        executor = ContextCompat.getMainExecutor(LoginActivity.this);
                        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                                executor, new BiometricPrompt.AuthenticationCallback() {

                            @Override
                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                                Toast.makeText(getApplicationContext(),"지문인식 취소",Toast.LENGTH_LONG).show();
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

                                                    SharedPreferences.Editor autoLogin = auto.edit();
                                                    autoLogin.putString("inputId", id.getText().toString());
                                                    autoLogin.putString("inputPwd", pwd.getText().toString());

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

                            @Override
                            public void onAuthenticationSucceeded(
                                    @NonNull BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                Toast.makeText(getApplicationContext(),
                                        "지문인식 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("member_id",member_id);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                Toast.makeText(getApplicationContext(), "지문인식 실패",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                loginId = null;
                                loginPwd = null;
                            }
                        });





                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                        .setTitle("지문 인증")
                                        .setSubtitle("기기에 등록된 지문을 이용하여 지문을 인증해주세요.")
                                        .setNegativeButtonText("취소")
                                        .setDeviceCredentialAllowed(false)
                                        .build();

                                //  사용자가 다른 인증을 이용하길 원할 때 추가하기
                                biometricPrompt.authenticate(promptInfo);
                                Toast.makeText(LoginActivity.this, loginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                                member.setMember_id(member_id);
                            }
                        });


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

                                        SharedPreferences.Editor autoLogin = auto.edit();
                                        autoLogin.putString("inputId", id.getText().toString());
                                        autoLogin.putString("inputPwd", pwd.getText().toString());

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
    public void regist(View view){
        Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
        startActivity(intent);
        finish();
    }
}