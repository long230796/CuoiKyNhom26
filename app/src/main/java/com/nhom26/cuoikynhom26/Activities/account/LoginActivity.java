package com.nhom26.cuoikynhom26.Activities.account;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.Activities.AdminHomeActivity;
import com.nhom26.cuoikynhom26.Activities.HomeActivity;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    EditText phone, password;
    TextView message, forgetpass, reglink;
    Button btnLogin;
    ArrayList<User> listUsr = new ArrayList<>();
    UserControl usrctl;
    User user,lastuser;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastuser=usrctl.getLastUser(getApplicationContext());
        if (lastuser==null){
            setContentView(R.layout.activity_login);
            addControls();
            getUserFromDB();
            addEvents();
        }
        else {
            Toast.makeText(LoginActivity.this, "Đăng nhập với số điện thoại " + lastuser.getPhone(), Toast.LENGTH_SHORT).show();
            if (lastuser.getVaitro().equals("0")){
                hienThiManHinhAdmin();
            }
            else hienThiManHinhHome();
        }


    }

    private void hienThiManHinhHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void hienThiManHinhAdmin() {
        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void addControls() {
        phone = (EditText) findViewById(R.id.edtUsername);
        password = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        forgetpass = (TextView) findViewById(R.id.txtForgetPass);
        reglink = (TextView) findViewById(R.id.txtReglink);
        message = (TextView) findViewById(R.id.txtMessage);
    }

    private void getUserFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM USER", null);
        listUsr.clear();
        while (cursor.moveToNext()) {
            String phone = cursor.getString(0);
            //Log.d("Phone",phone);
            String password = cursor.getString(1);
            //Log.d("Pass",password);
            String ten = cursor.getString(2);
            String vaitro = cursor.getString(3);
            User usr = new User(phone, password, ten, vaitro);
            //Log.d("Phone",String.valueOf(usr.getPhone()));
            listUsr.add(usr);

        }
        cursor.close();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = xacThucUser();
                if (user!=null){
                    usrctl.saveObjectToSharedPreference(getApplicationContext(), "lastUser", "user", user); //luu user xuong shared preference
                    Toast.makeText(LoginActivity.this, "Đăng nhập với vai trò " + user.getVaitro(), Toast.LENGTH_SHORT).show();
                    if (user.getVaitro().equals("0")){
                        hienThiManHinhAdmin();
                    }
                    else hienThiManHinhHome();
                }
            }
        });
        reglink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hienThiDangKy();
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hienThiResetPass();
            }
        });
    }

    private User xacThucUser(){
        String usrn = phone.getText().toString();
        String pass = password.getText().toString();
        for (int i=0;i<listUsr.size(); i++){

            if (listUsr.get(i).getPhone().equals(usrn) && listUsr.get(i).getPassword().equals(pass)){
                return listUsr.get(i);
            }
        }
        String result = "Thông tin đăng nhập không đúng!";
        message.setText(result);
        return null;
    }
    private void hienThiDangKy(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        //getApplicationContext().getSharedPreferences("lastUser", 0).edit().clear().commit(); //clear lastUser preference
        startActivity(intent);
    }
    private void hienThiResetPass(){
        Intent intent = new Intent(LoginActivity.this, ResetPassActivity.class);
        startActivity(intent);
    }
}
