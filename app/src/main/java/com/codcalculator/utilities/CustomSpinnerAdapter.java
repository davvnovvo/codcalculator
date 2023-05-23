package com.codcalculator.utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private int selectedItemColor;

    public CustomSpinnerAdapter(Context context, int resource, List<String> items, int selectedItemColor) {
        super(context, resource, items);
        this.context = context;
        this.selectedItemColor = selectedItemColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextColor(selectedItemColor);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextColor(selectedItemColor);
        return view;
    }
}

