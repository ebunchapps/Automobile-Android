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
import com.awrtechnologies.carbudgetsales.adapter.GarageViewpagerAdapter;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class GarageDetail extends Fragment implements OnPageChangeListener {

	ViewPager viewpage;
	GarageViewpagerAdapter viewpageadapter;
	List<Garage> arraylist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View carview = inflater.inflate(R.layout.cardealsdetail, null);
		viewpage = (ViewPager) carview.findViewById(R.id.viewp);
		arraylist = new ArrayList<Garage>();
		arraylist = Garage.getAll();
		viewpageadapter = new GarageViewpagerAdapter(getActivity(), arraylist);
		viewpage.setAdapter(viewpageadapter);
		viewpageadapter.notifyDataSetChanged();
		viewpage.setCurrentItem(PreferencesManager.getPreferenceIntByKey(
				getActivity(), "position"));
		viewpage.setOnPageChangeListener(this);
		return carview;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {

		String id = arraylist.get(position).getDealId();
		PreferencesManager.setPreferenceByKey(getActivity(), "id", id);

	}

}
