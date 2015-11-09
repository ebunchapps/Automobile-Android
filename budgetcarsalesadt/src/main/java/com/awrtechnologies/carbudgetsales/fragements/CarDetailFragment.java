package com.awrtechnologies.carbudgetsales.fragements;

import java.util.ArrayList;
import java.util.Stack;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.GarrageAdapter;
import com.awrtechnologies.carbudgetsales.adapter.OwnVehicleadadpter;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CarDetailFragment extends Fragment implements OnClickListener{

	TextView ownedvehicle;
	TextView favourities;
	public Fragment currentfragment;
	Stack<Fragment> fragmentstack;
	int position;
	int catgid;
	String dealId;
	
	public CarDetailFragment() {
		// TODO Auto-generated constructor stub
	}
	public CarDetailFragment(int position, int catgid, String dealId) {
		this.position=position;
		this.catgid=catgid;
		this.dealId=dealId;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.cardetail, null);
		ownedvehicle=(TextView) view.findViewById(R.id.buttonvehicle);
		favourities=(TextView) view.findViewById(R.id.buttonfavourities);
		
		fragmentstack = new Stack<Fragment>();
		ownedvehicle.setOnClickListener(this);
		favourities.setOnClickListener(this);
		PreferencesManager.setPreferenceByKey(getActivity(), "id", dealId);
		PreferencesManager.setPreferenceIntByKey(getActivity(), "position", position);
		PreferencesManager.setPreferenceIntByKey(getActivity(), "CATID", catgid);
		GeneralHelper.getInstance(getActivity()).setIscheck(true);
		ownedvehicle.setBackgroundColor(Color.GRAY);
		favourities.setBackgroundColor(Color.WHITE);
		openFragment(new CarsdealsDetail_fragment());
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonvehicle:
			ownedvehicle.setBackgroundColor(Color.GRAY);
			favourities.setBackgroundColor(Color.WHITE);
			openFragment(new CarsdealsDetail_fragment());
			break;
			
		case R.id.buttonfavourities:
			favourities.setBackgroundColor(Color.GRAY);
			ownedvehicle.setBackgroundColor(Color.WHITE);
			openFragment(new Gallery());

		default:
			break;
		}
		
		
	}
	
	
	public void openFragment(Fragment f) {

		try {
			InputMethodManager input = (InputMethodManager) 
					getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			input.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
		}
		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		fragmentstack.push(currentfragment);
		ft.replace(R.id.tabcontent, f);
		currentfragment = f;
		ft.commit();
	}

	public void back() {
		currentfragment = fragmentstack.pop();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		ft.replace(R.id.tabcontent, currentfragment);
		ft.commit();
	}

	public void onBackPressed() {
		try {
			if (fragmentstack.size() <= 1) {
				getActivity().finish();
			} else {
				back();
			}
		} catch (Exception e) {
			getActivity().finish();
		}

	}
}
