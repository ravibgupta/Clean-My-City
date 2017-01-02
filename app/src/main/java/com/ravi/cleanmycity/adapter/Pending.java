package com.ravi.cleanmycity.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class Pending implements Parcelable {
    private String address, id, time, path;

    public Pending() {
    }

    public Pending(String path, String address, String id, String time) {
        this.path = path;
        this.address = address;
        this.id = id;
        this.time = time;
    }

    protected Pending(Parcel in) {
        address = in.readString();
        id = in.readString();
        time = in.readString();
    }

    public static final Creator<Pending> CREATOR = new Creator<Pending>() {
        @Override
        public Pending createFromParcel(Parcel in) {
            return new Pending(in);
        }

        @Override
        public Pending[] newArray(int size) {
            return new Pending[size];
        }
    };

    public String getPath() {
        return path;
    }
    public String getAddress() {
        return address;
    }
    public String getId() {
        return id;
    }
    public String getTime() {
        return time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(id);
        dest.writeString(time);
    }
}