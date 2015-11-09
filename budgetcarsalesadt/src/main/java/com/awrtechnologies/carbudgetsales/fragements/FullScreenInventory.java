package com.awrtechnologies.carbudgetsales.fragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.InvetoryImageAdapter;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class FullScreenInventory extends Fragment {

	ViewPager viewpager;
	InvetoryImageAdapter gadpater;

	int id, position;
	Inventory inventory;

	public FullScreenInventory(int position) {
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_gallery, null);
		viewpager = (ViewPager) v.findViewById(R.id.viewp);
		int pminven = Integer.parseInt(PreferencesManager.getPreferenceByKey(
				getActivity(), "inventoryid"));

		inventory = Inventory.getById(Integer.parseInt(PreferencesManager
				.getPreferenceByKey(getActivity(), "inventoryid")));
		gadpater = new InvetoryImageAdapter(getActivity(), inventory);
		viewpager.setAdapter(gadpater);
		gadpater.notifyDataSetChanged();
		viewpager.setCurrentItem(position);

		return v;
	}

}
