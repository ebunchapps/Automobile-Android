package com.awrtechnologies.carbudgetsales.fragements;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.OwnVehicleadadpter;
import com.awrtechnologies.carbudgetsales.data.DealerInfo;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m004 on 30/06/15.
 */
public class VehicleReceipt extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView listview;
    List<VehicleInfo> vehiclelist;
    OwnVehicleadadpter adapter;
    RelativeLayout relativelayout;
    TextView emailtext;
    TextView contacttext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.vehiclereceiptlist, null);
        listview = (ListView) view.findViewById(R.id.listview);
        relativelayout = (RelativeLayout) view.findViewById(R.id.addLayout);
        emailtext = (TextView) view.findViewById(R.id.email);
        contacttext = (TextView) view.findViewById(R.id.contact);
        vehiclelist = new ArrayList<VehicleInfo>();
        vehiclelist = VehicleInfo.getAll();
        if (vehiclelist == null || vehiclelist.size() <= 0) {
            relativelayout.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        } else {
            listview.setVisibility(View.VISIBLE);
            relativelayout.setVisibility(View.GONE);
            adapter = new OwnVehicleadadpter(getActivity(), vehiclelist);
            listview.setAdapter(adapter);
        }
        listview.setOnItemClickListener(this);
        try {
            DealerInfo di = DealerInfo.getDealer();

            emailtext.setText(di.getEmail());
            contacttext.setText(di.getPhone());
        } catch (Exception e)
        {

        }
        emailtext.setOnClickListener(this);
        contacttext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((MainActivity) getActivity()).openNewFragment(new VehicleReceiptImages(vehiclelist.get(position).getVehicleid()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email:
                DealerInfo di = DealerInfo.getDealer();
                emailtext.setText(Html.fromHtml(di.getEmail()));
                break;

            case R.id.contact:

                String phno = contacttext.getText().toString().replace("-", "");
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + phno));
                try {
                    startActivity(phoneIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Call failed,please try again later",
                            Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }

    }
}
