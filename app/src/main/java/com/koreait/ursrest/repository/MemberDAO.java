package com.koreait.ursrest.repository;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koreait.ursrest.activity.LoginActivity;
import com.koreait.ursrest.activity.MainActivity;
import com.koreait.ursrest.activity.RegistActivity;
import com.koreait.ursrest.domain.Member;
import com.koreait.ursrest.domain.Receipt;
import com.koreait.ursrest.domain.Store;
import com.koreait.ursrest.exception.MemberLoginExeption;
import com.koreait.ursrest.fragment.body.MyPageFragment;

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
import java.nio.Buffer;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class MemberDAO {
    String TAG=this.getClass().getName();
    String ip="192.168.0.71";
    int port=8888;
    Gson gson=new Gson();
    LoginActivity loginActivity;
    RegistActivity registActivity;
    MainActivity mainActivity;
    MyPageFragment myPageFragment;
    public MemberDAO(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
    }

    public MemberDAO(RegistActivity registActivity){this.registActivity = registActivity;}

    public MemberDAO(MainActivity mainActivity){this.mainActivity = mainActivity;}

    public MemberDAO(MyPageFragment myPageFragment){this.myPageFragment = myPageFragment;}

    public int login(Member Member) throws MemberLoginExeption {
        String uri="/rest/member";
        BufferedWriter buffw=null; //서버에 보낼 것이므로...
        BufferedReader buffr=null;
        String errorMessage = null;
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

                //연결이 끊어지기 전에 스트림으로 데이터 가져오기
                buffr = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String data=null; //한줄을 받을 임시 데이터
                while(true){
                    data = buffr.readLine();
                    if(data==null)break;
                    sb.append(data);
                }

                if(contains(sb,"로그인 정보가 올바르지 않습니다.")) {
                    errorMessage = "로그인 정보가 올바르지 않습니다.";
                }else if(contains(sb,"이메일 인증이 완료되지않았습니다 이메일을 확인해주세요")){
                    errorMessage = "이메일 인증이 완료되지않았습니다 이메일을 확인해주세요.";
                }

                String finalErrorMessage = errorMessage;
                loginActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(loginActivity, finalErrorMessage, Toast.LENGTH_SHORT).show();
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

    public void RESTregist(Member member) throws MemberLoginExeption{
        String uri="/rest/member/regist";
        BufferedWriter buffw=null; //서버에 보낼 것이므로...
        try {
            URL url = new URL("http://"+ip+":"+port+uri);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");
            con.setDoOutput(true);
            //보낼 데이터 구성
            buffw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));

            //이 자바 --> json 으로.... Gson
            String jsonString = gson.toJson(member);
            buffw.write(jsonString);
            buffw.flush();
            int code = con.getResponseCode();
            if(code !=200){
                Log.d(TAG,"등록 실패");
                throw new MemberLoginExeption("등록 실패");
            }else{

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
    }

    public Member getDetail(int member_id){
        String uri = "/rest/member/" + member_id;
        BufferedReader buffr = null;

        Member member = new Member();

        try {
            URL url = new URL("http://" + ip + ":" + port + uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setDoOutput(true);
            //보낼 데이터 구성

            buffr = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String data = null;
            StringBuilder sb = new StringBuilder();

            while (true) {
                data = buffr.readLine();
                if (data == null)
                    break;
                sb.append(data);
            }

            member = gson.fromJson(sb.toString(), Member.class);

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("member",member);
            message.setData(bundle);

            myPageFragment.testHandler.sendMessage(message);

            con.getResponseCode();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return member;
    }

    public void updateMember(Member member) throws MemberLoginExeption{
        String uri = "/rest/member/";
        BufferedWriter buffw=null; //데이터 전송용 스트림

        try {
            URL url = new URL("http://"+ip+":"+port+uri);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");
            con.setDoOutput(true);//서버에 데이터를 보낼때는 이 옵션을 준다(POST, PUT)
            buffw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(),"UTF-8"));
            String jsonString = gson.toJson(member);

            buffw.write(jsonString);
            buffw.flush();

            int code = con.getResponseCode(); //요청 및 응답

            if(code !=200){
                throw new MemberLoginExeption("수정실패");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(buffw!=null){
                try {
                    buffw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getReceipt(int member_id){
        String uri="/rest/member/receipt/"+member_id;
        BufferedReader buffr = null;
        ArrayList<Receipt> receiptArrayList = new ArrayList<Receipt>();
        ArrayList<Store> storeList = new ArrayList<Store>();


        try {
            URL url = new URL("http://" + ip + ":" + port + uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setDoOutput(true);
            //보낼 데이터 구성

            buffr = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String data = null;
            StringBuilder sb = new StringBuilder();

            while (true) {
                data = buffr.readLine();
                if (data == null)
                    break;
                sb.append(data);
            }


            JSONArray jsonArray = new JSONArray(sb.toString());
            Log.d(TAG,"데이터 : " + jsonArray.length());
            for (int i = 0; i <2 ; i++) {
                Log.d(TAG,"데이터 : "+i + jsonArray.get(i));

                if(i == 0 ) {
                    for(int a = 0 ; a < jsonArray.getJSONArray(i).length(); a++) {
                        JSONObject json = (JSONObject) jsonArray.getJSONArray(i).get(a);
                        Receipt receipt = gson.fromJson(json.toString(), Receipt.class);

                        receiptArrayList.add(receipt);

                    }
                }else{
                    for(int a = 0 ; a < jsonArray.getJSONArray(i).length(); a++) {
                        JSONObject json = (JSONObject) jsonArray.getJSONArray(i).get(a);
                        Store store = gson.fromJson(json.toString(), Store.class);
                        String filename = store.getMember_id()+"M."+store.getStore_image();
                        load(store,filename);
                        storeList.add(store);
                    }
                }
            }

            Log.d(TAG,"영수증 갯수" + receiptArrayList.size());
            Log.d(TAG,"가게 갯수" + storeList.size());

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("receiptList", receiptArrayList);
            bundle.putParcelableArrayList("storeList",storeList);
            message.setData(bundle);
            mainActivity.recieptHandler.sendMessage(message);


            int code = con.getResponseCode(); //요청 및 응답

            if(code !=200){
                throw new MemberLoginExeption("불러오기 실패");
            }

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



    public int checkId(String user_id){
        String uri="/rest/member/checkId";
        BufferedWriter buffw=null; //서버에 보낼 것이므로...
        BufferedReader buffr=null;
        int result = 0 ;
        try {
            URL url = new URL("http://"+ip+":"+port+uri);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");
            con.setDoOutput(true);
            //보낼 데이터 구성
            buffw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));

            //이 자바 --> json 으로.... Gson
            String jsonString = gson.toJson(user_id);
            buffw.write(jsonString);
            buffw.flush();
            int code = con.getResponseCode();
            if(code !=200){
                Log.d(TAG,"아이디가 중복 됩니다");
            }else{

                //연결이 끊어지기 전에 스트림으로 데이터 가져오기
                buffr = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String data=null; //한줄을 받을 임시 데이터

                result = Integer.parseInt(buffr.readLine());
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

        return result;
    }



    public static boolean contains(StringBuilder sb , String findString){
        return sb.indexOf(findString) > -1;
    }

    public void load(Store store,String filename){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://"+ip+":"+port+"/resources/data/store/"+filename);
                    InputStream is = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    store.setBitmap_image(bitmap);

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