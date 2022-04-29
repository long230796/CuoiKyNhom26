package com.nhom26.cuoikynhom26.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.NguyenLieu;

/**
 * Created by long2 on 4/28/2022.
 */

public class NguyenLieuAdapter extends ArrayAdapter<NguyenLieu> {
    Activity context;
    int resource;



    public NguyenLieuAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @android.support.annotation.NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);

        TextView txtTenNL = customView.findViewById(R.id.txtTenNL);
        TextView txtDinhLuong = customView.findViewById(R.id.txtDinhLuong);
        TextView txtDonVi = customView.findViewById(R.id.txtDonVi);


        NguyenLieu nl = getItem(position);
        txtTenNL.setText(nl.getTennl());
        txtDinhLuong.setText(nl.getDinhluong());
        txtDonVi.setText(nl.getDonvi());



        return customView;
    }


}