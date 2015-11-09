package com.awrtechnologies.carbudgetsales.fragements;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.GalleryViewpagerAdapter;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class GalleryViewpager extends Fragment {

	ViewPager viewpager;
	GalleryViewpagerAdapter gadpater;

	int catid;
	String image;
	String type;
	int position;

	public GalleryViewpager(String image, String type, int position) {
		this.image = image;
		this.type = type;
		this.position = position;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_gallery, null);

		viewpager = (ViewPager) v.findViewById(R.id.viewp);
		catid = PreferencesManager
				.getPreferenceIntByKey(getActivity(), "CATID");
		ArrayList<String> arraylist = new ArrayList<String>();
		int i = 0;
		String main[] = image.split(",");
		for (String s : type.split(",")) {
			if (s.trim().equals("pdf")) {
				continue;
			}
			arraylist.add(main[i]);
			++i;
		}
		gadpater = new GalleryViewpagerAdapter(getActivity(), arraylist);
		viewpager.setAdapter(gadpater);
		viewpager.setCurrentItem(position);
		

		return v;
	}


}
