package com.awrtechnologies.carbudgetsales.adapter;

import java.util.ArrayList;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class VehicleGalleryadapter extends BaseAdapter {

	Context context;
	VehicleInfo vehicleinfo;
	LayoutInflater layinfltr;
	Point point;
	public ImageLoader mLoader;
	private ArrayList<String> typeList;
	private ArrayList<String> mainList;
	private ArrayList<String> thumbList;

	public VehicleGalleryadapter(Context context, VehicleInfo vehicleinfo) {

		this.context = context;
		this.vehicleinfo = vehicleinfo;
		layinfltr = LayoutInflater.from(context);
		this.point = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
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

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return typeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return typeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return typeList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = layinfltr.inflate(R.layout.gallerylist, null);
		ImageView image = (ImageView) convertView.findViewById(R.id.imageview);
		LayoutParams lp = image.getLayoutParams();
		lp.height = point.x / 4;

		lp.width = point.x / 3;
		image.setLayoutParams(lp);

		// for (int i = 0; i < main.length; i++) {
		if (typeList.get(position).trim().equals("pdf")) {
			mLoader.displayImage("assets://pdficon.jpeg", image);
		} else  {
			mLoader.displayImage(thumbList.get(position), image);
			System.out.println("Images===" + vehicleinfo.getUsertype());
		}
		// }

		return convertView;
	}

}
