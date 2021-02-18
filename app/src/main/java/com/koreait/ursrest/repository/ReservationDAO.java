package com.koreait.ursrest.repository;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.Paybill;
import com.koreait.ursrest.exception.MemberLoginExeption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReservationDAO {
    String TAG = this.getClass().getName();
    private static final String IP = "Insert Your IP!";
    int port = 8888;
    Gson gson = new Gson();
    MainActivity mainActivity;

    public ReservationDAO(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public int pay(Paybill paybill){
        String uri="/rest/member/paybill";
        BufferedWriter buffw=null; //서버에 보낼 것이므로...
        BufferedReader buffr = null;
        int receipt_id = 0 ;
        try {
            URL url = new URL("http://"+IP+":"+port+uri);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");
            con.setDoOutput(true);
            //보낼 데이터 구성
            buffw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));

            //이 자바 --> json 으로.... Gson
            String jsonString = gson.toJson(paybill);
            buffw.write(jsonString);
            buffw.flush();
            int code = con.getResponseCode();
            if(code !=200){
                Log.d(TAG,"등록 실패");
                throw new MemberLoginExeption("등록 실패");
            }else{
                buffr = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data = null;
                StringBuilder sb = new StringBuilder();

                while (true) {
                    data = buffr.readLine();
                    if (data == null)
                        break;
                    sb.append(data);
                }

                receipt_id = Integer.parseInt(sb.toString());
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

        return receipt_id;
    }



}
