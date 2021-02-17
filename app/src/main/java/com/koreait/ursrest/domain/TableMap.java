package com.koreait.ursrest.domain;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class TableMap implements Parcelable {
    private int tablemap_id;
    private int store_id;
    private String map_index;
    private String unavailable;
    private String is_first;

    public TableMap() {

    }

    protected TableMap(Parcel in) {
        tablemap_id = in.readInt();
        store_id = in.readInt();
        map_index = in.readString();
        unavailable = in.readString();
        is_first = in.readString();
    }

    public static final Creator<TableMap> CREATOR = new Creator<TableMap>() {
        @Override
        public TableMap createFromParcel(Parcel in) {
            return new TableMap(in);
        }

        @Override
        public TableMap[] newArray(int size) {
            return new TableMap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(tablemap_id);
        parcel.writeInt(store_id);
        parcel.writeString(map_index);
        parcel.writeString(unavailable);
        parcel.writeString(is_first);
    }
}
