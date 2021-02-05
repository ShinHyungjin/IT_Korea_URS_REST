package com.koreait.ursrest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SubActivity extends Activity {
    String TAG = this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sub);

            Button logout = (Button)findViewById(R.id.logout);
            Intent intent = getIntent();

            logout.setText(Integer.toString(intent.getExtras().getInt("member_id")));
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                    //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                    Intent intent = new Intent(SubActivity.this, MainActivity.class);
                    startActivity(intent);
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = auto.edit();
                    //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                    editor.clear();
                    editor.commit();

                    Toast.makeText(SubActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
}