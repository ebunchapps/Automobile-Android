package com.awrtechnologies.carbudgetsales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;

import java.util.ArrayList;

/**
 * Created by awr001 on 11/07/15.
 */
public class SpinnerVehicle extends BaseAdapter {

    Context context;
    ArrayList<String> arraylist;
    LayoutInflater layoutinfltr;

    public SpinnerVehicle(Context context, ArrayList<String> array) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.arraylist = array;
        layoutinfltr = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return arraylist.get(position).toString().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = layoutinfltr.inflate(R.layout.spinnerlist, null);
        TextView text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(arraylist.get(position).toString());
        return convertView;
    }

}
