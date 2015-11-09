package com.awrtechnologies.carbudgetsales.fragements.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.AppointmentAdapter;
import com.awrtechnologies.carbudgetsales.adapter.QuickServiceAdapter;
import com.awrtechnologies.carbudgetsales.data.Appointments;
import com.awrtechnologies.carbudgetsales.data.Services;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niteshpurohit on 20/08/15.
 */
public class QuickService extends Fragment {

    View view;
    ListView listview;
    QuickServiceAdapter adapter;
    List<Services> arraylist;
    String serviceId;
    Button request;

    public QuickService() {
    }

    public QuickService(String serviceId) {
        this.serviceId=serviceId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.quick_service, null);

        findviewbyid();
        arraylist=new ArrayList<Services>();
        arraylist=Services.getAllServicesByServiceID(serviceId);
        adapter=new QuickServiceAdapter(getActivity(),arraylist);
        listview.setAdapter(adapter);


        return view;
    }

    private void findviewbyid() {
        listview= (ListView) view.findViewById(R.id.listview);
    }







    public void showMessage(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
