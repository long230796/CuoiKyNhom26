package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.NguyenLieu;

import java.util.Random;

public class NguyenLieuActivity extends AppCompatActivity {

    ListView lvNguyenLieu;
    ImageView imgThemNL;
    ArrayAdapter<NguyenLieu> nguyenLieuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguyen_lieu);
        addControls();
        getNguyenLieuFromDB();
        addEvents();
    }

    private void addEvents() {
        imgThemNL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiDialogThemNL();
            }
        });

    }

    private void hienThiDialogThemNL() {
        final Dialog dialogThemNL = new Dialog(NguyenLieuActivity.this);
        dialogThemNL.setContentView(R.layout.dialog_nguyenlieu_add);

        Random r = new Random();
        final int randomInt = r.nextInt(10000) + 1;

        final TextView txtMaNL = dialogThemNL.findViewById(R.id.txtMaNL);
        final EditText edtTenNL = dialogThemNL.findViewById(R.id.edtTenNL);
        final EditText edtDonVi = dialogThemNL.findViewById(R.id.edtDonVi);
        Button btnThem = dialogThemNL.findViewById(R.id.btnThem);
        Button btnHuy = dialogThemNL.findViewById(R.id.btnHuy);

        txtMaNL.setText("nl" + randomInt);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themNguyenLieu(randomInt, edtTenNL.getText().toString(), edtDonVi.getText().toString(), dialogThemNL);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogThemNL.dismiss();
            }
        });

        dialogThemNL.show();
    }

    private void themNguyenLieu(int manl, String tennl, String donvi, Dialog dialogThemNL) {
        if (!tennl.matches("") && !donvi.matches("")) {
            ContentValues values = new ContentValues();
            values.put("MANL","nl" + String.valueOf(manl));
            values.put("TENNL", tennl);
            values.put("DONVI", donvi);
            long kq = AdminHomeActivity.database.insert("NGUYENLIEU", null, values);
            if (kq > 0) {
                getNguyenLieuFromDB();
                Toast.makeText(this, "Thêm Nguyên liệu thành công", Toast.LENGTH_SHORT).show();
                dialogThemNL.dismiss();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra, Thêm không thành công", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
        }

    }

    private void getNguyenLieuFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM NGUYENLIEU", null);
        nguyenLieuAdapter.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String donvi = cursor.getString(2);
            NguyenLieu nl = new NguyenLieu(ma, ten, donvi);
            nguyenLieuAdapter.add(nl);
        }
        cursor.close();
    }

    private void addControls() {
        lvNguyenLieu = (ListView) findViewById(R.id.lvNguyenLieu);
        imgThemNL = (ImageView) findViewById(R.id.imgThemNL);

        nguyenLieuAdapter = new ArrayAdapter<NguyenLieu>(NguyenLieuActivity.this, android.R.layout.simple_list_item_1);
        lvNguyenLieu.setAdapter(nguyenLieuAdapter);
    }
}
