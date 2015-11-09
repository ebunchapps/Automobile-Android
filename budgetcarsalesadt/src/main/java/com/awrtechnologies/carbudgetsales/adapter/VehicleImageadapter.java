package com.awrtechnologies.carbudgetsales.adapter;

import java.util.ArrayList;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VehicleImageadapter extends PagerAdapter{

	
	Context context;
	// List<Inventory> arraylist;
	VehicleInfo vehicleinfo;
	LayoutInflater layoutinfltr;
	public ImageLoader mLoader;
//	String[] main;
	private ArrayList<String> typeList;
	private ArrayList<String> mainList;
	private ArrayList<String> thumbList;
	
	public VehicleImageadapter(Context context,VehicleInfo vehicleinfo) {
		this.context = context;
		// this.arraylist = datalist;
		this.vehicleinfo = vehicleinfo;
		layoutinfltr = LayoutInflater.from(context);
//		this.main = vehicleinfo.getMain().split(",");
		String[] userType = vehicleinfo.getUsertype().split(",");
		String[] type = vehicleinfo.getType().split(",");
		String[] main = vehicleinfo.getMain().split(",");
		String[] thumb = vehicleinfo.getThumb().split(",");

		typeList = new ArrayList<String>();
		mainList = new ArrayList<String>();
		thumbList = new ArrayList<String>();

		for (int i = 0; i < userType.length; ++i) {
			if (userType[i].equals("dealer")) {
				typeList.add(type[i]);
				mainList.add(main[i]);
				thumbList.add(thumb[i]);
			}
		}
		mLoader = ImageLoader.getInstance();
	}
	
	@Override
	public int getCount() {
		return typeList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((RelativeLayout) arg1);
	}
	
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemview = layoutinfltr.inflate(R.layout.galleryviewpagerlist,
				null);
		ImageView image = (ImageView) itemview
				.findViewById(R.id.imageViewCarImage);
		TextView text = (TextView) itemview.findViewById(R.id.textview);

		mLoader.displayImage(mainList.get(position).replace("\"", "").trim(), image);

		image.setVisibility(View.VISIBLE);
		text.setVisibility(View.VISIBLE);
		text.setText((position + 1) + "");

		container.addView(itemview);
		return itemview;

	}

	@Override
	public void destroyItem(View container, int position, Object object) {

		((ViewPager) container).removeView((RelativeLayout) object);

	}

	public void setList(VehicleInfo vehicleinfo) {
		vehicleinfo = vehicleinfo;

	}

}
