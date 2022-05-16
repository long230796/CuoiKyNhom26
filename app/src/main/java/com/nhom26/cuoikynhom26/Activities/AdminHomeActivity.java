package com.nhom26.cuoikynhom26.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.Activities.account.LoginActivity;
import com.nhom26.cuoikynhom26.Activities.account.UserControl;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.User;

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

    UserControl usrctl;
    User lastuser;
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastuser = usrctl.getLastUser(getApplicationContext());
        if (lastuser!=null){
            if (lastuser.getVaitro().equals("0")){
                setContentView(R.layout.activity_admin_home);
                processCopy();
                addControls();
                addEvents();
            }
        }
        else hienThiManHinhLogin();
//        setContentView(R.layout.activity_admin_home);
//        processCopy();
//        addControls();
//        addEvents();
    }

    private void hienThiManHinhLogin() {
        Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void addControls() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        tb1 = tabHost.newTabSpec("tab1");
        tb1.setContent(R.id.tab1);
        tb1.setIndicator("", getResources().getDrawable(R.drawable.ic_home_black_24dp));
        tabHost.addTab(tb1);

        tb2 = tabHost.newTabSpec("tab2");
        tb2.setContent(R.id.tab2);
        tb2.setIndicator("", getResources().getDrawable(R.drawable.ic_search_black_24dp));
        tabHost.addTab(tb2);

        tb3 = tabHost.newTabSpec("tab3");
        tb3.setIndicator("", getResources().getDrawable(R.drawable.ic_person_black_24dp));
        tb3.setContent(R.id.tab3);
        tabHost.addTab(tb3);

        layoutLoai = (LinearLayout) findViewById(R.id.layoutLoai);
        layoutNL = (LinearLayout) findViewById(R.id.layoutNL);
        layoutFood = (LinearLayout) findViewById(R.id.layoutFood);

        logout = (TextView) findViewById(R.id.txtLogout);
    }

    private void addEvents() {
        layoutNL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhNL();
            }
        });
        
        layoutLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhLoai();
            }
        });

        layoutFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiManHinhFood();
            }
        });
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getApplicationContext().getSharedPreferences("lastUser", 0).edit().clear().commit();
                hienThiManHinhLogin();
            }
        });
    }

    private void hienThiManHinhFood() {
        Intent intent = new Intent(AdminHomeActivity.this, MonAnActivity.class);
        startActivity(intent);
    }

    private void hienThiManHinhLoai() {
        Intent intent = new Intent(AdminHomeActivity.this, LoaiActivity.class);
        startActivity(intent);
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

    public void onShowUaThich(View view) {
        String phonenumber = "0123456789";
        Intent intent = new Intent(AdminHomeActivity.this, DanhSachUaThich.class);
        intent.putExtra("phonenumber", phonenumber);
        startActivity(intent);
    }
}
