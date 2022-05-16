package com.nhom26.cuoikynhom26.model;

/**
 * Created by apoll on 5/9/2022.
 */

public class LoaiSearch {
    private String ten;
    private String hinhAnh;

    public LoaiSearch() {
    }

    public LoaiSearch(String ten, String hinhAnh) {
        this.ten = ten;
        this.hinhAnh = hinhAnh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}