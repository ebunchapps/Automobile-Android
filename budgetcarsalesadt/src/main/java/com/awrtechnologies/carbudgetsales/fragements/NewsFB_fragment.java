package com.awrtechnologies.carbudgetsales.fragements;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsFB_fragment extends Fragment implements View.OnClickListener{

	View newsfbview;
	TextView newstitlefb;
	TextView newstype;
	TextView newsdescfb;
	TextView newsdatefb;
	TextView newssubtitle;
	TextView newstitlelocal;
	TextView newsdatelocal;
	TextView newsdesclocal;
	TextView newscontact;
	TextView newsemaill;
	TextView textviewtype;
	ImageView newsimage;
	
	RelativeLayout rlFb;
	RelativeLayout rlLocal;
	
	String newstilted;
	String newsdated;
	String newsdescd;
	String newstyped;
	String newsimaged;
	String newssubtitled;
	String newscontectno;
	String newsemail;
	
	Point size;
	public ImageLoader mLoader;
	LinearLayout lr;

	public NewsFB_fragment(String newstilted, String newsdated,
			String newsdescd, String newstyped, String newsimaged,
			String newssubtitled, String newscontectno, String newsemail) {
		this.newstilted = newstilted;
		this.newsdated = newsdated;
		this.newsdescd = newsdescd;
		this.newstyped = newstyped;
		this.newsimaged = newsimaged;
		this.newssubtitled = newssubtitled;
		this.newscontectno = newscontectno;
		this.newsemail = newsemail;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		newsfbview = inflater.inflate(R.layout.newsfb, null);
		size = GeneralHelper.getInstance(getActivity()).getScreenSize();
		mLoader = ImageLoader.getInstance();
		oncreate();
		
		LayoutParams lp = newsimage.getLayoutParams();
		lp.height = size.x / 3;
		lp.width = size.x / 2;
		newsimage.setLayoutParams(lp);


		if (newstyped.equals("FB")) {
			rlFb.setVisibility(View.VISIBLE);
			rlLocal.setVisibility(View.GONE);

			textviewtype.setBackgroundResource(R.drawable.facebook);
			newstitlefb.setText(newstilted);
			newsdatefb.setText(newsdated);
			newsdescfb.setText(newsdescd);
		}else if (newstyped.equals("TW")) {
			rlFb.setVisibility(View.VISIBLE);
			rlLocal.setVisibility(View.GONE);
			textviewtype.setBackgroundResource(R.drawable.twitter);
			newstitlefb.setText(newstilted);
			newsdatefb.setText(newsdated);
			newsdescfb.setText(newsdescd);
		}
		else if (newstyped.equals("local")) {
			rlFb.setVisibility(View.GONE);
			rlLocal.setVisibility(View.VISIBLE);
			lr.setVisibility(View.VISIBLE);
			newstitlelocal.setText(newstilted);
			newsdatelocal.setText(newsdated);
//			newssubtitle.setText(newssubtitled);
			newsdesclocal.setText(newsdescd);
			newsemaill.setText(newsemail);
			newscontact.setText(newscontectno);
			if(newsimaged.equals(""))
			{
				newsimage.setVisibility(View.GONE);
			}else {
				newsimage.setVisibility(View.VISIBLE);
				String url = newsimaged;
				// url = url.replace("\\", "");
				mLoader.displayImage(url, newsimage);
			}
		}

		newstitlefb.setOnClickListener(this);
		newsemaill.setOnClickListener(this);
		newscontact.setOnClickListener(this);
		newsdescfb.setOnClickListener(this);
		return newsfbview;
	}

	private void oncreate() {
		lr = (LinearLayout) newsfbview
				.findViewById(R.id.linearlayoutcontactemail);
		rlFb = (RelativeLayout) newsfbview.findViewById(R.id.relativelayoutfb);
		rlLocal = (RelativeLayout) newsfbview
				.findViewById(R.id.relativelayoutlocal);
		newstitlefb = (TextView) newsfbview.findViewById(R.id.textviewtitle);
		newstype = (TextView) newsfbview.findViewById(R.id.textviewtype);
		newsdatefb = (TextView) newsfbview.findViewById(R.id.textviewdate);
		newsdescfb = (TextView) newsfbview.findViewById(R.id.textviewdesc);

		newstitlelocal = (TextView) newsfbview
				.findViewById(R.id.textviewtitlel);
		newsdatelocal = (TextView) newsfbview
				.findViewById(R.id.textviewdatelocal);
		newsdesclocal = (TextView) newsfbview.findViewById(R.id.textviewdescl);
		newssubtitle = (TextView) newsfbview
				.findViewById(R.id.textviewsubtitlel);
		newscontact = (TextView) newsfbview.findViewById(R.id.textviewcontact);
		newsemaill = (TextView) newsfbview.findViewById(R.id.textviewemail);
		newsimage = (ImageView) newsfbview.findViewById(R.id.imageview);
		textviewtype=(TextView)newsfbview.findViewById(R.id.textviewtype);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.textviewtitle:
				try {
							Intent myIntent;
							myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newstilted));
							getActivity().startActivity(myIntent);
				} catch (ActivityNotFoundException e) {
							Toast.makeText(getActivity(), "No application can handle this request."
									+ " Please install a webbrowser", Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
				break;

			case R.id.textviewcontact:

				Intent phoneIntent = new Intent(Intent.ACTION_CALL);
				phoneIntent.setData(Uri.parse("tel:" + newscontectno));
				try {
					startActivity(phoneIntent);

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "Call failed,please try again later",
							Toast.LENGTH_SHORT).show();
				}

				break;

			case R.id.textviewemail:
				newsemaill.setText(Html.fromHtml(newsemail));
				break;

			case R.id.textviewdesc:
				try {
					Intent myIntent;
					myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsdescd));
					getActivity().startActivity(myIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(getActivity(), "No application can handle this request."
							+ " Please install a webbrowser", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				break;
		}
	}
}
