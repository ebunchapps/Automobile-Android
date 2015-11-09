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
import com.awrtechnologies.carbudgetsales.data.DocumentModel;
import com.awrtechnologies.carbudgetsales.data.DocumentModelDetails;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ModeldataAdapter extends BaseAdapter {

	Context context;
	List<DocumentModel> arraylist;
	LayoutInflater layinftr;
	Point point;
	public ImageLoader mLoader;

	public ModeldataAdapter(Context context, List<DocumentModel> arraylist) {
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

		DocumentModelDetails obj = DocumentModelDetails.getByModelId(arraylist
				.get(position).getModelId());

		h.modelname.setText(arraylist.get(position).getTitle());
//		String url = obj.getImageThumb();
		// url = url.replace("\\", "");

//		mLoader.displayImage(url, h.image);
		return convertView;
	}

	public class Holder {
		TextView modelname;
		ImageView image;
	}
}
