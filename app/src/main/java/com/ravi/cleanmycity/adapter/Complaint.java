package com.ravi.cleanmycity.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class Complaint implements Parcelable {
    private String address, id, time, url, detail;

    public Complaint() {
    }

    public Complaint(String address, String id, String time, String detail, String url) {
        this.address = address;
        this.id = id;
        this.time = time;
        this.detail = detail;
        this.url = url;
    }

    protected Complaint(Parcel in) {
        address = in.readString();
        id = in.readString();
        time = in.readString();
    }

    public static final Creator<Complaint> CREATOR = new Creator<Complaint>() {
        @Override
        public Complaint createFromParcel(Parcel in) {
            return new Complaint(in);
        }

        @Override
        public Complaint[] newArray(int size) {
            return new Complaint[size];
        }
    };

    public String getAddress() {
        return address;
    }
    public String getDetail() {
        return detail;
    }
    public String getId() {
        return id;
    }
    public String getTime() {
        return time;
    }
    public String getUrl() {
        return url;
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