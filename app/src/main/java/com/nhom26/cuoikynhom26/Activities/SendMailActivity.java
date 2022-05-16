package com.nhom26.cuoikynhom26.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.Class.JavaMailAPI;
import com.nhom26.cuoikynhom26.R;

public class SendMailActivity extends AppCompatActivity {

    String email = "long230796@gmail.com";

    private EditText subject, message;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.message);
        button = (Button) findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senEmail();
            }
        });
    }

    private void senEmail() {
        String mEmail = email;
        String mSubject = subject.getText().toString();
        String mMessage = message.getText().toString();

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();
        Toast.makeText(this, "Đã gửi", Toast.LENGTH_SHORT).show();
        finish();
    }
}
