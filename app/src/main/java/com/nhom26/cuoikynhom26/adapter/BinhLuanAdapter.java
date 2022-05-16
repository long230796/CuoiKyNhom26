package com.nhom26.cuoikynhom26.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.BinhLuan;

/**
 * Created by long2 on 5/13/2022.
 */
public class BinhLuanAdapter extends ArrayAdapter<BinhLuan> {
    Activity context;
    int resource;



    public BinhLuanAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @android.support.annotation.NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);

        TextView ten = customView.findViewById(R.id.txtTenReviewer);
        TextView thoigian = customView.findViewById(R.id.txtThoigian);
        TextView noidung = customView.findViewById(R.id.txtNoidung);


        BinhLuan bl = getItem(position);
        ten.setText(bl.getPhone());
        thoigian.setText(bl.getNgaygio());
        noidung.setText(bl.getNoidung());



        return customView;
    }


}