package com.koreait.ursrest.domain;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class Receipt implements Parcelable {
    private int receipt_id;
    private int member_id;
    private int store_id;
    private String receipt_regdate;
    private int receipt_totalamount;
    private int menu_quantity;
    private String reservation_table;

    protected Receipt(Parcel in) {
        receipt_id = in.readInt();
        member_id = in.readInt();
        store_id = in.readInt();
        receipt_regdate = in.readString();
        receipt_totalamount = in.readInt();
        menu_quantity = in.readInt();
        reservation_table = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(receipt_id);
        dest.writeInt(member_id);
        dest.writeInt(store_id);
        dest.writeString(receipt_regdate);
        dest.writeInt(receipt_totalamount);
        dest.writeInt(menu_quantity);
        dest.writeString(reservation_table);
    }
}