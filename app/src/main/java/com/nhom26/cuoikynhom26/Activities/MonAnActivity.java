package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.MonAn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MonAnActivity extends AppCompatActivity {

    ListView lvMonAn;
    ImageView imgThemMonAn;
    ArrayAdapter<MonAn> monAnAdapter;
    MonAn selectedMonAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);
        createNotificationChannel();
        addControls();
        getMonAnFromDB();
        addEvents();
        registerForContextMenu(lvMonAn);
    }

    private void addEvents() {
        imgThemMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    hienThiManHinhThemFood();
            }
        });

        lvMonAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonAn = monAnAdapter.getItem(i);
                Intent intent = new Intent(MonAnActivity.this, ChiTietMonAnActivity.class);
                intent.putExtra("selectedMonAn", selectedMonAn);
                startActivity(intent);
            }
        });

        lvMonAn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonAn = monAnAdapter.getItem(i);
                return false;
            }
        });
    }

    private void hienThiManHinhThemFood() {
        Intent intent = new Intent(MonAnActivity.this, ThemmonanActivity.class);
        startActivityForResult(intent, 113);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 113 && resultCode == 115) {
            getMonAnFromDB();
        }
    }


    private void getMonAnFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT MAMON, MALOAI, MACT, TENMON, MOTA, LINK FROM MONAN", null);
        monAnAdapter.clear();
        while (cursor.moveToNext()) {
            String mamon = cursor.getString(0);
            String maloai = cursor.getString(1);
            String mact = cursor.getString(2);
            String tenmon = cursor.getString(3);
            String mota = cursor.getString(4);
            String hinhanh = "";
            String link = cursor.getString(5);

            MonAn monan = new MonAn(mamon, maloai, mact, tenmon, mota, hinhanh, link);
            monAnAdapter.add(monan);
        }
        cursor.close();
    }

    private void addControls() {
        lvMonAn = (ListView) findViewById(R.id.lvMonAn);
        imgThemMonAn = (ImageView) findViewById(R.id.imgThemMonAn);
        monAnAdapter = new ArrayAdapter<MonAn>(MonAnActivity.this, android.R.layout.simple_list_item_1);
        lvMonAn.setAdapter(monAnAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuNotification:
                // event notificate clicked
                Intent intent = new Intent(this, NguyenLieuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MonAnActivity.this)
                        .setSmallIcon(R.drawable.ic_rice_bowl)
                        .setContentTitle("My notification")
                        .setContentText("Much longer text that cannot fit one line...")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MonAnActivity.this);
                managerCompat.notify(1, builder.build());
                break;
            case R.id.mnuSendMail:
                Intent mailIntent = new Intent(this, SendMailActivity.class);
                startActivity(mailIntent);
                break;
            case R.id.mnuChart:
//                Intent chartIntent = new Intent(this, CircleChartActivity.class);
//                startActivity(chartIntent);
//                break;

        }
        return super.onOptionsItemSelected(item);
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
                monAnAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Chọn hành động");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.monan_action_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
                hienThiDialogEditLoai();
                break;

            case R.id.mnuXoa:
                hienThiDialogXoaMonAn();
                break;

            case R.id.mnuPdf:
                hienThiDialogConfirmPdf();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void hienThiDialogConfirmPdf() {
        final Dialog dialogConfirmPdf = new Dialog(MonAnActivity.this);
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

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        canvas.drawText(selectedMonAn.getMota(),60 , 620, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("NGUYÊN LIỆU",30 , 650, title);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("CÁCH LÀM",30 , 800, title);

//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("Tầng", 60, 190, title);

//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText(phong.getMa(),160 , 130, title);
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText(phong.getLoai(),160 , 160, title);
//
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText(String.valueOf(phong.getTang()),160 , 190, title);
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("THIẾT BỊ SỬ DỤNG",30 , 240, title);
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("Mã TB",30 , 270, title);
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("Tên TB",130 , 270, title);
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("Xuất xứ",320 , 270, title);
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("Mã loại",430 , 270, title);
//
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("Số lượng",530 , 270, title);
//
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(15);
//        title.setColor(ContextCompat.getColor(this, android.R.color.black));
//        canvas.drawText("Ngày sử dụng",630 , 270, title);

//        int heightItem = 270;
//
//        for (int i = 0; i < chitietsudungAdapter.getCount(); i ++) {
//            heightItem += 30;
//            getThietBiByMa(chitietsudungAdapter.getItem(i).getMatb());
//
//            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            title.setTextSize(15);
//            title.setColor(ContextCompat.getColor(this, android.R.color.black));
//            canvas.drawText(selectedThietBi.getMatb(),30 , heightItem, title);
//
//            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            title.setTextSize(15);
//            title.setColor(ContextCompat.getColor(this, android.R.color.black));
//            canvas.drawText(selectedThietBi.getTentb(),130 , heightItem, title);
//
//            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            title.setTextSize(15);
//            title.setColor(ContextCompat.getColor(this, android.R.color.black));
//            canvas.drawText(selectedThietBi.getXuatxu(),320 , heightItem, title);
//
//            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            title.setTextSize(15);
//            title.setColor(ContextCompat.getColor(this, android.R.color.black));
//            canvas.drawText(selectedThietBi.getMaLoai(),430 , heightItem, title);
//
//            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            title.setTextSize(15);
//            title.setColor(ContextCompat.getColor(this, android.R.color.black));
//            canvas.drawText(chitietsudungAdapter.getItem(i).getSoluong(),530 , heightItem, title);
//
//            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            title.setTextSize(15);
//            title.setColor(ContextCompat.getColor(this, android.R.color.black));
//            canvas.drawText(chitietsudungAdapter.getItem(i).getNgaysudung(),630 , heightItem, title);
//        }

        pdfDocument.finishPage(myPage);
        File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(MonAnActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();


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

    private void hienThiDialogEditLoai() {

        Intent intent = new Intent(MonAnActivity.this, SuamonanActivity.class);
        intent.putExtra("monan", selectedMonAn);

        startActivityForResult(intent, 113);

    }

    private void hienThiDialogXoaMonAn() {
        final Dialog dialogXoa = new Dialog(MonAnActivity.this);
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
        int kq = AdminHomeActivity.database.delete("MONAN", "MAMON=?", new String[]{selectedMonAn.getMamon()});
        if (kq > 0) {
            int kq2 = AdminHomeActivity.database.delete("CTCONGTHUC", "MACT=?", new String[]{selectedMonAn.getMact()});
            if (kq2 > 0) {
                int kq3 = AdminHomeActivity.database.delete("CONGTHUC", "MACT=?", new String[]{selectedMonAn.getMact()});
                if (kq3 > 0) {
                    Toast.makeText(MonAnActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    getMonAnFromDB();
                    dialogXoa.dismiss();
                } else {
                    Toast.makeText(this, "Không thể xóa CONGTHUC", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không thể xóa CTCONGTHUC", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MonAnActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }
}
