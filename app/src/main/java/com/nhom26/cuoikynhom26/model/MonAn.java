package com.nhom26.cuoikynhom26.model;

import java.io.Serializable;

/**
 * Created by long2 on 4/24/2022.
 */

public class MonAn implements Serializable {
    String mamon;
    String maloai;
    String mact;
    String tenmon;
    String mota;
    String anhminhhoa;
    String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public MonAn(String mamon, String maloai, String mact, String tenmon, String mota, String anhminhhoa, String link) {
        this.mamon = mamon;
        this.maloai = maloai;
        this.mact = mact;
        this.tenmon = tenmon;
        this.mota = mota;
        this.anhminhhoa = anhminhhoa;
        this.link = link;

    }

    public String getMamon() {
        return mamon;
    }

    public void setMamon(String mamon) {
        this.mamon = mamon;
    }

    public String getMaloai() {
        return maloai;
    }

    public void setMaloai(String maloai) {
        this.maloai = maloai;
    }

    public String getMact() {
        return mact;
    }

    public void setMact(String mact) {
        this.mact = mact;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getAnhminhhoa() {
        return anhminhhoa;
    }

    public void setAnhminhhoa(String anhminhhoa) {
        this.anhminhhoa = anhminhhoa;
    }

    public MonAn() {

    }

    public MonAn(String mamon, String maloai, String mact, String tenmon, String mota, String anhminhhoa) {

        this.mamon = mamon;
        this.maloai = maloai;
        this.mact = mact;
        this.tenmon = tenmon;
        this.mota = mota;
        this.anhminhhoa = anhminhhoa;
    }

    @Override
    public String toString() {
        return this.tenmon;
    }
}
