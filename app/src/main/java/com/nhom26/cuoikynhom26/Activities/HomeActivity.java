package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.Activities.account.LoginActivity;
import com.nhom26.cuoikynhom26.Activities.account.UserControl;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.adapter.LoaiAdapterHome;
import com.nhom26.cuoikynhom26.adapter.MonAnAdapterHome;
import com.nhom26.cuoikynhom26.model.Loai;
import com.nhom26.cuoikynhom26.model.LoaiSearch;
import com.nhom26.cuoikynhom26.model.MonAn;
import com.nhom26.cuoikynhom26.model.User;

public class HomeActivity extends AppCompatActivity {


    TabHost tabHost;
    TabHost.TabSpec tb1, tb2, tb3;
    LoaiAdapterHome loaiAdapterHome;
    ArrayAdapter<Loai> loaiAdapter;
    MonAnAdapterHome monAnAdapterH;
    ArrayAdapter<MonAn> monAnAdapter;
    ListView lvLoaiHome;
    ListView lvMonAnHome;
    Button btn_uathich;
    TextView logout;
    TextView txtTenUser;
    TextView txtPhone;
    UserControl usrctl;
    User lastUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setIcon(R.drawable.ic_home_black_24dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvMonAnHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MonAn monAn = monAnAdapterH.getItem(i);
                monAn.setAnhminhhoa("");
                Intent intent = new Intent(HomeActivity.this, ChiTietMonAnActivity.class);
                intent.putExtra("selectedMonAn",monAn);
                startActivity(intent);
            }
        });
        lvLoaiHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Loai loai = loaiAdapter.getItem(i);
                Intent intent = new Intent(HomeActivity.this, DanhSachMonTheoLoaiActivity.class);
                intent.putExtra("selectedLoai",loai);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getApplicationContext().getSharedPreferences("lastUser", 0).edit().clear().commit();
                HomeActivity.this.finish();
                hienThiManHinhLogin();
            }
        });

        btn_uathich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowUaThich();
            }
        });
    }

    private void hienThiManHinhLogin() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void addControls() {

        logout = (TextView) findViewById(R.id.txtLogout);
        lastUser=usrctl.getLastUser(getApplicationContext());

        btn_uathich = (Button) findViewById(R.id.btn_uathich);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtTenUser = (TextView) findViewById(R.id.txtTenUser);

        txtPhone.setText(lastUser.getPhone());
        txtTenUser.setText(lastUser.getTen());

        tabHost = (TabHost) findViewById(R.id.tabHostHome);
        tabHost.setup();

        tb1 = tabHost.newTabSpec("tab1Home");
        tb1.setContent(R.id.tab1Home);
        tb1.setIndicator("", getResources().getDrawable(R.drawable.ic_home_black_24dp));
        tabHost.addTab(tb1);

        tb2 = tabHost.newTabSpec("tab2Home");
        tb2.setContent(R.id.tab2Home);
        tb2.setIndicator("", getResources().getDrawable(R.drawable.ic_search_black_24dp));
        tabHost.addTab(tb2);

        tb3 = tabHost.newTabSpec("tab3Home");
        tb3.setIndicator("", getResources().getDrawable(R.drawable.ic_person_black_24dp));
        tb3.setContent(R.id.tab3Home);
        tabHost.addTab(tb3);

        //tab1
        lvLoaiHome = (ListView) findViewById(R.id.lvLoaiHome);
        loaiAdapterHome = new LoaiAdapterHome(HomeActivity.this, R.layout.item_loai);
        lvLoaiHome.setAdapter(loaiAdapterHome);
        loaiAdapter = new ArrayAdapter<Loai>(HomeActivity.this, android.R.layout.simple_list_item_1);
        getLoaiFromDB();
        for (int i = 0 ; i<loaiAdapter.getCount();i++){
            String ten = loaiAdapter.getItem(i).getTenloai();
            monAnAdapter = null;
            monAnAdapter = new ArrayAdapter<MonAn>(HomeActivity.this,android.R.layout.simple_list_item_1);
            getMonAnFromDB(loaiAdapter.getItem(i).getMaloai());
            String hinh = monAnAdapter.getItem(0).getAnhminhhoa();
            LoaiSearch loaiHome = new LoaiSearch(ten,hinh);
            loaiAdapterHome.add(loaiHome);
        }

        //Tab2
        lvMonAnHome = (ListView) findViewById(R.id.lvMonAnHome);
        monAnAdapterH = new MonAnAdapterHome(HomeActivity.this,R.layout.item_mon);
        lvMonAnHome.setAdapter(monAnAdapterH);
        getMonAnFromDB1();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuChiTiet2:
                thongTinPhanMem();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void thongTinPhanMem() {
        Dialog dialogThongTinPhanMem = new Dialog(HomeActivity.this);
        dialogThongTinPhanMem.setContentView(R.layout.dialog_thongtinphanmem);

        Button btnLienHe = dialogThongTinPhanMem.findViewById(R.id.btnLienHe);

        btnLienHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(HomeActivity.this, SendMailActivity.class);
                startActivity(mailIntent);
            }
        });
        dialogThongTinPhanMem.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_option_menu, menu);

        MenuItem mnuSearch = menu.findItem(R.id.mnuSearch);
        SearchView searchView = (SearchView) mnuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                monAnAdapterH.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void getMonAnFromDB1() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM MONAN", null);
        monAnAdapterH.clear();
        while (cursor.moveToNext()) {
            String mamon = cursor.getString(0);
            String maloai = cursor.getString(1);
            String mact = cursor.getString(2);
            String tenmon = cursor.getString(3);
            String mota = cursor.getString(4);
            String hinhanh = cursor.getString(5);
            MonAn monan = new MonAn(mamon, maloai, mact, tenmon, mota, hinhanh);
            monAnAdapterH.add(monan);
        }
        cursor.close();
    }
    private void getMonAnFromDB(String ma) {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.query("MONAN", null,"MALOAI=?",new String[]{ma},null,null,null);
        monAnAdapter.clear();
        while (cursor.moveToNext()) {
            String mamon = cursor.getString(0);
            String maloai = cursor.getString(1);
            String mact = cursor.getString(2);
            String tenmon = cursor.getString(3);
            String mota = cursor.getString(4);
            String hinhanh = cursor.getString(5);

            MonAn monan = new MonAn(mamon, maloai, mact, tenmon, mota, hinhanh);
            monAnAdapter.add(monan);
        }
        cursor.close();
    }

    private void getLoaiFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM LOAIMONAN", null);
        loaiAdapter.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            Loai nl = new Loai(ma, ten);
            loaiAdapter.add(nl);
        }
        cursor.close();
    }

    public void onShowUaThich() {
        String phonenumber = lastUser.getPhone();
        Intent intent = new Intent(HomeActivity.this, DanhSachUaThich.class);
        intent.putExtra("phonenumber", phonenumber);
        startActivity(intent);
    }

}