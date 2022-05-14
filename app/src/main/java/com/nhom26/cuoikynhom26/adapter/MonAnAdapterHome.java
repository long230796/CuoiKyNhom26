package com.nhom26.cuoikynhom26.adapter;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.MonAn;

/**
 * Created by apoll on 5/9/2022.
 */

public class MonAnAdapterHome extends ArrayAdapter<MonAn>{
    Activity context;
    int resource;

    public MonAnAdapterHome(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource =resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource,null);

        ImageView imgMonAn = (ImageView) customView.findViewById(R.id.imgMonAn_search);
        TextView txtTenMon = (TextView) customView.findViewById(R.id.txtTenMon_search);

        MonAn monAn = getItem(position);
        imgMonAn.setImageBitmap(StringToBitMap(monAn.getAnhminhhoa()));
        txtTenMon.setText(monAn.getTenmon());

        return customView;
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
}
