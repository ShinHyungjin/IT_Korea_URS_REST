package com.koreait.ursrest.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class Store implements Parcelable {
    private int store_id;
    private String store_name;
    private String store_location;
    private String store_phone;
    private String store_repimg;
    private String store_reservation;
    private String store_openhour;
    private String store_closehour;
    private String store_pass;
    private int category_id;
    private String store_account;
    private String store_image;
    private String store_info;
    private String store_regdate;
    private String store_tablecount;
    private int member_id;
    private String store_bank;
    private Bitmap bitmap_image;

    protected Store(Parcel in) {
        store_id = in.readInt();
        store_name = in.readString();
        store_location = in.readString();
        store_phone = in.readString();
        store_repimg = in.readString();
        store_reservation = in.readString();
        store_openhour = in.readString();
        store_closehour = in.readString();
        store_pass = in.readString();
        category_id = in.readInt();
        store_account = in.readString();
        store_image = in.readString();
        store_info = in.readString();
        store_regdate = in.readString();
        store_tablecount = in.readString();
        member_id = in.readInt();
        store_bank = in.readString();
    }

    public Store() {

    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(store_id);
        parcel.writeString(store_name);
        parcel.writeString(store_location);
        parcel.writeString(store_phone);
        parcel.writeString(store_repimg);
        parcel.writeString(store_reservation);
        parcel.writeString(store_openhour);
        parcel.writeString(store_closehour);
        parcel.writeString(store_pass);
        parcel.writeInt(category_id);
        parcel.writeString(store_account);
        parcel.writeString(store_image);
        parcel.writeString(store_info);
        parcel.writeString(store_regdate);
        parcel.writeString(store_tablecount);
        parcel.writeInt(member_id);
        parcel.writeString(store_bank);
    }
}
