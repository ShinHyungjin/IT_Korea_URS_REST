package com.koreait.ursrest.repository;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.Store;
import com.koreait.ursrest.domain.TableMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class TableMapDAO {
    String TAG = this.getClass().getName();
    String ip="192.168.0.71";
    int port = 8888;
    Gson gson = new Gson();
    MainActivity mainActivity;

    public TableMapDAO(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void selectAll(int store_id) {
        String uri = "/rest/member/tableMap/" + store_id;
        BufferedReader buffr = null;

        try {
            URL url = new URL("http://" + ip + ":" + port + uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            System.out.println("TableMap 접속 완료");

            buffr = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String data = null;
            StringBuilder sb = new StringBuilder();

            while (true) {
                data = buffr.readLine();
                if (data == null)
                    break;
                sb.append(data);
                System.out.println("Data : " + data);
            }
            JSONObject json = new JSONObject(sb.toString());
            TableMap tableMap = gson.fromJson(json.toString(), TableMap.class);

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("tableMap", tableMap);
            message.setData(bundle);
            mainActivity.tableMapHandler.sendMessage(message);

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

}
