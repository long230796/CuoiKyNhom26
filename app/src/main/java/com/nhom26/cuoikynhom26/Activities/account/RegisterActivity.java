package com.nhom26.cuoikynhom26.Activities.account;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.*;

import com.nhom26.cuoikynhom26.Activities.AdminHomeActivity;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.User;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    EditText name, phone, password ;
    TextView message;
    Button btnReg;
    ArrayList<User> listUsr = new ArrayList<>();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        addControls();
        getUserFromDB();
        addEvents();

    }



    private void phoneAuth(String num){

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(num)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void addControls() {
        name = (EditText) findViewById(R.id.edtRegName);
        phone = (EditText) findViewById(R.id.edtRegPhone);
        password = (EditText) findViewById(R.id.edtRegPass);
        btnReg = (Button) findViewById(R.id.btnReg);

        message = (TextView) findViewById(R.id.txtRegMessage);

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
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taoUserMoi();
            }
        });
    }

    private boolean taoUserMoi(){
        String phonenum = phone.getText().toString();
        String ten = name.getText().toString();
        String pass = password.getText().toString();
        String result = null;
        if (phonenum.matches("") | ten.matches("") | pass.matches("")) {
            result = "Vui lòng nhập đầy đủ các trường!";
            message.setText(result);
            return false;
        }
        Log.d("Phone",String.valueOf(phonenum));
        for (int i=0;i<listUsr.size(); i++){
            Log.d("Phonedb",String.valueOf(listUsr.get(0).getPhone()));
            if (listUsr.get(i).getPhone().equals(phonenum)){
                result = "Số điện thoại này đã được đăng ký!";
                message.setText(result);
                return false;
            }

        }
        luuUserVaoDB(phonenum, ten, pass);
        return true;
    }
    private void luuUserVaoDB(String phone, String name, String pass) {

            ContentValues values = new ContentValues();
            values.put("PHONE",phone);
            values.put("PASSWORD", pass);
            values.put("TEN", name);
            values.put("VAITRO", 1);
            long kq = AdminHomeActivity.database.insert("USER", null, values);
            if (kq > 0) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
    }
}
