package com.awrtechnologies.carbudgetsales.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.Dealslistadapter.Holder;
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GalleryAdapter extends BaseAdapter {

	Context context;
	Deals deal;

	String type[];
	String main[];
	String thumb[];
	LayoutInflater layinfltr;
	Point point;
	public ImageLoader mLoader;

	public GalleryAdapter(Context context, Deals deal) {
		this.context = context;
		this.deal = deal;
		layinfltr = LayoutInflater.from(context);
		this.point = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
		type = deal.getImagesType().split(",");
		main = deal.getImagesMain().split(",");
		thumb = deal.getImagesThumb().split(",");

		for (int i = 0; i < type.length; i++) {
			if (type[i].equals("image")) {
				System.out.println("IMAGES TYPE");
			} else if (type[i].equals("pdf")) {
				System.out.println("PDF TYPE");
			}else {
				System.out.println("MOT KNOWN "+type[i]);
			}
		}

		
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
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = layinfltr.inflate(R.layout.gallerylistitem, null);
			holder.image = (ImageView) convertView.findViewById(R.id.imageview);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		LayoutParams lp = holder.image.getLayoutParams();
		lp.height = point.x / 3;

		lp.width = point.x / 2;
		holder.image.setLayoutParams(lp);
		try {
//			for (String s : type) {
//				System.out.println("TYPE" + type);
//				if (s.trim().equals("image")) {
//					mLoader.displayImage(thumb[position], holder.image);
//
//				}
//				if (s.trim().equals("pdf")) {
//					mLoader.displayImage("assets://pdficon.jpeg", holder.image);
//				}
//			}
//			 for (int i = 0; i < type.length; i++) {
			 if (type[position].trim().equals("image")) {
			 mLoader.displayImage(main[position], holder.image);
			 System.out.println("Thumb image===" + thumb[position]);
			 } else if(type[position].trim().equals("pdf")){
			 System.out.println("PDf");
			 mLoader.displayImage("assets://pdficon.jpeg", holder.image);
			 }
//			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	class Holder {
		ImageView image;

	}

}
