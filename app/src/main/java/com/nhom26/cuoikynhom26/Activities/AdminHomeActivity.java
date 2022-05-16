package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.Activities.account.LoginActivity;
import com.nhom26.cuoikynhom26.Activities.account.UserControl;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.User;

public class AdminHomeActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "dbCuoiKyNhom26.db";
    public static SQLiteDatabase database = null;
    String DB_PATH_SUFFIX = "/databases/";

    TabHost tabHost;
    TabHost.TabSpec tb1, tb2, tb3;

    LinearLayout layoutNL;
    LinearLayout layoutLoai;
    LinearLayout layoutFood;

    Button btn_uathich;
    TextView logout;
    TextView txtTenUser;
    TextView txtPhone;
    UserControl usrctl;
    User lastUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastUser = usrctl.getLastUser(getApplicationContext());
        if (lastUser!=null){
            if (lastUser.getVaitro().equals("0")){
                setContentView(R.layout.activity_admin_home);
                addControls();
                addEvents();
            }
        }
        else hienThiManHinhLogin();
    }

    private void hienThiManHinhLogin() {
        Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void addControls() {

        logout = (TextView) findViewById(R.id.txtLogout);
        lastUser=usrctl.getLastUser(getApplicationContext());

        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtTenUser = (TextView) findViewById(R.id.txtTenUser);
        btn_uathich = (Button) findViewById(R.id.btn_uathich);

        txtPhone.setText(lastUser.getPhone());
        txtTenUser.setText(lastUser.getTen());

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        tb1 = tabHost.newTabSpec("tab1");
        tb1.setContent(R.id.tab1);
        tb1.setIndicator("", getResources().getDrawable(R.drawable.ic_home_black_24dp));
        tabHost.addTab(tb1);

        tb3 = tabHost.newTabSpec("tab3");
        tb3.setIndicator("", getResources().getDrawable(R.drawable.ic_person_black_24dp));
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

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getApplicationContext().getSharedPreferences("lastUser", 0).edit().clear().commit();
                AdminHomeActivity.this.finish();
                hienThiManHinhLogin();
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

        btn_uathich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowUaThich();
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



    public void onShowUaThich() {
        String phonenumber = lastUser.getPhone();
        Intent intent = new Intent(AdminHomeActivity.this, DanhSachUaThich.class);
        intent.putExtra("phonenumber", phonenumber);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuChitiet:
                thongTinPhanMem();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void thongTinPhanMem() {
        Dialog dialogThongTinPhanMem = new Dialog(AdminHomeActivity.this);
        dialogThongTinPhanMem.setContentView(R.layout.dialog_thongtinphanmem);

        Button btnLienHe = dialogThongTinPhanMem.findViewById(R.id.btnLienHe);

        btnLienHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(AdminHomeActivity.this, SendMailActivity.class);
                startActivity(mailIntent);
            }
        });
        dialogThongTinPhanMem.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lienhe_option_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
