package com.koreait.ursrest.domain;

import android.os.Parcel;
import android.os.Parcelable;


import lombok.Data;

@Data
public class Member implements Parcelable {
    private int member_id;
    private String user_id;
    private String user_password;
    private String user_name;
    private String user_birthday;
    private String user_gender;
    private String user_email_id;
    private String user_email_server;
    private String user_phone;
    private String user_location;
    private String user_image;
    private String user_position;
    private String user_roulette;
    private String regdate;
    private String authkey;
    private String authstatus;

    public Member(Parcel in) {
        member_id = in.readInt();
        user_id = in.readString();
        user_password = in.readString();
        user_name = in.readString();
        user_birthday = in.readString();
        user_gender = in.readString();
        user_email_id = in.readString();
        user_email_server = in.readString();
        user_phone = in.readString();
        user_location = in.readString();
        user_image = in.readString();
        user_position = in.readString();
        user_roulette = in.readString();
        regdate = in.readString();
        authkey = in.readString();
        authstatus = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public Member() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(member_id);
        dest.writeString(user_id);
        dest.writeString(user_password);
        dest.writeString(user_name);
        dest.writeString(user_birthday);
        dest.writeString(user_gender);
        dest.writeString(user_email_id);
        dest.writeString(user_email_server);
        dest.writeString(user_phone);
        dest.writeString(user_location);
        dest.writeString(user_image);
        dest.writeString(user_position);
        dest.writeString(user_roulette);
        dest.writeString(regdate);
        dest.writeString(authkey);
        dest.writeString(authstatus);
    }
}