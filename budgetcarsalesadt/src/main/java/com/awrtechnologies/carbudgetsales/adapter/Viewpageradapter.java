package com.awrtechnologies.carbudgetsales.adapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Viewpageradapter extends PagerAdapter {

	Context context;
	List<Deals> arraylist;
	LayoutInflater layoutinfltr;
	Point size;
	public ImageLoader mLoader;

	public Viewpageradapter(Context context, List<Deals> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		layoutinfltr = LayoutInflater.from(context);
		size = GeneralHelper.getInstance(context).getScreenSize();
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

		View itemview = layoutinfltr.inflate(R.layout.cardetaillist, null);
		ImageView image = (ImageView) itemview
				.findViewById(R.id.imageViewCarImage);
		android.view.ViewGroup.LayoutParams lp = image.getLayoutParams();
		lp.height = size.x / 3;
		lp.width = size.x / 3;
		image.setLayoutParams(lp);
		TextView title = (TextView) itemview.findViewById(R.id.textitle);
		TextView price = (TextView) itemview.findViewById(R.id.price);

		TextView textkm = (TextView) itemview.findViewById(R.id.textkm);

		TextView description = (TextView) itemview.findViewById(R.id.textdesc);
		TextView textviewkm = (TextView) itemview.findViewById(R.id.textviewkm);
		Button save = (Button) itemview.findViewById(R.id.buttoncallseller);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Task().execute();
			}
		});
		title.setText(arraylist.get(position).getName());

		price.setText("$" + arraylist.get(position).getPrice());

		description.setText(arraylist.get(position).getDescription());
		boolean found = false;
		int i = 0;
		outer: for (String str : arraylist.get(position).getImagesType()
				.split(",")) {
			if (str.equals("image")) {

				found = true;
				mLoader.displayImage(arraylist.get(position).getImagesMain()
						.split(",")[i], image);
				break outer;
			}
			++i;
		}
		if (!found
				&& arraylist.get(position).getImagesMain().split(",").length > 0) {
			mLoader.displayImage("assets://pdficon.jpeg", image);
		}

		if (Integer.parseInt(arraylist.get(position).getVehicleDriven()) != -1) {
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

	public void setList(List<Deals> alist) {
		arraylist = alist;
		notifyDataSetChanged();
	}

	public class Task extends AsyncTask<Void, Void, String> {

		ProgressDialog mProgressDialog;
		JSONArray arry;
		private JSONObject object1;

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(Constants.BASEURL
						+ "/api/add_garage");
				ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
				BasicNameValuePair pasrd = new BasicNameValuePair("appid",
						Constants.APPID);
				param.add(pasrd);
				BasicNameValuePair pasrdd = new BasicNameValuePair(
						"server_key", Constants.SERVER_KEY);
				param.add(pasrdd);
				BasicNameValuePair pasrd1 = new BasicNameValuePair("userid",
						PreferencesManager
								.getPreferenceByKey(context, "userid"));
				param.add(pasrd1);
				BasicNameValuePair pasrd2 = new BasicNameValuePair("dealid",
						PreferencesManager.getPreferenceByKey(context, "id")
								+ "");
				param.add(pasrd2);

				httppost.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse httpresponse = httpclient.execute(httppost);

				InputStream inputstream = httpresponse.getEntity().getContent();

				String result = convertStreamToString(inputstream);
				
				System.out.println("id "+PreferencesManager.getPreferenceByKey(context, "id"));
				System.out.println("userid "+PreferencesManager.getPreferenceByKey(context, "userid"));
				
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Loading..");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("result " + result);
			try {
				JSONObject obj = new JSONObject(result);
				int rescode = obj.getInt("response_code");

				if (rescode == 1) {

					try {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder
								.setMessage("Deal has been added in garage.")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
												((MainActivity) context).loadData();

											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (rescode == 0) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
					alertDialogBuilder
							.setMessage("This deal is already in garage.")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();

										}
									});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressDialog.dismiss();
			
			super.onPostExecute(result);
		}

		public String convertStreamToString(InputStream inputstream)
				throws IOException {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = inputstream.read()) != -1) {
				bytestream.write(ch);
			}
			return new String(bytestream.toByteArray(), "UTF-8");
		}

	}
}
