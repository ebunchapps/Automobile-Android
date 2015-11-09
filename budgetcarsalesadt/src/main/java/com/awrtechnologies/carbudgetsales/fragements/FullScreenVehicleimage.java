package com.awrtechnologies.carbudgetsales.fragements;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.VehicleImageadapter;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FullScreenVehicleimage extends Fragment{
	
	ViewPager viewpager;
	VehicleInfo vehicleinfo;
	VehicleImageadapter adapter;
	String vehicleid;
	int position;
	int position2;
	
	
	public FullScreenVehicleimage(int position, String vehicleid) {
		this.position=position;
		this.vehicleid=vehicleid;
	}


	

	public FullScreenVehicleimage(int position2) {
		this.position2=position2;
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_gallery, null);
		viewpager = (ViewPager) v.findViewById(R.id.viewp);
//		vehicleinfo=VehicleInfo.getById(vehicleid);
		vehicleinfo=VehicleInfo.getimages();
		adapter = new VehicleImageadapter(getActivity(), vehicleinfo);
		viewpager.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		viewpager.setCurrentItem(position2);
		return v;
	}

}
