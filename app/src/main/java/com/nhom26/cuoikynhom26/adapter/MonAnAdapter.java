package com.nhom26.cuoikynhom26.adapter;
import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by CANH-PC on 05/11/22.
 */

public class MonAnAdapter extends ArrayAdapter<MonAn>{
    Context context;
    int resource;
    ArrayList<MonAn> arrayListMA;

    public MonAnAdapter(Context context, int resource, @NonNull ArrayList<MonAn> arrayListMA) {
        super(context, resource, arrayListMA);
        this.arrayListMA = arrayListMA;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return arrayListMA.size();
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

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(resource, null);
        MonAn ma = getItem(position);

        ImageView imgHinh = convertView.findViewById(R.id.img_monuathich);
        TextView txtTenMon = convertView.findViewById(R.id.txt_temon);
        TextView txtMoTaMon = convertView.findViewById(R.id.txt_motamon);

//        imgHinh.setImageResource(Integer.parseInt(ma.getAnhminhhoa()));
        imgHinh.setImageBitmap(StringToBitMap(ma.getAnhminhhoa()));
        txtTenMon.setText(ma.getTenmon());
        txtMoTaMon.setText(ma.getMota());

        return convertView;
    }
}
