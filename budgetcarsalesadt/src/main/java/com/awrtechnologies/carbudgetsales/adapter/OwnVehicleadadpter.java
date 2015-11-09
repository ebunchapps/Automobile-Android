package com.awrtechnologies.carbudgetsales.adapter;

import java.util.List;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.InventoryAdapter.Holder;
import com.awrtechnologies.carbudgetsales.data.Inventory;
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
import android.widget.TextView;

public class OwnVehicleadadpter extends BaseAdapter{

	Context context;
	public List<VehicleInfo> arraylist;
	LayoutInflater layinfltr;
	Point point;
	public ImageLoader mLoader;
	
	public OwnVehicleadadpter(Context context,List<VehicleInfo> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layinfltr = LayoutInflater.from(context);
		this.point = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
		

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return arraylist.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = layinfltr.inflate(R.layout.ownvehiclelist, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.images_car);
			LayoutParams lp = holder.image.getLayoutParams();
			lp.height = point.x / 5;

			lp.width = point.x / 4;
			holder.image.setLayoutParams(lp);
			
			holder.make = (TextView) convertView.findViewById(R.id.text_make);
			holder.model = (TextView) convertView.findViewById(R.id.text_model);
			holder.year = (TextView) convertView.findViewById(R.id.text_year);
			holder.license = (TextView) convertView.findViewById(R.id.text_license);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		String[] type=arraylist.get(position).getUsertype().split(",");
		for (int i = 0; i < type.length; i++) {
			if(type[i].equals("dealer"))
			{
				String url = arraylist.get(position).getMain().split(",")[i];
				mLoader.displayImage(url.trim(), holder.image);
			}
		}
		
		holder.make.setText(arraylist.get(position).getMake());
		holder.model.setText(arraylist.get(position).getModel());
		holder.year.setText(arraylist.get(position).getYear());
		holder.license.setText(arraylist.get(position).getLicense());
		return convertView;
	}
	public class Holder {
		ImageView image;
		TextView make;
		TextView model;
		TextView year;
		TextView license;
	}
}
