package com.nhom26.cuoikynhom26.model;

import java.io.Serializable;

/**
 * Created by long2 on 4/22/2022.
 */

public class NguyenLieu implements Serializable {
    String manl;
    String tennl;
    String donvi;
    String dinhluong;

    public NguyenLieu(String manl, String tennl, String donvi) {
        this.manl = manl;
        this.tennl = tennl;
        this.donvi = donvi;
    }

    public NguyenLieu() {
    }

    public String getManl() {
        return manl;
    }

    public void setManl(String manl) {
        this.manl = manl;
    }

    public String getTennl() {
        return tennl;
    }

    public void setTennl(String tennl) {
        this.tennl = tennl;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public void setDinhluong(String dinhluong) {
        this.dinhluong = dinhluong;
    }

    public String getDinhluong() {

        return dinhluong;
    }

    @Override
    public String toString() {
        return this.tennl;
    }
}
