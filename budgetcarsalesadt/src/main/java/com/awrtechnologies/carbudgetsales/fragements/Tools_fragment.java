package com.awrtechnologies.carbudgetsales.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;

public class Tools_fragment extends Fragment implements OnClickListener {

	ImageView calculator, flashlight, roadassistants;
	RelativeLayout accountinfo,vehicleinfo;
	ImageView settings;

	public Tools_fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View toolsview = inflater.inflate(R.layout.tools, null);
		GeneralHelper.getInstance(getActivity()).setIscheckfragment(false);
		RelativeLayout garage = (RelativeLayout) toolsview
				.findViewById(R.id.relative_vehicleinfo);
		garage.setOnClickListener(this);
		calculator = (ImageView) toolsview.findViewById(R.id.img_calcultor);
		flashlight = (ImageView) toolsview.findViewById(R.id.img_flashLite);
		roadassistants = (ImageView) toolsview
				.findViewById(R.id.img_roadassistance);
		vehicleinfo=(RelativeLayout) toolsview.findViewById(R.id.relative_vehiclereceipt);
		settings = (ImageView) toolsview.findViewById(R.id.img_settings);
		accountinfo = (RelativeLayout) toolsview
				.findViewById(R.id.relative_vehicleinfo);
		((MainActivity) getActivity()).deals
				.setBackgroundResource(R.drawable.deals);
		((MainActivity) getActivity()).inventory
				.setBackgroundResource(R.drawable.inventory);
		((MainActivity) getActivity()).news
				.setBackgroundResource(R.drawable.news);
		((MainActivity) getActivity()).tools
				.setBackgroundResource(R.drawable.pressedtools);
		((MainActivity) getActivity()).info
				.setBackgroundResource(R.drawable.information);
		((MainActivity) getActivity()).service
				.setBackgroundResource(R.drawable.services);
		calculator.setOnClickListener(this);
		flashlight.setOnClickListener(this);
		roadassistants.setOnClickListener(this);
		accountinfo.setOnClickListener(this);
		settings.setOnClickListener(this);
		vehicleinfo.setOnClickListener(this);
		return toolsview;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.img_calcultor) {
			((MainActivity) getActivity())
					.openNewFragment(new Calculator_fragment());
		}
		if (id == R.id.img_flashLite) {
			((MainActivity) getActivity())
					.openNewFragment(new Flashlight_fragment());
		}
		if (id == R.id.img_roadassistance) {
			((MainActivity) getActivity())
					.openNewFragment(new Roadassistants_fragment());
		}
		if (id == R.id.relative_vehicleinfo) {
			((MainActivity) getActivity()).openNewFragment(new Vehicleinfo());
		}
		if (id == R.id.img_settings) {
			((MainActivity) getActivity())
					.openNewFragment(new AccountInfo());
		}
		if(id==R.id.relative_vehiclereceipt)
		{
			((MainActivity)getActivity()).openNewFragment(new VehicleReceipt());
		}
	}

}
