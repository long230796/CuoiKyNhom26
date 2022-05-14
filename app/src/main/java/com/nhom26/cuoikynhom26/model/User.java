package com.nhom26.cuoikynhom26.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class User {

    String phone;
    String password;
    String ten;
    String vaitro;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getVaitro() {
        return vaitro;
    }

    public void setVaitro(String vaitro) {
        this.vaitro = vaitro;
    }

    public User() {
    }

    public User(String phone, String password, String ten, String vaitro) {
        this.phone = phone;
        this.password = password;
        this.ten = ten;
        this.vaitro = vaitro;
    }
//    // Trien khai Parcelable
//    protected User(Parcel in) {
//        this.phone = in.readString();
//        this.ten = in.readString();
//        this.password = in.readString();
//        this.vaitro = in.readString();
//    }
//
//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(phone);
//        parcel.writeString(ten);
//        parcel.writeString(password);
//        parcel.writeString(vaitro);
//    }
}
