package com.awrtechnologies.carbudgetsales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Appointments;
import com.awrtechnologies.carbudgetsales.data.Services;

import java.util.List;

/**
 * Created by m004 on 21/08/15.
 */
public class QuickServiceAdapter extends BaseAdapter {

    Context context;
    List<Services> arraylist;
    LayoutInflater layinflter;

    public QuickServiceAdapter(Context context, List<Services> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        layinflter=LayoutInflater.from(context);

    }



    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arraylist.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = layinflter.inflate(R.layout.appointmentlist, null);
            holder.textmakemodelyear= (TextView) convertView.findViewById(R.id.textmakemodelyear);
            holder.textstatus= (TextView) convertView.findViewById(R.id.textstatus);
            holder.textresult= (TextView) convertView.findViewById(R.id.textresult);
            holder.textrequestedtime= (TextView) convertView.findViewById(R.id.textrequestedtime);
            holder.textmessage= (TextView) convertView.findViewById(R.id.textmessage);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.textmakemodelyear.setText(arraylist.get(position).getMake()+"/"+arraylist.get(position).getModel()+"/"+arraylist.get(position).getYear());
        holder.textrequestedtime.setText(arraylist.get(position).getRequest_timestamp());
        holder.textresult.setText(arraylist.get(position).getResult());
        holder.textstatus.setText(arraylist.get(position).getStatus());
        holder.textmessage.setText(arraylist.get(position).getMessage());
        return convertView;
    }


    public class Holder{
        TextView textmakemodelyear;
        TextView textstatus;
        TextView textresult;
        TextView textrequestedtime;
        TextView textmessage;

    }
}
