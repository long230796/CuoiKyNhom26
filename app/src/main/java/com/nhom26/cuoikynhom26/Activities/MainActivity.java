package com.nhom26.cuoikynhom26.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.Activities.account.LoginActivity;
import com.nhom26.cuoikynhom26.Activities.account.UserControl;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.User;

public class MainActivity extends AppCompatActivity {

    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }



}
