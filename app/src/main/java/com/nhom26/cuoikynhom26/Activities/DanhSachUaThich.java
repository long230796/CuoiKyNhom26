package com.nhom26.cuoikynhom26.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.adapter.MonAnAdapter;
import com.nhom26.cuoikynhom26.model.MonAn;

import java.util.ArrayList;

public class DanhSachUaThich extends AppCompatActivity {
    ListView lvDSMonAn;
    ArrayList<MonAn> arrrayListMonAn;
    MonAnAdapter monAnAdapter;
    MonAn selectedMonAn;
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_ua_thich);
        addControls();

        addEvents();
        //getMaMon(phonenumber);
        getDSMonUaThichFromDB();
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private ArrayList getMaMon(String phone) {
        ArrayList<String> ALmaMon = new ArrayList<>();
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT MAMON FROM CTUATHICH WHERE PHONE = '" + phonenumber + "'", null);
        while (cursor.moveToNext()) {
            ALmaMon.add((cursor.getString(0)));
        }
        cursor.close();
        return ALmaMon;
    }

    private void getDS(String mamon) {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM MONAN WHERE MAMON = '" + mamon + "'", null);
        while (cursor.moveToNext()) {
//            System.out.println(cursor.getString(1));
//            System.out.println(cursor.getString(2));
//            System.out.println(cursor.getString(3));
//            System.out.println(cursor.getString(4));
          //  System.out.println(cursor.getString(5));
            MonAn monan = new MonAn();
            monan.setMamon(mamon);
            monan.setMaloai(cursor.getString(1));
            monan.setMact(cursor.getString(2));
            monan.setTenmon(cursor.getString(3));
            monan.setMota(cursor.getString(4));
            monan.setAnhminhhoa(cursor.getString(5));
            arrrayListMonAn.add(monan);
        }
        cursor.close();
        System.out.println(arrrayListMonAn.size());
       monAnAdapter.notifyDataSetChanged();
    }

    private void getDSMonUaThichFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        ArrayList<String> DSMaMon = getMaMon(phonenumber);
        for(int i = 0;i<DSMaMon.size();i++){
//            System.out.println(DSMaMon.get(i));
//            Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT ANHMINHHOA, TENMON, MOTA FROM MONAN WHERE PHONE = '" + DSMaMon.get(i) + "'", null);
//            while (cursor.moveToNext()) {
////                arrrayListMonAn.add(cursor.getString(0),cursor.getString(1),cursor.getString(2));
//                System.out.print(cursor.getString(0));
//                System.out.print(cursor.getString(1));
//                System.out.print(cursor.getString(2));
//            }
//            cursor.close();
            getDS(DSMaMon.get(i));
        }

    }

    private void addControls() {
        lvDSMonAn = (ListView) findViewById(R.id.lv_dsut);
        arrrayListMonAn = new ArrayList<>();
        monAnAdapter = new MonAnAdapter(getApplicationContext(),R.layout.item_monan,arrrayListMonAn);
        lvDSMonAn.setAdapter(monAnAdapter);
        Intent intent = getIntent();
        phonenumber = (String) intent.getSerializableExtra("phonenumber");
    }


    private void addEvents(){
        lvDSMonAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonAn = monAnAdapter.getItem(i);
                selectedMonAn.setAnhminhhoa("");
                Intent intent = new Intent(DanhSachUaThich.this, ChiTietMonAnActivity.class);
                intent.putExtra("selectedMonAn", selectedMonAn);
                startActivity(intent);
            }
        });
    }
}
