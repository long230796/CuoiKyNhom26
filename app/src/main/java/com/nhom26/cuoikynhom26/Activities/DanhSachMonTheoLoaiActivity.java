package com.nhom26.cuoikynhom26.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.adapter.MonAnAdapterHome;
import com.nhom26.cuoikynhom26.model.Loai;
import com.nhom26.cuoikynhom26.model.MonAn;

public class DanhSachMonTheoLoaiActivity extends AppCompatActivity {

    Loai loai;
    MonAnAdapterHome monAnAdapterH;
    ListView lvMonAnHome;
    TextView txtTenLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_mon_theo_loai);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvMonAnHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MonAn monAn = monAnAdapterH.getItem(i);
                monAn.setAnhminhhoa("");
                Intent intent = new Intent(DanhSachMonTheoLoaiActivity.this, ChiTietMonAnActivity.class);
                intent.putExtra("selectedMonAn",monAn);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        Intent intent =  getIntent();
        loai = (Loai) intent.getSerializableExtra("selectedLoai");

        txtTenLoai = (TextView) findViewById(R.id.txtTenLoaiGoiY);
        txtTenLoai.setText(loai.getTenloai());

        lvMonAnHome = (ListView) findViewById(R.id.lvMonAnGoiY);
        monAnAdapterH = new MonAnAdapterHome(DanhSachMonTheoLoaiActivity.this,R.layout.item_mon);
        lvMonAnHome.setAdapter(monAnAdapterH);
        getMonAnFromDB(loai.getMaloai());
    }

    private void getMonAnFromDB(String ma) {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.query("MONAN", null,"MALOAI=?",new String[]{ma},null,null,null);
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
}