package com.awrtechnologies.carbudgetsales.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GarageViewpagerAdapter extends PagerAdapter {

	Context context;
	List<Garage> arraylist;
	LayoutInflater layoutinfltr;
	Point size;
	public ImageLoader mLoader;
	String[] type;
	String[] main;
	String[] thumb;

	public GarageViewpagerAdapter(Context context, List<Garage> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layoutinfltr = LayoutInflater.from(context);
		size = GeneralHelper.getInstance(context).getScreenSize();
		mLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == ((RelativeLayout) arg1);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View itemview = layoutinfltr.inflate(R.layout.cardetaillist, null);
		ImageView image = (ImageView) itemview
				.findViewById(R.id.imageViewCarImage);
		android.view.ViewGroup.LayoutParams lp = image.getLayoutParams();
		lp.height = size.x / 3;
		lp.width = size.x / 3;
		image.setLayoutParams(lp);
		TextView title = (TextView) itemview.findViewById(R.id.textitle);
		TextView price = (TextView) itemview.findViewById(R.id.price);
		Button save = (Button) itemview.findViewById(R.id.buttoncallseller);
		save.setVisibility(View.GONE);
		TextView textkm = (TextView) itemview.findViewById(R.id.textkm);

		TextView description = (TextView) itemview.findViewById(R.id.textdesc);
		TextView textviewkm = (TextView) itemview.findViewById(R.id.textviewkm);

		title.setText(arraylist.get(position).getName());

		price.setText("$" + arraylist.get(position).getPrice());

		description.setText(arraylist.get(position).getDescription());

		type = arraylist.get(position).getImagesType().split(",");
		main = arraylist.get(position).getImagesMain().split(",");
		thumb = arraylist.get(position).getImagesThumb().split(",");

		for (int i = 0; i < main.length; i++) {
			if (type[0].equals("pdf")) {
				mLoader.displayImage("assets://pdficon.jpeg", image);
			} else {
				String url = thumb[0];
				mLoader.displayImage(url, image);
			}
		}

		if (!(arraylist.get(position).getVehicleDriven().equals("-1"))) {
			textviewkm.setVisibility(View.VISIBLE);
			textkm.setVisibility(View.VISIBLE);
			textkm.setText(arraylist.get(position).getKms() + ""
					+ "km");
		} else {
			textviewkm.setVisibility(View.GONE);
			textkm.setVisibility(View.GONE);
		}
		container.addView(itemview);
		return itemview;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {

		((ViewPager) container).removeView((RelativeLayout) object);

	}

	public void setList(List<Garage> alist) {
		arraylist = alist;

	}

}
