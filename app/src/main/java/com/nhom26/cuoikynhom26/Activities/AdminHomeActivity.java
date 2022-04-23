package com.nhom26.cuoikynhom26.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AdminHomeActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "dbCuoiKyNhom26.db";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    TabHost tabHost;
    TabHost.TabSpec tb1, tb2, tb3;

    LinearLayout layoutNL;
    LinearLayout layoutLoai;
    LinearLayout layoutFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        processCopy();
        addControls();
        addEvents();
    }



    private void addControls() {
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

        layoutLoai = (LinearLayout) findViewById(R.id.layoutLoai);
        layoutNL = (LinearLayout) findViewById(R.id.layoutNL);
        layoutFood = (LinearLayout) findViewById(R.id.layoutFood);
    }

    private void addEvents() {
        layoutNL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhNL();
            }
        });
    }

    private void hienThiManHinhNL() {
        Intent intent = new Intent(AdminHomeActivity.this, NguyenLieuActivity.class);
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
