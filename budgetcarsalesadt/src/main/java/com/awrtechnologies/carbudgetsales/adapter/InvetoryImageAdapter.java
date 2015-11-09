package com.awrtechnologies.carbudgetsales.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.nostra13.universalimageloader.core.ImageLoader;

public class InvetoryImageAdapter extends PagerAdapter {

	Context context;
	// List<Inventory> arraylist;
	Inventory inventory;
	LayoutInflater layoutinfltr;
	public ImageLoader mLoader;
	String[] main;

	public InvetoryImageAdapter(Context context, Inventory inventory) {
		this.context = context;
		// this.arraylist = datalist;
		this.inventory = inventory;
		layoutinfltr = LayoutInflater.from(context);
		this.main = inventory.getImagesMain().split(",");
		mLoader = ImageLoader.getInstance();

	}

	@Override
	public int getCount() {
		return main.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((RelativeLayout) arg1);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemview = layoutinfltr.inflate(R.layout.galleryviewpagerlist,
				null);
		ImageView image = (ImageView) itemview
				.findViewById(R.id.imageViewCarImage);
		TextView text = (TextView) itemview.findViewById(R.id.textview);

		mLoader.displayImage(main[position].replace("\"", "").trim(), image);

		image.setVisibility(View.VISIBLE);
		text.setVisibility(View.VISIBLE);
		text.setText((position + 1) + "");

		container.addView(itemview);
		return itemview;

	}

	@Override
	public void destroyItem(View container, int position, Object object) {

		((ViewPager) container).removeView((RelativeLayout) object);

	}

	public void setList(Inventory invntory) {
		inventory = invntory;

	}

}
