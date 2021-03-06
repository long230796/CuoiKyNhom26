package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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

import com.nhom26.cuoikynhom26.Activities.account.LoginActivity;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.NguyenLieu;

import java.util.Random;

public class NguyenLieuActivity extends AppCompatActivity {

    ListView lvNguyenLieu;
    ImageView imgThemNL;
    ArrayAdapter<NguyenLieu> nguyenLieuAdapter;
    NguyenLieu selectedNguyenLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguyen_lieu);
        //hienThiManHinhLogin();
        addControls();
        getNguyenLieuFromDB();
        addEvents();
        registerForContextMenu(lvNguyenLieu);
    }

    private void hienThiManHinhLogin() {
        Intent intent = new Intent(NguyenLieuActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void addEvents() {
        imgThemNL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiDialogThemNL();
            }
        });

        lvNguyenLieu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNguyenLieu = nguyenLieuAdapter.getItem(i);
                hienThiDialogChiTietNL();
            }
        });

        lvNguyenLieu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNguyenLieu = nguyenLieuAdapter.getItem(i);
                return false;
            }
        });



    }

    private void hienThiDialogChiTietNL() {
        Dialog dialogChiTietNL = new Dialog(NguyenLieuActivity.this);
        dialogChiTietNL.setContentView(R.layout.dialog_nguyenlieu_detail);

        TextView txtMaNL = dialogChiTietNL.findViewById(R.id.txtMaNL);
        TextView txtTenNL = dialogChiTietNL.findViewById(R.id.txtTenNL);
        TextView txtDonVi = dialogChiTietNL.findViewById(R.id.txtDonVi);

        txtMaNL.setText(selectedNguyenLieu.getManl());
        txtTenNL.setText(selectedNguyenLieu.getTennl());
        txtDonVi.setText(selectedNguyenLieu.getDonvi());

        dialogChiTietNL.show();


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
                Toast.makeText(this, "Th??m Nguy??n li???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                dialogThemNL.dismiss();
            } else {
                Toast.makeText(this, "C?? l???i x???y ra, Th??m kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui l??ng ??i???n ????? th??ng tin", Toast.LENGTH_SHORT).show();
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

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setIcon(R.drawable.ic_grain_sack);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        lvNguyenLieu = (ListView) findViewById(R.id.lvNguyenLieu);
        imgThemNL = (ImageView) findViewById(R.id.imgThemNL);

        nguyenLieuAdapter = new ArrayAdapter<NguyenLieu>(NguyenLieuActivity.this, android.R.layout.simple_list_item_1);
        lvNguyenLieu.setAdapter(nguyenLieuAdapter);
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
                nguyenLieuAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Ch???n h??nh ?????ng");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
                hienThiDialogEditNL();
                break;

            case R.id.mnuXoa:
                hienThiDialogXoaNL();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void hienThiDialogXoaNL() {

        final Dialog dialogXoa = new Dialog(NguyenLieuActivity.this);
        dialogXoa.setContentView(R.layout.dialog_nguyenlieu_delete);

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
        int kq = AdminHomeActivity.database.delete("NGUYENLIEU", "MANL=?", new String[]{selectedNguyenLieu.getManl()});
        if (kq > 0) {
            Toast.makeText(NguyenLieuActivity.this, "X??a th??nh c??ng", Toast.LENGTH_SHORT).show();
            dialogXoa.dismiss();
            getNguyenLieuFromDB();
        } else {
            Toast.makeText(NguyenLieuActivity.this, "C?? l???i x???y ra, vui l??ng th??? l???i", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienThiDialogEditNL() {
        final Dialog dialogSuaNL = new Dialog(NguyenLieuActivity.this);
        dialogSuaNL.setContentView(R.layout.dialog_nguyenlieu_edit);

        final TextView txtMaNL = dialogSuaNL.findViewById(R.id.txtMaNL);
        final EditText edtTenNL = dialogSuaNL.findViewById(R.id.edtTenNL);
        final EditText edtDonVi = dialogSuaNL.findViewById(R.id.edtDonVi);
        Button btnLuu = dialogSuaNL.findViewById(R.id.btnLuu);
        Button btnHuy = dialogSuaNL.findViewById(R.id.btnHuy);

        txtMaNL.setText(selectedNguyenLieu.getManl());
        edtTenNL.setText(selectedNguyenLieu.getTennl());
        edtDonVi.setText(selectedNguyenLieu.getDonvi());

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manl = txtMaNL.getText().toString();
                String tennl = edtTenNL.getText().toString();
                String donvi = edtDonVi.getText().toString();
                editNguyenLIeu(dialogSuaNL, manl, tennl, donvi);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSuaNL.dismiss();
            }
        });

        dialogSuaNL.show();

    }

    private void editNguyenLIeu(Dialog dialogSuaNL, String manl, String tennl, String donvi) {
        ContentValues values = new ContentValues();
        values.put("TENNL", tennl);
        values.put("DONVI", donvi);

        int kq = AdminHomeActivity.database.update("NGUYENLIEU", values, "MANL=?", new String[]{manl});
        if (kq > 0) {
            Toast.makeText(NguyenLieuActivity.this, "C???p nh???t th??nh c??ng", Toast.LENGTH_LONG).show();
            dialogSuaNL.dismiss();
            getNguyenLieuFromDB();
        } else {
            Toast.makeText(NguyenLieuActivity.this, "C?? l???i x???y ra, vui l??ng th??? l???i", Toast.LENGTH_LONG).show();
        }
    }
}
