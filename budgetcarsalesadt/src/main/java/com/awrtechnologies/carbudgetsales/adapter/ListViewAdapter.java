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
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListViewAdapter extends BaseAdapter {

	Context context;
	List<Deals> arraylist;
	LayoutInflater layoutinflt;
	Point point;
	public ImageLoader mLoader;

	public ListViewAdapter(Context context, List<Deals> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layoutinflt = LayoutInflater.from(context);
		this.point = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
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
			convertView = layoutinflt.inflate(R.layout.list_items, null);
			holder.images = (ImageView) convertView
					.findViewById(R.id.images_car);
			LayoutParams lp = holder.images.getLayoutParams();
			lp.height = point.x / 3;
			lp.width = point.x / 3;
			holder.images.setLayoutParams(lp);
			holder.name = (TextView) convertView.findViewById(R.id.text_name);
			holder.price = (TextView) convertView.findViewById(R.id.text_price);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.name.setText(arraylist.get(position).getName());
		holder.price.setText("$" + arraylist.get(position).getPrice());

		boolean found = false;
		int i = 0;
		outer: for (String str : arraylist.get(position).getImagesType()
				.split(",")) {
			if (str.equals("image")) {

				found = true;
				mLoader.displayImage(arraylist.get(position).getImagesMain()
						.split(",")[i], holder.images);
				break outer;
			}
			++i;

		}
		if (!found
				&& arraylist.get(position).getImagesMain().split(",").length > 0) {

			mLoader.displayImage("assets://pdficon.jpeg", holder.images);
		}

		return convertView;
	}

}

class Holder {
	ImageView images;
	TextView name;
	TextView price;

}
