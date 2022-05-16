package com.nhom26.cuoikynhom26.Activities;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

        }
        return super.onContextItemSelected(item);
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
