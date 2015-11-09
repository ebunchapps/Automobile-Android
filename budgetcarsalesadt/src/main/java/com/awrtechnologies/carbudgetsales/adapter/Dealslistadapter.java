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
import com.awrtechnologies.carbudgetsales.data.DealCategory;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Dealslistadapter extends BaseAdapter {

	Context context;
	List<DealCategory> arraylist;
	LayoutInflater layoutinfltr;
	Point point;
	public ImageLoader mLoader;

	public Dealslistadapter(Context context, List<DealCategory> arrayList) {
		this.context = context;
		this.arraylist = arrayList;
		layoutinfltr = LayoutInflater.from(context);
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
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = layoutinfltr.inflate(R.layout.listitems_deals, null);
			holder.images = (ImageView) convertView
					.findViewById(R.id.images_car);
			LayoutParams lp = holder.images.getLayoutParams();
			lp.height = point.x / 4;

			lp.width = point.x / 3;
			holder.images.setLayoutParams(lp);
			holder.name = (TextView) convertView.findViewById(R.id.text_name);
			holder.number = (TextView) convertView
					.findViewById(R.id.text_number);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.name.setText(arraylist.get(position).getName());
		holder.number.setText(arraylist.get(position).getDealCount() + "");
		String url = arraylist.get(position).getMainImage();
		// url = url.replace("\\", "");

		mLoader.displayImage(url, holder.images);
		return convertView;
	}

	class Holder {
		ImageView images;
		TextView name;
		TextView number;

	}

	public void setList(List<DealCategory> alist) {
		arraylist = alist;
		notifyDataSetChanged();

	}
}
