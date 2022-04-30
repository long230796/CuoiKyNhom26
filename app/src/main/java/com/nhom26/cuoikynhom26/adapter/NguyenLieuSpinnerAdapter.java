package com.nhom26.cuoikynhom26.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhom26.cuoikynhom26.R;
import com.nhom26.cuoikynhom26.model.NguyenLieu;

import java.util.ArrayList;

/**
 * Created by long2 on 4/28/2022.
 */

public class NguyenLieuSpinnerAdapter extends ArrayAdapter<NguyenLieu> {

    public NguyenLieuSpinnerAdapter(Context context,
                              ArrayList<NguyenLieu> algorithmList) {
        super(context, 0, algorithmList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.spnItem);
        NguyenLieu currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.q
        if (currentItem != null) {
            textViewName.setText(currentItem.getTennl());
        }
        return convertView;
    }
}
