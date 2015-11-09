package com.awrtechnologies.carbudgetsales.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GarageGalleryAdapter extends BaseAdapter {

	Context context;
	Garage garage;

	String type[];
	String main[];
	String thumb[];
	LayoutInflater layinfltr;
	Point point;
	public ImageLoader mLoader;

	public GarageGalleryAdapter(Context context, Garage garage) {
		this.context = context;
		this.garage = garage;
		layinfltr = LayoutInflater.from(context);
		this.point = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
		type = garage.getImagesType().split(",");
		main = garage.getImagesMain().split(",");
		thumb = garage.getImagesThumb().split(",");

	}

	@Override
	public int getCount() {
		return type.length;
	}

	@Override
	public Object getItem(int position) {
		return type[position];
	}

	@Override
	public long getItemId(int position) {
		return type[position].hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = layinfltr.inflate(R.layout.gallerylist, null);
		ImageView image = (ImageView) convertView.findViewById(R.id.imageview);
		LayoutParams lp = image.getLayoutParams();
		lp.height = point.x / 3;

		lp.width = point.x / 2;
		image.setLayoutParams(lp);

		// for (int i = 0; i < main.length; i++) {
		try{
		if (type[position].trim().equals("pdf")) {
			mLoader.displayImage("assets://pdficon.jpeg", image);
		} else {
			mLoader.displayImage(main[position], image);
			// System.out.println("image"+i+" "+thumb[i]);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		// }

		return convertView;
	}

}
