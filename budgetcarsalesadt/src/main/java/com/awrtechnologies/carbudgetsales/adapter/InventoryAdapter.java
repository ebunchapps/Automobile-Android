package com.awrtechnologies.carbudgetsales.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class InventoryAdapter extends BaseAdapter {

	Context context;
	public List<Inventory> arraylist;
	LayoutInflater layinfltr;
	Point point;
	public ImageLoader mLoader;
	Datafilter datafilter;
	List<Inventory> filterlist;

	public InventoryAdapter(Context context, List<Inventory> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layinfltr = LayoutInflater.from(context);
		this.point = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
		this.filterlist = arraylist;
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
			convertView = layinfltr.inflate(R.layout.inventorylist, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.images_car);
			LayoutParams lp = holder.image.getLayoutParams();
			lp.height = point.x / 3;

			lp.width = point.x / 3;
			holder.image.setLayoutParams(lp);
			holder.status = (ImageView) convertView
					.findViewById(R.id.images_status);
			holder.make = (TextView) convertView.findViewById(R.id.text_make);
			holder.model = (TextView) convertView.findViewById(R.id.text_model);
			holder.year = (TextView) convertView.findViewById(R.id.text_year);
			holder.price = (TextView) convertView.findViewById(R.id.text_price);
			holder.category = (TextView) convertView
					.findViewById(R.id.text_category);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		String url = arraylist.get(position).getImagesMain().split(",")[0];
		mLoader.displayImage(url.trim(), holder.image);
		holder.make.setText(arraylist.get(position).getMake());
		holder.model.setText(arraylist.get(position).getModel());
		holder.year.setText(arraylist.get(position).getYear());
		try {
			holder.price.setText(String.format("$%.2f",
					Double.parseDouble(arraylist.get(position).getPrice())));
		} catch (Exception e) {
			holder.price.setVisibility(View.INVISIBLE);
		}
		holder.category.setText(arraylist.get(position).getCategory());
		String status = arraylist.get(position).getStatus();
		if (status.equals("Used")) {
			mLoader.displayImage("assets://used.png", holder.status);
		} else {
			mLoader.displayImage("assets://new.PNG", holder.status);
		}

		// holder.price.setFilters(new InputFilter{new DecimalHelper(5 , 2)});
		return convertView;
	}

	public class Holder {
		ImageView image, status;
		TextView make, model, year, price, category;
	}

	public void setList(List<Inventory> arraylist2) {
		arraylist = arraylist2;
	}

	public void refresh() {
		arraylist = Inventory.getAll();

		notifyDataSetChanged();
	}

	protected class Datafilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults datafilter = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				datafilter.values = filterlist;
				datafilter.count = filterlist.size();
			} else {
				ArrayList<Inventory> arraylist = new ArrayList<Inventory>();

				for (Inventory fd : filterlist) {
					if (fd.getMake().toUpperCase()
							.startsWith(constraint.toString().toUpperCase())
							|| fd.getModel()
							.toUpperCase()
							.startsWith(
									constraint.toString().toUpperCase())
							|| fd.getYear()
							.toUpperCase()
							.startsWith(
									constraint.toString().toUpperCase())) {
						arraylist.add(fd);
					}
				}
				datafilter.values = arraylist;
				datafilter.count = arraylist.size();
			}
			return datafilter;
		}

		@SuppressWarnings({ "unchecked", "unchecked" })
		@Override
		protected void publishResults(CharSequence constraint,
									  FilterResults results) {
			// TODO Auto-generated method stub
			if (results.count == 0) {
				notifyDataSetInvalidated();
			} else {
				arraylist = (ArrayList<Inventory>) results.values;
				notifyDataSetChanged();

			}
		}

	}



	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (datafilter == null) {
			datafilter = new Datafilter();
		}
		return datafilter;
	}
}
