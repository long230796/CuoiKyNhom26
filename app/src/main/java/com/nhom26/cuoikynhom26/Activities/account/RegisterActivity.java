package com.nhom26.cuoikynhom26.Activities.account;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    String code;
    String phonenum, ten, pass, result = null;
    ArrayList<User> listUsr = new ArrayList<>();
    FirebaseAuth mAuth;



    private static String mVerificationId;
    private static PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        addControls();
        getUserFromDB();
        addEvents();

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);
                String code = phoneAuthCredential.getSmsCode();
                Log.d("JEJE", "SMS code:" +code);

                //luuUserVaoDB(phonenum, ten, pass);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("JEJE", e.toString()) ;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(RegisterActivity.this, "Mã OTP không hợp lệ!", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(RegisterActivity.this, "Yêu cầu OTP đạt giới hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(RegisterActivity.this, "Mã OTP đã được gửi", Toast.LENGTH_SHORT).show();
                mVerificationId = s;
                mResendToken = forceResendingToken;
                Log.d("JEJE", s) ;
            }
        };
    }

    private void hienThiManHinhLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
            String password = cursor.getString(1);
            String ten = cursor.getString(2);
            String vaitro = cursor.getString(3);
            User usr = new User(phone, password, ten, vaitro);
            listUsr.add(usr);
        }
        cursor.close();
    }

    private void addEvents() {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenum = phone.getText().toString();
                ten = name.getText().toString();
                pass = password.getText().toString();
                result = null;
                boolean flag =true;
                if (phonenum.matches("") | ten.matches("") | pass.matches("")) {
                    result = "Vui lòng nhập đầy đủ các trường!";
                    message.setText(result);
                    flag=false;
                }
                else {
                    for (int i = 0; i < listUsr.size(); i++) {
                        Log.d("Phonedb", String.valueOf(listUsr.get(0).getPhone()));
                        if (listUsr.get(i).getPhone().equals(phonenum)) {
                            result = "Số điện thoại này đã được đăng ký!";
                            message.setText(result);
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag == true){
                    message.setText("");
                    hienThiDialogXacThuc();
                }
            }
        });
    }

    public void verifyPhone(String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phoneNumber.substring(1),// Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallback
    }

    private void verifyCode(String otpcode) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpcode);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    luuUserVaoDB(phonenum, ten, pass);
                    Toast.makeText(RegisterActivity.this, "Xin mời đăng nhập để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
                    hienThiManHinhLogin();
                }else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(RegisterActivity.this, "Mã xác thực không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void hienThiDialogXacThuc() {
        Dialog dialogXacThuc = new Dialog(RegisterActivity.this);
        dialogXacThuc.setContentView(R.layout.dialog_xacthuc);
        final EditText edtOTP = dialogXacThuc.findViewById(R.id.edtOTP);
        Button btnXacThuc = dialogXacThuc.findViewById(R.id.btnXacThuc);
        verifyPhone(phonenum,mCallBacks);
        if (code != null) {
            edtOTP.setText(code);

        }
        btnXacThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpcode = edtOTP.getText().toString();
                if (otpcode.isEmpty()|otpcode.length()!=6){
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyCode(otpcode);
                }
            }
        });
        dialogXacThuc.show();
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
