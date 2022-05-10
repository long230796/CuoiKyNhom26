package com.nhom26.cuoikynhom26.Activities.account;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.Activities.AdminHomeActivity;
import com.nhom26.cuoikynhom26.Activities.MainActivity;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText phone, password;
    TextView message, forgetpass, reglink;
    Button btnLogin;
    ArrayList<User> listUsr = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        getUserFromDB();
        addEvents();
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
        //Log.d("listUsr(0) Phone",String.valueOf(listUsr.get(0).getPhone()));
        cursor.close();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xacThucUser();
            }
        });
        reglink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hienThiDangKy();
            }
        });
    }

    private User xacThucUser(){
        String usrn = phone.getText().toString();
        String pass = password.getText().toString();
        //Log.d("Phone","=================");
        Log.d("Login phone",usrn);
        Log.d("login pass",pass);
        Log.d("listUser(0) phone",String.valueOf(listUsr.get(0).getPhone()));
        for (int i=0;i<listUsr.size(); i++){
//            String a = listUsr.get(i).getPhone();
//            String b = listUsr.get(i).getPassword();
//
//            Log.i("Phone",a);Log.i("Phone",b);

            if (listUsr.get(i).getPhone().equals(usrn) && listUsr.get(i).getPassword().equals(pass)){

                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                return listUsr.get(i);
            }
        }
        String result = "Thông tin đăng nhập không đúng!";
        message.setText(result);
        return null;
    }
    private void hienThiDangKy(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
