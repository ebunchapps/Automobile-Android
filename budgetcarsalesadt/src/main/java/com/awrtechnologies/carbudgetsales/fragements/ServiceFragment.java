package com.awrtechnologies.carbudgetsales.fragements;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.fragements.service.Appointment;
import com.awrtechnologies.carbudgetsales.fragements.service.FastLane;
import com.awrtechnologies.carbudgetsales.fragements.service.FastLaneService;
import com.awrtechnologies.carbudgetsales.fragements.service.Maintenance;

public class ServiceFragment extends Fragment{



    private View rootView;
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_service, null);

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Service").setIndicator(getTabIndicator("Service")),
                FastLaneService.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Appointment").setIndicator(getTabIndicator("Appointment")),
                Appointment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Maintenance").setIndicator(getTabIndicator("Maintenance")),
                Maintenance.class, null);

        ((MainActivity) getActivity()).deals
                .setBackgroundResource(R.drawable.deals);
        ((MainActivity) getActivity()).inventory
                .setBackgroundResource(R.drawable.inventory);
        ((MainActivity) getActivity()).news
                .setBackgroundResource(R.drawable.news);
        ((MainActivity) getActivity()).tools
                .setBackgroundResource(R.drawable.tools);
        ((MainActivity) getActivity()).info
                .setBackgroundResource(R.drawable.information);
        ((MainActivity) getActivity()).service
                .setBackgroundResource(R.drawable.pressedservices);
        ((MainActivity) getActivity()).layoutShown();
        return rootView;

    }

    View getTabIndicator(String title){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator,null);
        TextView title_tv = (TextView) view.findViewById(R.id.title);
        title_tv.setText(title);
        return view;
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
