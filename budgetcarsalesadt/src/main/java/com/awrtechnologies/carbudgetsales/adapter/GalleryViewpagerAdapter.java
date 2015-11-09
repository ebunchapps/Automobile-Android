package com.awrtechnologies.carbudgetsales.adapter;

import java.util.ArrayList;

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
import com.nostra13.universalimageloader.core.ImageLoader;

public class GalleryViewpagerAdapter extends PagerAdapter {

	Context context;
	ArrayList<String> arraylist;
	LayoutInflater layoutinfltr;

	public ImageLoader mLoader;

	public GalleryViewpagerAdapter(Context context, ArrayList<String> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layoutinfltr = LayoutInflater.from(context);

		mLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return arraylist.size();
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

		image.setVisibility(View.VISIBLE);
		text.setVisibility(View.VISIBLE);
		text.setText((position + 1) + "");
		mLoader.displayImage(arraylist.get(position), image);

		container.addView(itemview);
		return itemview;

	}

	@Override
	public void destroyItem(View container, int position, Object object) {

		((ViewPager) container).removeView((RelativeLayout) object);

	}

	public void setList(ArrayList<String> alist) {
		arraylist = alist;
		notifyDataSetChanged();
	}
}
