package com.awrtechnologies.carbudgetsales.fragements;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.Viewpageradapter;
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class CarsdealsDetail_fragment extends Fragment implements
		OnPageChangeListener {

	ViewPager viewpage;

	ArrayList<Deals> arraylist;
	Viewpageradapter viewpageadapter;

	int position;

	private int catid;

	private List<Deals> arryList;

	public CarsdealsDetail_fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View carview = inflater.inflate(R.layout.cardealsdetail, null);
		viewpage = (ViewPager) carview.findViewById(R.id.viewp);

		position = PreferencesManager.getPreferenceIntByKey(getActivity(),
				"position");
		catid = PreferencesManager
				.getPreferenceIntByKey(getActivity(), "CATID");

		arryList = Deals.getAllById(catid);
		if (arryList == null) {
			arryList = new ArrayList<Deals>();
		}
		viewpageadapter = new Viewpageradapter(getActivity(), arryList);
		viewpage.setAdapter(viewpageadapter);
		viewpageadapter.notifyDataSetChanged();
		viewpage.setCurrentItem(position);

		viewpage.setOnPageChangeListener(this);

		return carview;
	}

	@Override
	public void onPageScrollStateChanged(int position) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {

		String id = arryList.get(position).getDealId();
		PreferencesManager.setPreferenceByKey(getActivity(), "id", id);

	}
}
