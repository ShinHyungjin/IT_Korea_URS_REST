package com.koreait.ursrest;


import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MemberDAO {
    String TAG=this.getClass().getName();
    String ip="192.168.0.71";
    int port=8888;
    Gson gson=new Gson();
    LoginActivity loginActivity;
    public MemberDAO(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
    }

    public int login(Member Member) throws MemberLoginExeption {
        String uri="/rest/member";
        BufferedWriter buffw=null; //서버에 보낼 것이므로...
        BufferedReader buffr=null;
        int member_id = 0;
        try {
            URL url = new URL("http://"+ip+":"+port+uri);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");
            con.setDoOutput(true);
            //보낼 데이터 구성

            buffw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));


            //이 자바 --> json 으로.... Gson
            String jsonString = gson.toJson(Member);
            buffw.write(jsonString);
            buffw.flush();
            int code = con.getResponseCode();
            if(code !=200){
                Log.d(TAG,"로그인 실패");
                loginActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(loginActivity, "로그인 정보가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //연결이 끊어지기 전에 스트림으로 데이터 가져오기
                buffr = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String data=null; //한줄을 받을 임시 데이터

                member_id = Integer.parseInt(buffr.readLine());

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(buffw!=null){
                try {
                    buffw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return member_id;
    }

}
