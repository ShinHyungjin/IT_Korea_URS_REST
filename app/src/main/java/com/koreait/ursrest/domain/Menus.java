package com.koreait.ursrest.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class Menus implements Parcelable {
    private int menu_id;
    private int store_id;
    private String menu_name;
    private int menu_price;
    private String menu_image;
    private String menu_stock;
    private String m_image;
    private Bitmap bitmap_image;

    public Menus() {

    }


    protected Menus(Parcel in) {
        menu_id = in.readInt();
        store_id = in.readInt();
        menu_name = in.readString();
        menu_price = in.readInt();
        menu_image = in.readString();
        menu_stock = in.readString();
        m_image = in.readString();
    }

    public static final Creator<Menus> CREATOR = new Creator<Menus>() {
        @Override
        public Menus createFromParcel(Parcel in) {
            return new Menus(in);
        }

        @Override
        public Menus[] newArray(int size) {
            return new Menus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(menu_id);
        dest.writeInt(store_id);
        dest.writeString(menu_name);
        dest.writeInt(menu_price);
        dest.writeString(menu_image);
        dest.writeString(menu_stock);
        dest.writeString(m_image);
    }
}
