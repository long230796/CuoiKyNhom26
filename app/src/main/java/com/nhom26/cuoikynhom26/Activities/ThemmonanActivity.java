package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

import com.google.gson.Gson;
import com.nhom26.cuoikynhom26.Activities.account.UserControl;
import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.adapter.LoaiSpinnerAdapter;
import com.nhom26.cuoikynhom26.adapter.NguyenLieuAdapter;
import com.nhom26.cuoikynhom26.adapter.NguyenLieuSpinnerAdapter;
import com.nhom26.cuoikynhom26.model.Loai;
import com.nhom26.cuoikynhom26.model.NguyenLieu;
import com.nhom26.cuoikynhom26.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class ThemmonanActivity extends AppCompatActivity {

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
    int selectedItemNguyenLieu;

    String imageString;

    ArrayList<Loai> loaiList = new ArrayList<>();
    ArrayList<NguyenLieu> nlList = new ArrayList<>();

    LinearLayout layoutThemNL;

//    UserControl usrctl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themmonan);
        createNotificationChannel();
        addControls();
        getLoaiFromDB();
        addEvents();
//        //User user= usrctl.getSavedObjectFromPreference(getApplicationContext(), "lastUser", "user", User.class);
//        User user = usrctl.getLastUser(getApplicationContext());
//        if (user!= null){
//            Toast.makeText(this, user.getPhone(), Toast.LENGTH_SHORT).show();
//        }
//        else Toast.makeText(this, "No user", Toast.LENGTH_SHORT).show();
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
                   themMonAnVaoDB();
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
                selectedLoai = (Loai) adapterView.getItemAtPosition(0);
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("My notification", "My notification", importance);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void notification(String title, String content) {
        // event notificate clicked
        Intent intent = new Intent(this, NguyenLieuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ThemmonanActivity.this)
                .setSmallIcon(R.drawable.ic_rice_bowl)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ThemmonanActivity.this);
        managerCompat.notify(1, builder.build());
    }

    private void themMonAnVaoDB() {

        Random r = new Random();
        int randomInt1 = r.nextInt(10000) + 1;
        int randomInt2 = r.nextInt(10000) + 1;
        // them mon an
        ContentValues values1 = new ContentValues();
        values1.put("MAMON", "m" + randomInt1);
        values1.put("MALOAI", selectedLoai.getMaloai());
        values1.put("MACT", "ct" + randomInt2);
        values1.put("TENMON", edtTenMonAn.getText().toString());
        values1.put("MOTA", edtMoTa.getText().toString());
        values1.put("ANHMINHHOA", imageString);
        values1.put("LINK", edtLink.getText().toString());


        int kq1 = (int) AdminHomeActivity.database.insert("MONAN",null , values1);
        if (kq1 > 0) {
            // them cong thuc
            ContentValues values2 = new ContentValues();
            values2.put("MAMON", "m" + randomInt1);
            values2.put("MACT", "ct" + randomInt2);
            values2.put("CACHLAM", edtBuocLam.getText().toString());

            int kq2 = (int) AdminHomeActivity.database.insert("CONGTHUC",null , values2);
            if (kq2 > 0) {
                // them ctcongthuc
                boolean isSuccess = true;
                ContentValues values3 = new ContentValues();
                values3.put("MACT", "ct" + randomInt2);
                for (int i = 0; i < nguyenLieuAdapter.getCount(); i ++) {
                    values3.put("MANL", nguyenLieuAdapter.getItem(i).getManl());
                    values3.put("DINHLUONG", nguyenLieuAdapter.getItem(i).getDinhluong());
                    int kq3 = (int) AdminHomeActivity.database.insert("CTCONGTHUC",null , values3);
                    if (kq3 == 0 || kq3 < 0) {
                        Toast.makeText(this, "kh??ng th??? th??m nguy??n li???u", Toast.LENGTH_SHORT).show();
                        isSuccess = false;
                        break;
                    }

                }
                if (isSuccess) {
                    notification("B???n c?? m???t m??n ??n m???i", edtTenMonAn.getText().toString());
                    Toast.makeText(this, "Th??m m??n th??nh c??ng", Toast.LENGTH_SHORT).show();
                    setResult(115);
                    finish();
                } else {
                    Toast.makeText(this, "C?? l???i x???y ra, Th??m m??n th???t b???i.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Kh??ng th??? th??m c??ng th???c", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ThemmonanActivity.this, "C?? l???i x???y ra, vui l??ng th??? l???i", Toast.LENGTH_LONG).show();
        }
    }

    private boolean kiemTraDuLieu() {
        String tenmon = edtTenMonAn.getText().toString();
        Loai loai = selectedLoai;
        String mota = edtMoTa.getText().toString();
        String link = edtLink.getText().toString();
        String anh = txtAnh.getText().toString();
        int nlSize = nguyenLieuAdapter.getCount();
        String buoclam = edtBuocLam.getText().toString();
        if (!tenmon.matches("") && loai != null && !mota.matches("") && !anh.matches("") && nlSize != 0 && !buoclam.matches("") && !link.matches("")) {
            return true;
        } else {
            Toast.makeText(this, "Vui l??ng nh???p ????? th??ng tin", Toast.LENGTH_SHORT).show();
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
                int fileSize = getFileSizeFromUri(ThemmonanActivity.this, selectedImage);
                if (fileSize < 250) {
                    Bitmap hinh = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageString = BitMapToString(hinh);
                    String fileName = getFileName(selectedImage);
                    txtAnh.setText(fileName);

                } else {
                    Toast.makeText(this, "File ph???i c?? k??ch th?????c nh??? h??n 250KB, file hi???n t???i: " +String.valueOf(fileSize) + "KB", Toast.LENGTH_SHORT).show();
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



    private void checkFileSize(Uri selectedImage) {
        String path = selectedImage.getPath();
        File file  = new File(path);
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        Toast.makeText(this, String.valueOf(path), Toast.LENGTH_SHORT).show();
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
            final Dialog dialogThemNL = new Dialog(ThemmonanActivity.this);
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
                        Toast.makeText(ThemmonanActivity.this, "Vui L??ng nh???p ????? th??ng tin", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "???? h???t nguy??n li???u", Toast.LENGTH_SHORT).show();
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
        edtTenMonAn = (EditText) findViewById(R.id.edtTenMonAn);
        spnLoai = (Spinner) findViewById(R.id.spnLoai);
        edtMoTa = (EditText) findViewById(R.id.edtMoTa);
        edtLink = (EditText) findViewById(R.id.edtLinkYoutube);
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

        registerForContextMenu(lvNguyenLieu);

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
        nlList.add(selectedNguyenLieu);
        nguyenLieuAdapter.remove(selectedNguyenLieu);
        selectedNguyenLieu = null;
        Toast.makeText(this, "X??a nguy??n li???u", Toast.LENGTH_SHORT).show();
    }

    private void hienThiDialogEditNL() {
        final Dialog dialogSuaNL = new Dialog(ThemmonanActivity.this);
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
                Toast.makeText(ThemmonanActivity.this, "c???p nh???t ?????nh l?????ng", Toast.LENGTH_SHORT).show();
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
