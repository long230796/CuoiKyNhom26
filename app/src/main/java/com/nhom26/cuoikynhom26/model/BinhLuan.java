package com.nhom26.cuoikynhom26.model;

import java.io.Serializable;

/**
 * Created by long2 on 5/13/2022.
 */

public class BinhLuan implements Serializable{
    String mamon;
    String phone;
    String noidung;
    String ngaygio;

    public BinhLuan() {
    }

    public BinhLuan(String mamon, String phone, String noidung, String ngaygio) {
        this.mamon = mamon;
        this.phone = phone;
        this.noidung = noidung;
        this.ngaygio = ngaygio;
    }

    public String getMamon() {
        return mamon;
    }

    public void setMamon(String mamon) {
        this.mamon = mamon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getNgaygio() {
        return ngaygio;
    }

    public void setNgaygio(String ngaygio) {
        this.ngaygio = ngaygio;
    }
}
