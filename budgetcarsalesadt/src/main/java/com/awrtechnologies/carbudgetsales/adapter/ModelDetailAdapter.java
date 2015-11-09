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
import com.awrtechnologies.carbudgetsales.data.DocumentModelDetails;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ModelDetailAdapter extends BaseAdapter {

	Context context;
	List<DocumentModelDetails> arraylist;
	LayoutInflater layinftr;
	Point point;
	public ImageLoader mLoader;

	public ModelDetailAdapter(Context context,
			List<DocumentModelDetails> arraylist2) {
		this.context = context;
		this.arraylist = arraylist2;
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
			convertView = layinftr.inflate(R.layout.modellistdata, null);
			h.modelname = (TextView) convertView.findViewById(R.id.text_name);
			h.image = (ImageView) convertView.findViewById(R.id.images_car);
			LayoutParams lp = h.image.getLayoutParams();
			lp.height = point.x / 4;

			lp.width = point.x / 3;
			h.image.setLayoutParams(lp);
			convertView.setTag(h);
		} else {
			h = (Holder) convertView.getTag();
		}

		h.modelname.setText(arraylist.get(position).getTitle());
		String url = arraylist.get(position).getImageThumb();
		// url = url.replace("\\", "");

		try{
			if (arraylist.get(position).getType().equals("pdf")) {
				mLoader.displayImage("assets://pdficon.jpeg",  h.image);
			} else {
				mLoader.displayImage(url, h.image);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		return convertView;
	}

	public class Holder {
		TextView modelname;
		ImageView image;
	}
}
