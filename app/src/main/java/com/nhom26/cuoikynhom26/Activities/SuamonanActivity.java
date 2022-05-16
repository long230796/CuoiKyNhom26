package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.Activities.account.UserControl;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.adapter.LoaiSpinnerAdapter;
import com.nhom26.cuoikynhom26.adapter.NguyenLieuAdapter;
import com.nhom26.cuoikynhom26.adapter.NguyenLieuSpinnerAdapter;
import com.nhom26.cuoikynhom26.model.Loai;
import com.nhom26.cuoikynhom26.model.MonAn;
import com.nhom26.cuoikynhom26.model.NguyenLieu;
import com.nhom26.cuoikynhom26.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SuamonanActivity extends AppCompatActivity {

    EditText edtTenMonAn;
    EditText edtBuocLam;
    EditText edtMoTa;
    EditText edtLink;
    TextView txtAnh;
    ListView lvNguyenLieu;
    Spinner spnLoai;
    Button btnThem;
    Button btnHuy;
    Button btnSelectImage;

    NguyenLieuAdapter nguyenLieuAdapter;
    LoaiSpinnerAdapter loaiSpinnerAdapter;
    NguyenLieuSpinnerAdapter nlSpinnerAdapter;

    NguyenLieu selectedNguyenLieu;
    Loai selectedLoai;
    MonAn monan;
    int selectedItemNguyenLieu;

    String imageString = null;

    ArrayList<Loai> loaiList = new ArrayList<>();
    ArrayList<NguyenLieu> nlList = new ArrayList<>();

    LinearLayout layoutThemNL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suamonan);
        addControls();
        getLoaiFromDB();
        addEvents();


    }

    private void addEvents() {
        layoutThemNL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiDialogThemNL();
            }
        });

        lvNguyenLieu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItemNguyenLieu = i;
                selectedNguyenLieu = nguyenLieuAdapter.getItem(i);
                return false;
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kiemTraDuLieu()) {
                    suaMonAn();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        spnLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLoai = (Loai) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void suaMonAn() {

        // them mon an

        ContentValues values1 = new ContentValues();
        values1.put("TENMON", edtTenMonAn.getText().toString());
        values1.put("MOTA", edtMoTa.getText().toString());
        values1.put("LINK", edtLink.getText().toString());
        values1.put("MALOAI", selectedLoai.getMaloai());

        if (imageString != null) {
            values1.put("ANHMINHHOA", imageString);
        }

        int kq1 = (int) AdminHomeActivity.database.update("MONAN", values1, "MAMON=?", new String[]{monan.getMamon()});
        if (kq1 > 0) {
            // them cong thuc
            ContentValues values2 = new ContentValues();
            values2.put("CACHLAM", edtBuocLam.getText().toString());

            int kq2 = (int) AdminHomeActivity.database.update("CONGTHUC", values2, "MACT=?", new String[]{monan.getMact()});
            if (kq2 > 0) {
                // xoa ctcongthuc
                int kq3 = AdminHomeActivity.database.delete("CTCONGTHUC", "MACT=?", new String[]{monan.getMact()});
                if (kq3 > 0) {
                    // them ctcongthuc
                    boolean isSuccess = true;
                    ContentValues values3 = new ContentValues();
                    values3.put("MACT", monan.getMact());
                    for (int i = 0; i < nguyenLieuAdapter.getCount(); i ++) {
                        values3.put("MANL", nguyenLieuAdapter.getItem(i).getManl());
                        values3.put("DINHLUONG", nguyenLieuAdapter.getItem(i).getDinhluong());
                        int kq4 = (int) AdminHomeActivity.database.insert("CTCONGTHUC",null , values3);
                        if (kq4 == 0 || kq4 < 0) {
                            Toast.makeText(this, "không thể chỉnh sửa nguyên liệu", Toast.LENGTH_SHORT).show();
                            isSuccess = false;
                            break;
                        }

                    }
                    if (isSuccess) {
                        Toast.makeText(this, "Chỉnh sửa món thành công", Toast.LENGTH_SHORT).show();
                        setResult(115);
                        finish();
                    } else {
                        Toast.makeText(this, "Có lỗi xảy ra, Thêm món thất bại.", Toast.LENGTH_SHORT).show();
                    }

                }

            } else {
                Toast.makeText(this, "Không thể cập nhật công thức", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SuamonanActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private boolean kiemTraDuLieu() {
        String tenmon = edtTenMonAn.getText().toString();
        Loai loai = selectedLoai;
        String mota = edtMoTa.getText().toString();
//        String anh = txtAnh.getText().toString();
        int nlSize = nguyenLieuAdapter.getCount();
        String buoclam = edtBuocLam.getText().toString();
        String link = edtLink.getText().toString();
        if (!tenmon.matches("") && loai != null && !mota.matches("") && nlSize != 0 && !buoclam.matches("") && !link.matches("")) {
            return true;
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Chon Hinh"), 113);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 113 && resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                int fileSize = getFileSizeFromUri(SuamonanActivity.this, selectedImage);
                if (fileSize < 250) {
                    Bitmap hinh = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageString = BitMapToString(hinh);
                    String fileName = getFileName(selectedImage);
                    txtAnh.setText(fileName);

                } else {
                    Toast.makeText(this, "File phải có kích thước nhỏ hơn 250KB, file hiện tại: " +String.valueOf(fileSize) + "KB", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                Log.e("loi: ", ex.toString());
            }
        }
    }

    private static int getFileSizeFromUri(final Context context, final Uri uri) throws IOException {
        long fileSize = 0L;

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream != null) {
            byte[] bytes = new byte[1024];
            int read = -1;
            while ((read = inputStream.read(bytes)) >= 0) {
                fileSize += read;
            }
        }
        inputStream.close();
        return (int) (fileSize*0.001);
    }



    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    private void hienThiDialogThemNL() {

        if (nguyenLieuAdapter.getCount() != 0) {
            for (int i = 0; i < nguyenLieuAdapter.getCount(); i ++) {
                nlList.remove(nguyenLieuAdapter.getItem(i));
            }
        }

        if (nlList.size() != 0) {
            final Dialog dialogThemNL = new Dialog(SuamonanActivity.this);
            dialogThemNL.setContentView(R.layout.dialog_nguyenlieu_add_v2);
            final Spinner spnNL = dialogThemNL.findViewById(R.id.spnNL);
            final EditText edtDinhLuong = dialogThemNL.findViewById(R.id.edtDinhLuong);
            Button btnThem = dialogThemNL.findViewById(R.id.btnThem);
            Button btnHuy = dialogThemNL.findViewById(R.id.btnHuy);

            nlSpinnerAdapter = new NguyenLieuSpinnerAdapter(this, nlList);
            spnNL.setAdapter(nlSpinnerAdapter);

            spnNL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedNguyenLieu = (NguyenLieu) adapterView.getItemAtPosition(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedNguyenLieu = (NguyenLieu) adapterView.getItemAtPosition(0);
                }
            });

            btnThem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!edtDinhLuong.getText().toString().matches("") && selectedNguyenLieu != null) {
                        selectedNguyenLieu.setDinhluong(edtDinhLuong.getText().toString());
                        nguyenLieuAdapter.add(selectedNguyenLieu);
                        selectedNguyenLieu = null;
                        dialogThemNL.dismiss();
                    } else {
                        Toast.makeText(SuamonanActivity.this, "Vui Lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogThemNL.dismiss();
                }
            });

            dialogThemNL.show();

        } else {
            Toast.makeText(this, "Đã hết nguyên liệu", Toast.LENGTH_SHORT).show();
        }




    }

    private void getLoaiFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM LOAIMONAN", null);
        loaiList.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            Loai loai = new Loai(ma, ten);
            loaiList.add(loai);
        }
        cursor.close();
    }

    private void getNguyenLieuFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM NGUYENLIEU", null);
        nlList.clear();
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String donvi = cursor.getString(2);
            NguyenLieu nl = new NguyenLieu(ma, ten, donvi);
            nlList.add(nl);
        }
        cursor.close();
    }

    private void addControls() {
        Intent intent = getIntent();
        monan = (MonAn) intent.getSerializableExtra("monan");

        edtTenMonAn = (EditText) findViewById(R.id.edtTenMonAn);
        edtLink = (EditText) findViewById(R.id.edtLinkYoutube);
        spnLoai = (Spinner) findViewById(R.id.spnLoai);
        edtMoTa = (EditText) findViewById(R.id.edtMoTa);
        txtAnh = (TextView) findViewById(R.id.txtAnh);
        lvNguyenLieu = (ListView) findViewById(R.id.lvNguyenLieu);
        edtBuocLam = (EditText) findViewById(R.id.edtBuocLam);
        btnThem = (Button) findViewById(R.id.btnThem);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        spnLoai = (Spinner) findViewById(R.id.spnLoai);
        layoutThemNL = (LinearLayout) findViewById(R.id.layoutThemNL);

        getLoaiFromDB();
        getNguyenLieuFromDB();
        loaiSpinnerAdapter = new LoaiSpinnerAdapter(this, loaiList);
        spnLoai.setAdapter(loaiSpinnerAdapter);

        nguyenLieuAdapter = new NguyenLieuAdapter(this, R.layout.nguyenlieu_custom_listview);
        lvNguyenLieu.setAdapter(nguyenLieuAdapter);

        initValueMonAn();
        registerForContextMenu(lvNguyenLieu);

    }

    private void initValueMonAn() {
        edtTenMonAn.setText(monan.getTenmon());
        edtMoTa.setText(monan.getMota());
        edtLink.setText(monan.getLink());
        spnLoai.setSelection(getPositionMonAn());
        getCongThucCTCongThucByMaCT(monan.getMact());

    }

    private void getCongThucCTCongThucByMaCT(String maCT) {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM CONGTHUC WHERE MACT = ?", new String[]{maCT});

        while (cursor.moveToNext()) {
            edtBuocLam.setText(cursor.getString(2));
        }
        cursor.close();

        Cursor cursor1 = AdminHomeActivity.database.rawQuery("SELECT * FROM CTCONGTHUC WHERE MACT = ?", new String[]{maCT});
        nguyenLieuAdapter.clear();
        while (cursor1.moveToNext()) {
            for (int i = 0; i < nlList.size(); i ++) {
                if (nlList.get(i).getManl().equals(cursor1.getString(1))) {
                    selectedNguyenLieu = nlList.get(i);
                    selectedNguyenLieu.setDinhluong(cursor1.getString(2));
                    nguyenLieuAdapter.add(selectedNguyenLieu);
                }
            }
        }
        cursor1.close();
        selectedNguyenLieu = null;

    }

    public int getPositionMonAn() {
        for (int i = 0; i < loaiList.size(); i ++) {
            if (monan.getMaloai().equals(loaiList.get(i).getMaloai())) {
                return i;
            }
        }
        return 0;
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
                hienThiDialogEditNL();
                break;

            case R.id.mnuXoa:
                hienThiDialogXoaNL();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void hienThiDialogXoaNL() {
        nlList.add(selectedNguyenLieu);
        nguyenLieuAdapter.remove(selectedNguyenLieu);
        selectedNguyenLieu = null;
        Toast.makeText(this, "Xóa nguyên liệu", Toast.LENGTH_SHORT).show();
    }

    private void hienThiDialogEditNL() {
        final Dialog dialogSuaNL = new Dialog(SuamonanActivity.this);
        dialogSuaNL.setContentView(R.layout.dialog_nguyenlieu_edit_v2);

        TextView txtMaNL = dialogSuaNL.findViewById(R.id.txtMaNL);
        TextView txtTenNL = dialogSuaNL.findViewById(R.id.txtTenNL);
        TextView txtDonVi = dialogSuaNL.findViewById(R.id.txtDonVi);
        final EditText edtDinhLuong = dialogSuaNL.findViewById(R.id.edtDinhLuong);
        Button btnLuu = dialogSuaNL.findViewById(R.id.btnLuu);
        Button btnHuy = dialogSuaNL.findViewById(R.id.btnHuy);

        txtMaNL.setText(selectedNguyenLieu.getManl());
        txtTenNL.setText(selectedNguyenLieu.getTennl());
        txtDonVi.setText(selectedNguyenLieu.getDonvi());
        edtDinhLuong.setText(selectedNguyenLieu.getDinhluong());

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nguyenLieuAdapter.getItem(selectedItemNguyenLieu).setDinhluong(edtDinhLuong.getText().toString());
                nguyenLieuAdapter.notifyDataSetChanged();
                Toast.makeText(SuamonanActivity.this, "cập nhật định lượng", Toast.LENGTH_SHORT).show();
                selectedNguyenLieu = null;
                dialogSuaNL.dismiss();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSuaNL.dismiss();
                selectedNguyenLieu = null;
            }
        });

        dialogSuaNL.show();
    }
}
