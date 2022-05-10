package com.nhom26.cuoikynhom26.model;

import java.io.Serializable;


public class User implements Serializable {


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
}
