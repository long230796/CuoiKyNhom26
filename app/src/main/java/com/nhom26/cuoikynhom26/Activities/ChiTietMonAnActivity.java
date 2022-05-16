package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.adapter.BinhLuanAdapter;
import com.nhom26.cuoikynhom26.model.BinhLuan;
import com.nhom26.cuoikynhom26.model.MonAn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChiTietMonAnActivity extends AppCompatActivity {
    ListView lvComment;
    Button btnGui;
    EditText edtBinhLuan;
    ImageView imgMotamonan;

    BinhLuanAdapter binhLuanAdapter;

    MonAn selectedMonAn;
    BinhLuan selectedBinhLuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_an);
        addControls();
        getChiTietMonAnFromDB();
        getBinhLuanByMaMonFromDB();
        addEvents();
        registerForContextMenu(lvComment);


    }

    private void addEvents() {
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themBinhLuan();
            }
        });

        lvComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBinhLuan = binhLuanAdapter.getItem(i);
                return false;
            }
        });
    }

    private void themBinhLuan() {
        if (!edtBinhLuan.getText().toString().matches("")) {

            String pattern = "hh:mm dd/MM";
            String dateInString =new SimpleDateFormat(pattern).format(new Date());

            ContentValues values = new ContentValues();
            values.put("MAMON", selectedMonAn.getMamon());
            values.put("PHONE", "0329138040");
            values.put("NOIDUNG", edtBinhLuan.getText().toString());
            values.put("THOIGIAN", dateInString);

            int kq = (int) AdminHomeActivity.database.insert("CTBINHLUAN", null , values);
            if (kq > 0) {
                edtBinhLuan.setText("");
                getBinhLuanByMaMonFromDB();
                Toast.makeText(this, "Đã gửi", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "vui lòng đợi 60s để gửi tiếp", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBinhLuanByMaMonFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM CTBINHLUAN WHERE MAMON = ?", new String[]{selectedMonAn.getMamon()});
        binhLuanAdapter.clear();
        while (cursor.moveToNext()) {
            String mamon = cursor.getString(0);
            String phone = cursor.getString(1);
            String noidung = cursor.getString(2);
            String thoigian = cursor.getString(3);
            BinhLuan bl = new BinhLuan(mamon, phone, noidung, thoigian);
            binhLuanAdapter.add(bl);
        }
        cursor.close();
    }

    private void addControls() {

        lvComment = (ListView) findViewById(R.id.lvReviewer);
        btnGui = (Button) findViewById(R.id.btnGui);
        edtBinhLuan = (EditText) findViewById(R.id.edtBinhLuan);


        Intent intent = getIntent();
        selectedMonAn = (MonAn) intent.getSerializableExtra("selectedMonAn");
        imgMotamonan = (ImageView) findViewById(R.id.img_anhminhhoamonan);
        binhLuanAdapter = new BinhLuanAdapter(this, R.layout.item_reviewer);
        lvComment.setAdapter(binhLuanAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuPdf:
                hienThiDialogConfirmPdf();
                break;

            case R.id.mnuVideo:
                Intent intent = new Intent(ChiTietMonAnActivity.this, YoutubeActivity.class);
                intent.putExtra("id", selectedMonAn.getLink());
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Chọn hành động");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.binhluan_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuXoa:
                hienThiDialogXoaBinhLuan();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void hienThiDialogXoaBinhLuan() {
        final Dialog dialogXoa = new Dialog(ChiTietMonAnActivity.this);
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
        int kq = AdminHomeActivity.database.delete("CTBINHLUAN", "THOIGIAN=?", new String[]{selectedBinhLuan.getNgaygio()});
        if (kq > 0) {
            Toast.makeText(this, "Đã xóa bình luận", Toast.LENGTH_SHORT).show();
            dialogXoa.dismiss();
            getBinhLuanByMaMonFromDB();
        } else {
            Toast.makeText(ChiTietMonAnActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienThiDialogConfirmPdf() {
        final Dialog dialogConfirmPdf = new Dialog(ChiTietMonAnActivity.this);
        dialogConfirmPdf.setContentView(R.layout.dialog_confirm_pdf);

        Button btnXuat = dialogConfirmPdf.findViewById(R.id.btnXuat);
        Button btnHuy = dialogConfirmPdf.findViewById(R.id.btnHuy);

        btnXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuatPdf();
                dialogConfirmPdf.dismiss();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirmPdf.dismiss();
            }
        });

        dialogConfirmPdf.show();
    }

    private void xuatPdf() {
        int pageHeight = 1280;
        int pagewidth = 768;
        final int PERMISSION_REQUEST_CODE = 200;

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        getImageByMaMonAn();
        generatePDF(pagewidth, pageHeight);

    }

    private void getImageByMaMonAn() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM MONAN WHERE MAMON = ?", new String[]{selectedMonAn.getMamon()});
        while (cursor.moveToNext()) {
            String anhminhhoa = cursor.getString(5);
            selectedMonAn.setAnhminhhoa(anhminhhoa);
        }

        cursor.close();
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 87);
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void generatePDF(int pagewidth, int pageHeight) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();


        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(24);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("CHI TIẾT MÓN ĂN", 384, 80, title);

        Bitmap scaledbmp = Bitmap.createScaledBitmap(StringToBitMap(selectedMonAn.getAnhminhhoa()), 400, 400, false);
        canvas.drawBitmap(scaledbmp, (pagewidth-400)/2, 100, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("TÊN MÓN ĂN",30 , 530, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(selectedMonAn.getTenmon(),60 , 560, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("MÔ TẢ",30 , 590, title);

        int currentHeight = 610;
        String mota = selectedMonAn.getMota().replace("\n", "").replace("\r", "");;
        String[] array = mota.split("(?<=\\G.{" + 50 + "})");
        while (array.length > 1) {
            currentHeight += 15;
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(array[0], 60 , currentHeight, title);
            array = array[1].split("(?<=\\G.{" + 50 + "})");
        }



        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("NGUYÊN LIỆU",30 , currentHeight += 30, title);

        ArrayList<String> listNL = new ArrayList<String>();
        ArrayList<String> listDinhLuong = new ArrayList<String>();
        listDinhLuong = getDinhLuong(selectedMonAn.getMact());
        listNL = getMaNL(selectedMonAn.getMact());

        for (int i = 0; i < listDinhLuong.size(); i++) {
            currentHeight += 30;
            String tenNl = getTenNL(listNL.get(i));
            String dinhLuong = (String)listDinhLuong.get(i);
            String donVi = getDonVi(listNL.get(i));

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(tenNl, 60 , currentHeight, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(dinhLuong, 170 , currentHeight, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(donVi, 210 , currentHeight, title);
        }

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("CÁCH LÀM",30 , currentHeight += 30, title);

        currentHeight += 20;
        String canhlam = getMoTaCachLam(selectedMonAn.getMact()).replace("\n", "").replace("\r", "");
        String[] array2 = canhlam.split("(?<=\\G.{50})");
        Toast.makeText(this, String.valueOf(array2.length), Toast.LENGTH_SHORT).show();
        while (array2.length > 1) {
            currentHeight += 15;
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(15);
            title.setColor(ContextCompat.getColor(this, android.R.color.black));
            canvas.drawText(array2[0], 60 , currentHeight, title);
            array2 = array2[1].split("(?<=\\G.{" + 50 + "})");
        }


        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ChiTietMonAnActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chitietmonan_option_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    private void getChiTietMonAnFromDB() {
        String nguyenlieu = "";
        ArrayList<String> listNL = new ArrayList<String>();
        ArrayList<String> listDinhLuong = new ArrayList<String>();
        listDinhLuong = getDinhLuong(selectedMonAn.getMact());
        listNL = getMaNL(selectedMonAn.getMact());
        for (int i = 0; i < listDinhLuong.size(); i++) {

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
