package com.awrtechnologies.carbudgetsales.fragements;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.GarrageAdapter;
import com.awrtechnologies.carbudgetsales.adapter.OwnVehicleadadpter;
import com.awrtechnologies.carbudgetsales.data.DealerInfo;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class Vehicleinfo extends Fragment implements OnItemClickListener, OnClickListener {

    ListView list;
    GarrageAdapter garageadapter;
    List<Garage> listGd;

    TextView ownedvehicle;
    TextView favourities;

    OwnVehicleadadpter adapter;
    List<VehicleInfo> vehiclelist;

    Point screensize;
    RelativeLayout relativelayout;
    TextView emailtext;
    TextView contacttext;
    TextView addtext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.garage, null);
        relativelayout = (RelativeLayout) view.findViewById(R.id.addLayout);
        emailtext = (TextView) view.findViewById(R.id.email);
        contacttext = (TextView) view.findViewById(R.id.contact);
        addtext=(TextView)view.findViewById(R.id.addTextf);

        screensize = GeneralHelper.getInstance(getActivity()).getScreenSize();
        ownedvehicle = (TextView) view.findViewById(R.id.buttonvehicle);
        favourities = (TextView) view.findViewById(R.id.buttonfavourities);
        list = (ListView) view.findViewById(R.id.listview);


        vehiclelist = new ArrayList<VehicleInfo>();
        vehiclelist = VehicleInfo.getAll();
        if (vehiclelist == null || vehiclelist.size() <= 0) {
            list.setVisibility(View.GONE);
            addtext.setVisibility(View.GONE);
            relativelayout.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            relativelayout.setVisibility(View.GONE);
            adapter = new OwnVehicleadadpter(getActivity(), vehiclelist);
            list.setAdapter(adapter);
        }
        list.setOnItemClickListener(this);
        ownedvehicle.setBackgroundColor(Color.GRAY);
        favourities.setBackgroundColor(Color.WHITE);
        ownedvehicle.setOnClickListener(this);
        favourities.setOnClickListener(this);
        ((MainActivity) getActivity()).layoutShown();


        try
        {
            DealerInfo di = DealerInfo.getDealer();
            emailtext.setText(di.getEmail());
            contacttext.setText(di.getPhone());
        }catch(Exception e)
        {
        }
        emailtext.setOnClickListener(this);
        contacttext.setOnClickListener(this);

        return view;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        try {
            if (GeneralHelper.getInstance(getActivity()).isIscheckimage() == true) {
                ((MainActivity) getActivity()).openNewFragment(new VehicleImages(vehiclelist.get(position).getVehicleid()));
            } else if (GeneralHelper.getInstance(getActivity()).isIscheckimage() == false) {
                PreferencesManager.setPreferenceByKey(getActivity(), "TYPE", listGd
                        .get(position).getImagesType());
                String dealid = listGd.get(position).getDealId();

                ((MainActivity) getActivity()).openNewFragment(new GarageDetailFragment(position, dealid));
            }
        } catch (Exception e) {
            ((MainActivity) getActivity()).openNewFragment(new VehicleImages(vehiclelist.get(position).getVehicleid()));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonvehicle:
                addtext.setVisibility(View.GONE);
                GeneralHelper.getInstance(getActivity()).setIscheckimage(true);
                ownedvehicle.setBackgroundColor(Color.GRAY);
                favourities.setBackgroundColor(Color.WHITE);
                vehiclelist = new ArrayList<VehicleInfo>();
                vehiclelist = VehicleInfo.getAll();
                for (int i = 0; i < vehiclelist.size(); i++) {
                }
                if (vehiclelist == null || vehiclelist.size() <= 0) {
                    list.setVisibility(View.GONE);
                    addtext.setVisibility(View.GONE);
                    relativelayout.setVisibility(View.VISIBLE);
                } else {
                    list.setVisibility(View.VISIBLE);
                    addtext.setVisibility(View.GONE);
                    relativelayout.setVisibility(View.GONE);
                    adapter = new OwnVehicleadadpter(getActivity(), vehiclelist);
                    list.setAdapter(adapter);
                }

                break;

            case R.id.buttonfavourities:
                GeneralHelper.getInstance(getActivity()).setIscheckimage(false);
                favourities.setBackgroundColor(Color.GRAY);
                ownedvehicle.setBackgroundColor(Color.WHITE);
                listGd = new ArrayList<Garage>();
                listGd = Garage.getAll();
                if(listGd==null || listGd.size() <=0)
                {
                    list.setVisibility(View.GONE);
                    relativelayout.setVisibility(View.GONE);
                    addtext.setVisibility(View.VISIBLE);
                }
                else
                {
                    relativelayout.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    garageadapter = new GarrageAdapter(getActivity(), listGd);
                    list.setAdapter(garageadapter);
                }

                break;


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

    public void setParametersForImage(ImageView imageview) {

        LayoutParams lp = (LayoutParams) imageview.getLayoutParams();
        lp.height = screensize.y / 16;
        lp.width = screensize.x / 12;
        imageview.setLayoutParams(lp);

    }

}
