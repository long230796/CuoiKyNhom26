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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nhom26.cuoikynhom26.Activities.AdminHomeActivity;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.User;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ResetPassActivity extends AppCompatActivity {

    EditText phone ;
    TextView message;
    Button btnSentOTP;
    String code;
    String phonenum, ten, pass, result = null;
    ArrayList<User> listUsr = new ArrayList<>();
    FirebaseAuth mAuth;

    User usr;

    Dialog dialogXacThuc, dialogDoiPass;
    private static String mVerificationId;
    private static PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        mAuth = FirebaseAuth.getInstance();
        addControls();
        getUserFromDB();
        addEvents();

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Log.d("JEJE", "onVerificationCompleted:" + phoneAuthCredential);
                String code = phoneAuthCredential.getSmsCode();
                Log.d("JEJE", "SMS code:" +code);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(ResetPassActivity.this, "Mã OTP không hợp lệ!", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(ResetPassActivity.this, "Yêu cầu OTP đạt giới hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(ResetPassActivity.this, "Mã OTP đã được gửi", Toast.LENGTH_SHORT).show();
                mVerificationId = s;
                mResendToken = forceResendingToken;
            }
        };
    }

    private void addControls() {
        phone = (EditText) findViewById(R.id.edtUserPhone);
        btnSentOTP = (Button) findViewById(R.id.btnSentOTP);
        message = (TextView) findViewById(R.id.txtMessage);
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
        btnSentOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenum = phone.getText().toString();

                result = null;
                boolean flag =false;
                if (phonenum.isEmpty()&& phonenum.length()!=10) {
                    result = "Định dạng số điện thoại không hợp lệ";
                    message.setText(result);
                }
                else {
                    for (int i = 0; i < listUsr.size(); i++) {
                        if (listUsr.get(i).getPhone().equals(phonenum)) {
                            flag = true;
                            usr=listUsr.get(i);
                            hienThiDialogXacThuc();
                            break;
                        }
                    }
                }
                if (flag != true){
                    message.setText("Số điện thoại này chưa được đăng ký!");
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
    private void hienThiManHinhLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // destroy this activity
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResetPassActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    dialogXacThuc.dismiss();
                    if (usr!=null){
                        hienThiDialogDoiPass();
                    }
                }else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(ResetPassActivity.this, "Mã xác thực không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void hienThiDialogXacThuc() {
        dialogXacThuc = new Dialog(ResetPassActivity.this);
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
                    Toast.makeText(ResetPassActivity.this, otpcode, Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyCode(otpcode);
                }
            }
        });
        dialogXacThuc.show();
    }

    private void hienThiDialogDoiPass() {
        dialogDoiPass = new Dialog(ResetPassActivity.this);
        dialogDoiPass.setContentView(R.layout.dialog_new_pass);
        final EditText newPass = dialogDoiPass.findViewById(R.id.edtNewPass);
        Button btnSavePass = dialogDoiPass.findViewById(R.id.btnSavePass);
        //TextView txtSDT = dialogDoiPass.findViewById(R.id.txtSDT);
        //TextView txtTen = dialogDoiPass.findViewById(R.id.txtTen);
        //txtSDT.setText(usr.getPhone());
        //txtTen.setText(usr.getTen());Log.d("jjjj",usr.getPhone());

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = newPass.getText().toString();
                if (pass.isEmpty()){
                    Toast.makeText(ResetPassActivity.this, "Mời nhập mật khẩu", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean checkupdate=updateUser(pass);
                    if (checkupdate == true){
                        Toast.makeText(ResetPassActivity.this, "Xin mời đăng nhập lại để sử dụng", Toast.LENGTH_SHORT).show();
                        hienThiManHinhLogin();
                    }
                }
            }
        });
        dialogDoiPass.show();
    }

    private boolean updateUser(String pass) {

        ContentValues values = new ContentValues();
        values.put("PASSWORD", pass);
        long kq = AdminHomeActivity.database.update("USER", values, "PHONE=?",new String []{usr.getPhone()});
        if (kq > 0) {
            Toast.makeText(this, "Tài khoản đã được cập nhật", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
