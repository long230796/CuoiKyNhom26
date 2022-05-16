package com.nhom26.cuoikynhom26.Activities.account;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "dbCuoiKyNhom26.db";
    public static SQLiteDatabase database = null;
    String DB_PATH_SUFFIX = "/databases/";

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
            processCopy();
            addControls();
            getUserFromDB();
            addEvents();
        }
        else {
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
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
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


    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset();
                Toast.makeText(this, "Copying success from Assets folder" , Toast.LENGTH_LONG).show();

            }
            catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDataBasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void copyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDataBasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOuput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOuput.write(buffer, 0, length);
            }
            myOuput.flush();
            myOuput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
