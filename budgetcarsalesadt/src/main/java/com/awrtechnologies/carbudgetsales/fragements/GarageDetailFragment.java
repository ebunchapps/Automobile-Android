package com.awrtechnologies.carbudgetsales.fragements;

import java.util.Stack;

import com.awrtechnologies.carbudgetsales.R;
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

public class GarageDetailFragment extends Fragment implements OnClickListener{
	TextView ownedvehicle;
	TextView favourities;
	public Fragment currentfragment;
	Stack<Fragment> fragmentstack;
	int position;
	String dealid;
	
	public GarageDetailFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public GarageDetailFragment(int position, String dealid) {
		
		this.dealid=dealid;
		this.position=position;
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
		PreferencesManager.setPreferenceByKey(getActivity(), "id", dealid);
		PreferencesManager.setPreferenceIntByKey(getActivity(), "position", position);
		GeneralHelper.getInstance(getActivity()).setIscheck(true);
		ownedvehicle.setBackgroundColor(Color.GRAY);
		favourities.setBackgroundColor(Color.WHITE);
		openFragment(new GarageDetail());
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonvehicle:
			ownedvehicle.setBackgroundColor(Color.GRAY);
			favourities.setBackgroundColor(Color.WHITE);
			openFragment(new GarageDetail());
			break;
			
		case R.id.buttonfavourities:
			favourities.setBackgroundColor(Color.GRAY);
			ownedvehicle.setBackgroundColor(Color.WHITE);
			openFragment(new GarageGallery());

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
