package com.nhom26.cuoikynhom26.Activities;

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
import android.widget.ImageView;
import android.widget.ListView;

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
        addControls();
        getLoaiFromDB();
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
//                hienThiDialogChiTietLoai();
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
            getLoaiFromDB();
        }
    }

    private void getLoaiFromDB() {
        AdminHomeActivity.database = openOrCreateDatabase(AdminHomeActivity.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = AdminHomeActivity.database.rawQuery("SELECT * FROM MONAN", null);
        monAnAdapter.clear();
        while (cursor.moveToNext()) {
            String mamon = cursor.getString(0);
            String maloai = cursor.getString(1);
            String mact = cursor.getString(2);
            String tenmon = cursor.getString(3);
            String mota = cursor.getString(4);
            String hinhanh = cursor.getString(5);

            MonAn monan = new MonAn(mamon, maloai, mact, tenmon, mota, hinhanh);
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
        inflater.inflate(R.menu.action_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSua:
//                hienThiDialogEditLoai();
                break;

            case R.id.mnuXoa:
//                hienThiDialogXoaLoai();
                break;

        }
        return super.onContextItemSelected(item);
    }
}
