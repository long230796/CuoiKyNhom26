package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.Loai;

import java.util.Random;

public class LoaiActivity extends AppCompatActivity {

    ListView lvLoai;
    ImageView imgThemLoai;
    ArrayAdapter<Loai> loaiAdapter;
    Loai selectedLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai);
        addControls();
        getLoaiFromDB();
        addEvents();
        registerForContextMenu(lvLoai);
    }

    private void addEvents() {
        imgThemLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiDialogThemLoai();
            }
        });

        lvLoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = loaiAdapter.getItem(i);
                hienThiDialogChiTietLoai();
            }
        });

        lvLoai.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = loaiAdapter.getItem(i);
                return false;
            }
        });
    }

    private void hienThiDialogChiTietLoai() {
        Dialog dialogChitietLoai = new Dialog(LoaiActivity.this);
        dialogChitietLoai.setContentView(R.layout.dialog_loai_detail);

        TextView txtMaLoai = dialogChitietLoai.findViewById(R.id.txtMaLoai);
        TextView txtTenLoai = dialogChitietLoai.findViewById(R.id.txtTenLoai);

        txtMaLoai.setText(selectedLoai.getMaloai());
        txtTenLoai.setText(selectedLoai.getTenloai());

        dialogChitietLoai.show();
    }

    private void hienThiDialogThemLoai() {
        final Dialog dialogThemLoai = new Dialog(LoaiActivity.this);
        dialogThemLoai.setContentView(R.layout.dialog_loai_add);

        Random r = new Random();
        final int randomInt = r.nextInt(100000) + 1;

        TextView txtMaLoai = dialogThemLoai.findViewById(R.id.txtMaLoai);
        final EditText edtTenLoai = dialogThemLoai.findViewById(R.id.edtTenLoai);
        Button btnThem = dialogThemLoai.findViewById(R.id.btnThem);
        Button btnHuy = dialogThemLoai.findViewById(R.id.btnHuy);

        txtMaLoai.setText("l" + randomInt);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maLoai = "l"+randomInt;
                String tenLoai = edtTenLoai.getText().toString();
                themLoai(maLoai, tenLoai, dialogThemLoai);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogThemLoai.dismiss();
            }
        });

        dialogThemLoai.show();



    }

    private void themLoai(String maloai, String tenloai, Dialog dialogThemLoai) {
        if (!tenloai.matches("")) {
            ContentValues values = new ContentValues();
            values.put("MALOAI", maloai);
            values.put("TENLOAI", tenloai);
            long kq = AdminHomeActivity.database.insert("LOAIMONAN", null, values);
            if (kq > 0) {
                Toast.makeText(this, "Thêm loại thành công", Toast.LENGTH_SHORT).show();
                getLoaiFromDB();
                dialogThemLoai.dismiss();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra, Thêm không thành công", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
        }

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

    private void addControls() {
        lvLoai = (ListView) findViewById(R.id.lvLoai);
        imgThemLoai = (ImageView) findViewById(R.id.imgThemLoai);
        loaiAdapter = new ArrayAdapter<Loai>(LoaiActivity.this, android.R.layout.simple_list_item_1);
        lvLoai.setAdapter(loaiAdapter);
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
                loaiAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Chọn hành động");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
                hienThiDialogEditLoai();
                break;

            case R.id.mnuXoa:
                hienThiDialogXoaLoai();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void hienThiDialogXoaLoai() {

        final Dialog dialogXoa = new Dialog(LoaiActivity.this);
        dialogXoa.setContentView(R.layout.dialog_delete);

        Button btnCo = dialogXoa.findViewById(R.id.btnCo);
        Button btnKhong = dialogXoa.findViewById(R.id.btnKhong);

        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(dialogXoa);
            }
        });

        btnKhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogXoa.dismiss();
            }
        });

        dialogXoa.show();


    }

    private void delete(Dialog dialogXoa) {
        int kq = AdminHomeActivity.database.delete("LOAIMONAN", "MALOAI=?", new String[]{selectedLoai.getMaloai()});
        if (kq > 0) {
            Toast.makeText(LoaiActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            dialogXoa.dismiss();
            getLoaiFromDB();
        } else {
            Toast.makeText(LoaiActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienThiDialogEditLoai() {
        final Dialog dialogSuaLoai = new Dialog(LoaiActivity.this);
        dialogSuaLoai.setContentView(R.layout.dialog_loai_edit);

        final TextView txtMaLoai = dialogSuaLoai.findViewById(R.id.txtMaLoai);
        final EditText edtTenLoai = dialogSuaLoai.findViewById(R.id.edtTenLoai);
        Button btnLuu = dialogSuaLoai.findViewById(R.id.btnLuu);
        Button btnHuy = dialogSuaLoai.findViewById(R.id.btnHuy);

        txtMaLoai.setText(selectedLoai.getMaloai());
        edtTenLoai.setText(selectedLoai.getTenloai());

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maloai = txtMaLoai.getText().toString();
                String tenloai = edtTenLoai.getText().toString();
                editLoai(dialogSuaLoai, maloai, tenloai);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSuaLoai.dismiss();
            }
        });

        dialogSuaLoai.show();

    }

    private void editLoai(Dialog dialogSuaLoai, String maloai, String tenloai) {
        ContentValues values = new ContentValues();
        values.put("TENLOAI", tenloai);

        int kq = AdminHomeActivity.database.update("LOAIMONAN", values, "MALOAI=?", new String[]{maloai});
        if (kq > 0) {
            Toast.makeText(LoaiActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            dialogSuaLoai.dismiss();
            getLoaiFromDB();
        } else {
            Toast.makeText(LoaiActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }
}
