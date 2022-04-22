package com.nhom26.cuoikynhom26.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import com.nhom26.cuoikynhom26.R;

public class AdminHomeActivity extends AppCompatActivity {

    TabHost tabHost;
    TabHost.TabSpec tb1, tb2, tb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        tb1 = tabHost.newTabSpec("tab1");
        tb1.setContent(R.id.tab1);
        tb1.setIndicator("Home", getResources().getDrawable(R.drawable.ic_beans));
        tabHost.addTab(tb1);

        tb2 = tabHost.newTabSpec("tab2");
        tb2.setContent(R.id.tab2);
        tb2.setIndicator("Search");
        tabHost.addTab(tb2);

        tb3 = tabHost.newTabSpec("tab3");
        tb3.setIndicator("User");
        tb3.setContent(R.id.tab3);
        tabHost.addTab(tb3);
    }
}
