package com.awrtechnologies.carbudgetsales.adapter;

import java.util.List;

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
import com.awrtechnologies.carbudgetsales.data.DocumentMake;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MakedataAdapter extends BaseAdapter {

	Context context;
	List<DocumentMake> arraylist;
	LayoutInflater layinftr;
	Point point;
	public ImageLoader mLoader;

	public MakedataAdapter(Context context, List<DocumentMake> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layinftr = LayoutInflater.from(context);
		this.point = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return arraylist.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h;
		if (convertView == null) {
			h = new Holder();
			convertView = layinftr.inflate(R.layout.makelist, null);
			h.makename = (TextView) convertView.findViewById(R.id.text_name);
			h.makedate = (TextView) convertView.findViewById(R.id.text_date);
			h.image = (ImageView) convertView.findViewById(R.id.images_car);
			LayoutParams lp = h.image.getLayoutParams();
			lp.height = point.x / 3;

			lp.width = point.x / 3;
			h.image.setLayoutParams(lp);
			convertView.setTag(h);
		} else {
			h = (Holder) convertView.getTag();
		}

		h.makename.setText(arraylist.get(position).getName());
		h.makedate.setText(arraylist.get(position).getCreateDate());
		String url = arraylist.get(position).getImage();
		// url = url.replace("\\", "");

		mLoader.displayImage(url, h.image);
		return convertView;
	}

	public class Holder {
		TextView makename, makedate;
		ImageView image;
	}
}
