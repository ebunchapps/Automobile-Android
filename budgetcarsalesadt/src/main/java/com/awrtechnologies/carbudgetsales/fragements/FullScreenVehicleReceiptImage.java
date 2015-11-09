package com.awrtechnologies.carbudgetsales.fragements;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.VehicleImageadapter;
import com.awrtechnologies.carbudgetsales.adapter.VehicleReceiptimageadapter;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;

public class FullScreenVehicleReceiptImage extends Fragment{
	
	ViewPager viewpager;
	VehicleInfo vehicleinfo;
	VehicleReceiptimageadapter adapter;
	String vehicleid;
	int position;
	
	
	public FullScreenVehicleReceiptImage(int position) {
		this.position=position;
	}


	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_gallery, null);
		
		viewpager = (ViewPager) v.findViewById(R.id.viewp);
	
		vehicleinfo=VehicleInfo.getimages();
		if(vehicleinfo==null)
		{
			vehicleinfo = new VehicleInfo();
		}
		else{
			
		adapter = new VehicleReceiptimageadapter(getActivity(), vehicleinfo);
		viewpager.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		viewpager.setCurrentItem(position);
		((MainActivity) getActivity()).layoutShown();
		}
		return v;
	}
	@SuppressWarnings({ "unused", "deprecation" })
	private void Notify(String notificationTitle, String notificationMessage) {
		NotificationManager notificationManager = (NotificationManager) getActivity()
				.getSystemService(getActivity().NOTIFICATION_SERVICE);
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.ic_launcher,
				"New Message", System.currentTimeMillis());

		Intent notificationIntent = new Intent(getActivity(),
				MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
				0, notificationIntent, 0);

		notification.setLatestEventInfo(getActivity(), notificationTitle,
				notificationMessage, pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		notificationManager.notify(0, notification);
	}





	@Override
	public void onPause() {
		super.onPause();
	}





	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	
}
