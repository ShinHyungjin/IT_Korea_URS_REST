package com.koreait.ursrest.repository;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.domain.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {
    String TAG = this.getClass().getName();
    private static final String IP = "Insert Your IP!";
    int port = 8888;
    Gson gson = new Gson();
    MainActivity mainActivity;

    public StoreDAO(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void selectAll(int category_id) {
        String uri = "/rest/member/storeList/" + category_id;
        BufferedReader buffr = null;

        ArrayList<Store> storeList = new ArrayList<Store>();

        try {
            URL url = new URL("http://" + IP + ":" + port + uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            //보낼 데이터 구성

            buffr = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String data = null;
            StringBuilder sb = new StringBuilder();

            while (true) {
                data = buffr.readLine();
                if (data == null)
                    break;
                sb.append(data);
                Log.d(TAG, "data : " + data);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                Store store = gson.fromJson(json.toString(), Store.class);
                Log.d(TAG, "StoreName : " + store.getStore_name());
                String filename = store.getMember_id()+"M."+store.getStore_image();
                load(store,filename);
                storeList.add(store);
            }

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("storeList", storeList);
            message.setData(bundle);
            mainActivity.storeHandler.sendMessage(message);

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
        } finally {
            if(buffr != null) {
                try {
                    buffr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(Store store,String filename){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {


                    URL url = new URL("http://"+IP+":"+port+"/resources/data/store/"+filename);
                    InputStream is = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    store.setBitmap_image(bitmap);


                    /*
                    URL url = new URL("http://"+ip+":"+port+"/resources/data/store/"+filename);
                    InputStream is = url.openStream();

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    // Get bitmap dimensions before reading...
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(String.valueOf(url), opts);
                    int width = opts.outWidth;
                    int height = opts.outHeight;
                    int largerSide = Math.max(width, height);
                    opts.inJustDecodeBounds = false; // This time it's for real!
                    int sampleSize = 2; // Calculate your sampleSize here
                    opts.inSampleSize = sampleSize;
                    Bitmap bmp = BitmapFactory.decodeFile((String.valueOf(url)), opts);

                    store.setBitmap_image(bmp);
                    */

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