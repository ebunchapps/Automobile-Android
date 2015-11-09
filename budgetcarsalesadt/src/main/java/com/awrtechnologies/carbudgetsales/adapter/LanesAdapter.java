package com.awrtechnologies.carbudgetsales.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Lanes;
import com.awrtechnologies.carbudgetsales.data.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by m004 on 20/08/15.
 */
public class LanesAdapter extends BaseAdapter {


    private final List<Services> services;
    private final SimpleDateFormat webDateFormat;
    private final HashMap<Integer, CountDownTimer> timerMap;
    Context context;
    LayoutInflater layoutinflter;
    List<Lanes> arraylist;
    CountDownTimer cdt = null;
    public Holder holder;

    public LanesAdapter(Context context, List<Lanes> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        layoutinflter = LayoutInflater.from(context);
        services = Services.getAllServices();
        webDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timerMap = new HashMap<Integer,CountDownTimer>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new Holder();
            convertView = layoutinflter.inflate(R.layout.laneslist, null);
            holder.counttime = (TextView) convertView.findViewById(R.id.texttimecount);
            holder.lanename = (TextView) convertView.findViewById(R.id.lanesname);
            holder.myrequest = (TextView) convertView.findViewById(R.id.myrequest);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        try {
            Date booked_until = webDateFormat.parse(arraylist.get(position).getBooked_until());
            Date nowDate = new Date();
            if (booked_until.getTime() > nowDate.getTime()) {
                startTimer(booked_until.getTime() - nowDate.getTime(), holder.counttime,position);
            } else {
                holder.counttime.setText("Lane is Empty");
            }
        } catch (ParseException e) {
            holder.counttime.setText("Lane is Empty");
            e.printStackTrace();
        }


        if (services == null || services.size() <= 0) {
            holder.myrequest.setText("");
        } else {
            boolean alreadyRequested = false;
            Date booked_until = null;
            Date nowDate = new Date();
            for(Services service : services){

                try {
                    booked_until = webDateFormat.parse(service.getRequest_timestamp());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (booked_until.getTime() > nowDate.getTime()) {
                    alreadyRequested = true;
                }
            }
            if(alreadyRequested){
                holder.myrequest.setText("Already in Queue");
                startTimer(booked_until.getTime() - nowDate.getTime(), holder.counttime,position);
            }else{
                holder.myrequest.setText("");
            }

        }

        holder.lanename.setText(arraylist.get(position).getLanename());
        return convertView;
    }

    private void startTimer(long totalMilisecondsForTimer, final TextView counttime, int position) {
        try{
            CountDownTimer cdt = timerMap.get(position);
            cdt.cancel();
        }catch (Exception e){

        }
        CountDownTimer timer = new CountDownTimer(totalMilisecondsForTimer, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

            public void onTick(long millisUntilFinished) {
                int totalSecs = (int) (millisUntilFinished / 1000);
                int hours = totalSecs / 3600;
                int minutes = (totalSecs % 3600) / 60;
                int seconds = totalSecs % 60;

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                counttime.setText(timeString);
            }

            public void onFinish() {
                counttime.setText("");
            }
        };
        timerMap.put(position,timer);
        timer.start();
    }

    public class Holder {
        TextView lanename;
       public TextView counttime;
        TextView myrequest;

    }

}
