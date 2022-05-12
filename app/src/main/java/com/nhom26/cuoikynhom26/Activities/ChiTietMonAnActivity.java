package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.adapter.NguyenLieuAdapter;
import com.nhom26.cuoikynhom26.model.MonAn;
import com.nhom26.cuoikynhom26.model.NguyenLieu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ChiTietMonAnActivity extends AppCompatActivity {
    ListView lvDSNguyenLieu;
    ImageView imgMotamonan;
    ArrayAdapter<NguyenLieu> NguyenLieuAdapter;
    MonAn selectedMonAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_an);
        addControls();
        getChiTietMonAnFromDB();
//        addEvents();
//        registerForContextMenu(lvMonAn);


    }

    private void addControls() {
        Intent intent = getIntent();
        selectedMonAn = (MonAn) intent.getSerializableExtra("selectedMonAn");
//        lvDSNguyenLieu = (ListView) findViewById(R.id.txt_dsnguyenlieu);
        imgMotamonan = (ImageView) findViewById(R.id.img_anhminhhoamonan);
//        NguyenLieuAdapter = new ArrayAdapter<NguyenLieu>(ChiTietMonAnActivity.this, android.R.layout.simple_list_item_1);
//        lvDSNguyenLieu.setAdapter(NguyenLieuAdapter);
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void getChiTietMonAnFromDB() {
        String nguyenlieu = "";
        ArrayList<String> listNL = new ArrayList<String>();
        ArrayList<String> listDinhLuong = new ArrayList<String>();
        ArrayList<String> dsnl = new ArrayList<String>();
//        ArrayList<String> listTenNL = new ArrayList<String>();
//        ArrayList<String> listDonVi = new ArrayList<String>();
        listDinhLuong = getDinhLuong(selectedMonAn.getMact());
        listNL = getMaNL(selectedMonAn.getMact());
        for (int i = 0; i < listDinhLuong.size(); i++) {

//            getTenNL(listNL.get(i));
//            getDinhLuong(selectedMonAn.getMact());
//            getDonVi(listNL.get(i));
//            dsnl.concat();
            nguyenlieu += getTenNL(listNL.get(i))+" "+(String)listDinhLuong.get(i)+" "+getDonVi(listNL.get(i))+" "+"\n";
        }

        TextView txtTenMonAn = (TextView) findViewById(R.id.txt_tenmonan);
        TextView txtMotaMonAn = (TextView) findViewById(R.id.txt_motamonan);
        ImageView imgMotamonan = (ImageView) findViewById(R.id.img_anhminhhoamonan);
        TextView txt_motacachlam = (TextView) findViewById(R.id.txt_motabuoclam);
        TextView txt_dsnguyenlieu = (TextView) findViewById(R.id.txt_dsnguyenlieu);
        //get Image
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        imgMotamonan.setImageBitmap(StringToBitMap(getImage(selectedMonAn.getMact())));
        txtTenMonAn.setText(selectedMonAn.getTenmon());
        txtMotaMonAn.setText(selectedMonAn.getMota());
        txt_motacachlam.setText(getMoTaCachLam(selectedMonAn.getMact()));
        txt_dsnguyenlieu.setText(nguyenlieu);

//        Cursor cursor3 = AdminHomeActivity.database.rawQuery("SELECT MANL, DINHLUONG FROM CTCONGTHUC WHERE MACT = '" + selectedMonAn.getMact() + "'", null);
//        while (cursor3.moveToNext()) {
//
//            listNL.add(cursor3.getString(0));
//            listDinhLuong.add(cursor3.getString(1));
//        }
//        cursor3.close();



    }

    private  String getImage(String MaCT) {
        String StringImg = "";
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT ANHMINHHOA FROM MONAN WHERE MAMON = '" + selectedMonAn.getMamon() + "'", null);
        while (cursor.moveToNext()) {
            StringImg = cursor.getString(0);
        }
        cursor.close();
        return StringImg;
    }

    private  String getMoTaCachLam(String MaCT) {
        String motacachlam = "";
        Cursor cursor2 = AdminHomeActivity.database.rawQuery("SELECT CACHLAM FROM CONGTHUC WHERE MACT = '" + selectedMonAn.getMact() + "'", null);
        while (cursor2.moveToNext()) {
            motacachlam = cursor2.getString(0);
        }
        cursor2.close();
        return motacachlam;
    }

    private static ArrayList getDinhLuong(String MaCT) {
        ArrayList<String> listDinhLuong = new ArrayList<String>();
        Cursor cursor4 = AdminHomeActivity.database.rawQuery("SELECT DINHLUONG FROM CTCONGTHUC WHERE MACT = '"+ MaCT+"'", null);
        while (cursor4.moveToNext()) {
            listDinhLuong.add(cursor4.getString(0));
        }
        cursor4.close();
        return listDinhLuong;
    }

    private String getTenNL(String MaNL) {
        String tenNL = "";
        Cursor cursor4 = AdminHomeActivity.database.rawQuery("SELECT TENNL FROM NGUYENLIEU WHERE MANL = '"+ MaNL+"'", null);
        while (cursor4.moveToNext()) {
            tenNL = cursor4.getString(0);
        }
        cursor4.close();
        return tenNL;
    }

    private static ArrayList getMaNL(String MaCT) {
        ArrayList<String> listMaNL = new ArrayList<String>();
        Cursor cursor4 = AdminHomeActivity.database.rawQuery("SELECT MANL FROM CTCONGTHUC WHERE MACT = '"+ MaCT+"'", null);
        while (cursor4.moveToNext()) {
            listMaNL.add(cursor4.getString(0));
        }
        cursor4.close();
        return listMaNL;
    }

    private String getDonVi(String MaNL) {
        String DonVi = "";
        Cursor cursor4 = AdminHomeActivity.database.rawQuery("SELECT DONVI FROM NGUYENLIEU WHERE MANL = '"+ MaNL+"'", null);
        while (cursor4.moveToNext()) {
            DonVi = cursor4.getString(0);
        }
        cursor4.close();
        return DonVi;
    }

    public void AddToUaThich(View view) {
        ThemUaThich(selectedMonAn.getMamon(),"0123456789");
        String a = "";
        String b = "";
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT MAMON, PHONE FROM CTUATHICH WHERE MAMON = '" + selectedMonAn.getMamon() + "'", null);
        while (cursor.moveToNext()) {
            a = cursor.getString(0);
            b = cursor.getString(1);
        }
        cursor.close();
        System.out.println(a);
        System.out.println(b);
        //Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }

    private void ThemUaThich(String mamon, String phone) {
        ContentValues values = new ContentValues();
        values.put("MAMON", mamon);
        values.put("PHONE", phone);
        long kq = AdminHomeActivity.database.insert("CTUATHICH", null, values);
        if (kq > 0) {
            Toast.makeText(this, "Thêm loại thành công", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Món ăn đã có sẵn trong danh mục ưa thích!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
