package com.nhom26.cuoikynhom26.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.Activities.account.LoginActivity;
import com.nhom26.cuoikynhom26.R;

public class HomeActivity extends AppCompatActivity {

    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setIcon(R.drawable.ic_home_black_24dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        logout = (TextView) findViewById(R.id.txtLogout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getApplicationContext().getSharedPreferences("lastUser", 0).edit().clear().commit();
                hienThiManHinhLogin();

            }
        });

    }
    private void hienThiManHinhLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_option_menu, menu);

        MenuItem mnuSearch = menu.findItem(R.id.mnuSearch);
        SearchView searchView = (SearchView) mnuSearch.getActionView();

        return super.onCreateOptionsMenu(menu);
    }
}
