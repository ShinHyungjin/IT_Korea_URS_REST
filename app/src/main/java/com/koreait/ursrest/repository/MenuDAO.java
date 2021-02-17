package com.koreait.ursrest.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.Menus;
import com.koreait.ursrest.domain.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MenuDAO {
    String TAG = this.getClass().getName();
    String ip="192.168.0.71";
    int port = 8888;
    Gson gson = new Gson();
    MainActivity mainActivity;

    public MenuDAO(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void selectAll(int store_id){
        String uri = "/rest/member/menuList/"+store_id;
        BufferedReader buffr = null;
        ArrayList<Menus> menusArrayList = new ArrayList<Menus>();

        try {
            URL url = new URL("http://" + ip + ":" + port + uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setDoOutput(true);
            //보낼 데이터 구성

            buffr = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            //Log.d(TAG,buffr.readLine());
            String data = null;
            StringBuilder sb = new StringBuilder();

            while (true) {
                data = buffr.readLine();
                if (data == null)
                    break;
                sb.append(data);
                Log.d(TAG, "얻어온 메뉴들은 : " + data);
            }
            Log.d(TAG, "StringBuilder : " + sb.toString());

            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                Menus menus = gson.fromJson(json.toString(), Menus.class);
                String filename = menus.getMenu_id()+"."+menus.getMenu_image();

                load(menus,filename);

                menusArrayList.add(menus);
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("menuList", menusArrayList);
            message.setData(bundle);
            mainActivity.menuHandler.sendMessage(message);

            con.getResponseCode();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void load(Menus menus,String filename){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://"+ip+":"+port+"/resources/data/menu/"+filename);
                    InputStream is = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    menus.setBitmap_image(bitmap);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }


}
