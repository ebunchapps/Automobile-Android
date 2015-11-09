package com.awrtechnologies.carbudgetsales.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.News;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsAdapter extends BaseAdapter {

	Context context;
	List<News> arraylist;
	LayoutInflater layinflt;
	Point size;
	public ImageLoader mLoader;

	public NewsAdapter(Context context, List<News> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layinflt = LayoutInflater.from(context);
		this.size = GeneralHelper.getInstance(context).getScreenSize();
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = layinflt.inflate(R.layout.newsdata, null);

			holder.rlFb = (RelativeLayout) convertView
					.findViewById(R.id.relativelayoutfb);
			holder.rlLocal = (RelativeLayout) convertView
					.findViewById(R.id.relativelayoutlocal);
			holder.newstitlefb = (TextView) convertView
					.findViewById(R.id.textviewtitle);
			holder.newstype = (TextView) convertView
					.findViewById(R.id.textviewtype);
			holder.newsdatefb = (TextView) convertView
					.findViewById(R.id.textviewdate);
			holder.newsdescfb = (TextView) convertView
					.findViewById(R.id.textviewdesc);

			holder.newstitlelocal = (TextView) convertView
					.findViewById(R.id.textviewtitlel);
			holder.newsdatelocal = (TextView) convertView
					.findViewById(R.id.textviewdatelocal);
			holder.newsdesclocal = (TextView) convertView
					.findViewById(R.id.textviewdescl);
			holder.newssubtitle = (TextView) convertView
					.findViewById(R.id.textviewsubtitlel);
			holder.newsimage = (ImageView) convertView
					.findViewById(R.id.imageview);
			holder.contact = (TextView) convertView
					.findViewById(R.id.contectno);
			holder.email = (TextView) convertView.findViewById(R.id.email);
			holder.fbtype=(TextView)convertView.findViewById(R.id.fbtype);
			LayoutParams lp = holder.newsimage.getLayoutParams();
			lp.height = size.x / 5;
			lp.width = size.x / 4;
			holder.newsimage.setLayoutParams(lp);


			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Log.d("Pawan","TITLE"+(arraylist.get(position).getTitle()));
		if (arraylist.get(position).getTitle().equals("")) {
			holder.rlFb.setVisibility(View.GONE);
			holder.rlLocal.setVisibility(View.GONE);
		} else {
			if (arraylist.get(position).getNewsType().equalsIgnoreCase("FB")) {
				holder.rlFb.setVisibility(View.VISIBLE);
				holder.rlLocal.setVisibility(View.GONE);

				holder.fbtype.setBackgroundResource(R.drawable.facebook);
				holder.newstitlefb.setText(arraylist.get(position).getTitle());
				holder.newsdatefb.setText(arraylist.get(position)
						.getCreateDate());
				holder.newsdescfb.setText(arraylist.get(position)
						.getDescription());
			}if (arraylist.get(position).getNewsType().equalsIgnoreCase("TW")) {
				holder.rlFb.setVisibility(View.VISIBLE);
				holder.rlLocal.setVisibility(View.GONE);
				holder.fbtype.setBackgroundResource(R.drawable.twitter);
				holder.newstitlefb.setText(arraylist.get(position).getTitle());
				holder.newsdatefb.setText(arraylist.get(position)
						.getCreateDate());
				holder.newsdescfb.setText(arraylist.get(position)
						.getDescription());

		}else if (arraylist.get(position).getNewsType()
					.equalsIgnoreCase("local")) {
				holder.rlFb.setVisibility(View.GONE);
				holder.rlLocal.setVisibility(View.VISIBLE);
				holder.newstitlelocal.setText(arraylist.get(position)
						.getTitle());
				holder.newsdatelocal.setText(arraylist.get(position)
						.getCreateDate());
//				holder.newssubtitle.setText(arraylist.get(position)
//						.getSubTitle());
				holder.newsdesclocal.setText(arraylist.get(position)
						.getDescription());
				String url = arraylist.get(position).getPicture();
				// url = url.replace("\\", "");
				if(url.equals(""))
				{
					holder.newsimage.setVisibility(View.GONE);
				}else
				{
					holder.newsimage.setVisibility(View.VISIBLE);
					System.out.println("IMAGE LOCAL" + url);
					mLoader.displayImage(url, holder.newsimage);
				}

				holder.contact.setText(arraylist.get(position).getContactNo());
				holder.email.setText(arraylist.get(position).getEmail());

//				holder.newstitlelocal.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						try {
//							Intent myIntent;
//							myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arraylist.get(position)
//									.getTitle()));
//							context.startActivity(myIntent);
//						} catch (ActivityNotFoundException e) {
//							Toast.makeText(context, "No application can handle this request."
//									+ " Please install a webbrowser", Toast.LENGTH_LONG).show();
//							e.printStackTrace();
//						}
//					}
//				});
			}
		}
		return convertView;
	}

	public class Holder {
		TextView newstitlefb, newstype, newsdescfb, newsdatefb, newssubtitle,
				newstitlelocal, newsdatelocal, newsdesclocal, contact, email;
		ImageView newsimage;
		RelativeLayout rlFb, rlLocal;
		TextView fbtype;
	}

}
