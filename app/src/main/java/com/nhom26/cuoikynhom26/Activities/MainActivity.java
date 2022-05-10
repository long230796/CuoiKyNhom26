package com.nhom26.cuoikynhom26.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nhom26.cuoikynhom26.Activities.account.LoginActivity;
import com.nhom26.cuoikynhom26.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hienThiManHinhLogin();
    }
    private void hienThiManHinhLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
