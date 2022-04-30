package com.nhom26.cuoikynhom26.model;

import java.io.Serializable;

/**
 * Created by long2 on 4/23/2022.
 */

public class Loai implements Serializable {
    String maloai;
    String tenloai;

    public String getMaloai() {
        return maloai;
    }

    public void setMaloai(String maloai) {
        this.maloai = maloai;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public Loai() {

    }

    public Loai(String maloai, String tenloai) {

        this.maloai = maloai;
        this.tenloai = tenloai;
    }

    @Override
    public String toString() {
        return this.tenloai;
    }
}
