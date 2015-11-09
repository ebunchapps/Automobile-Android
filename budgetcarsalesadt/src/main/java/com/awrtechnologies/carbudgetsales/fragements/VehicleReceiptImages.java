package com.awrtechnologies.carbudgetsales.fragements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

import ogrelab.org.apache.http.HttpResponse;
import ogrelab.org.apache.http.client.HttpClient;
import ogrelab.org.apache.http.client.methods.HttpPost;
import ogrelab.org.apache.http.entity.mime.MultipartEntity;
import ogrelab.org.apache.http.entity.mime.content.FileBody;
import ogrelab.org.apache.http.entity.mime.content.StringBody;
import ogrelab.org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.adapter.Vehiclereceiptadapter;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.data.VehicleInfo;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VehicleReceiptImages extends Fragment implements OnClickListener,
		OnItemClickListener {

	View view;

	GridView gridview;
	public ImageLoader mLoader;

	VehicleInfo vehicleimage;
	Vehiclereceiptadapter adapter;
	Point screensize;
	ImageView addimagebutton;
	private String pathName;
	User user;
	RelativeLayout relativeprogress;
	String vehicleid;

	private PendingIntent pendingIntent;

	public VehicleReceiptImages(String vehicleid) {
		this.vehicleid=vehicleid;
	}

	public VehicleReceiptImages() {
	}


	public void onImageSelect(String pathName) {
		this.pathName = pathName;
		// mLoader.displayImage("file://" + pathName, addimagebutton);
		try {
			// if (user.getHashid() == null) {
			// Toast.makeText(getActivity(),
			// "Error Occur!!You Can't Upload Receipt..",
			// Toast.LENGTH_LONG).show();
			// } else {

			new Task().execute();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.vehiclereceipt, null);
		gridview = (GridView) view.findViewById(R.id.gridview);
		relativeprogress = (RelativeLayout) view
				.findViewById(R.id.relativelayoutprogressbarheader);
		screensize = GeneralHelper.getInstance(getActivity()).getScreenSize();
		user = User.getUser();

		GeneralHelper.getInstance(getActivity()).setIscheckfragment(true);
		if (GeneralHelper.getInstance(getActivity()).isIscheckreceipt() == true) {

			Notify("CAR RECEIPT", "Car Receipt has been Uploaded.");
		}
		GeneralHelper.getInstance(getActivity()).setIscheckreceipt(false);
		addimagebutton = (ImageView) view.findViewById(R.id.buttonadd);
		mLoader = ImageLoader.getInstance();
		vehicleimage = VehicleInfo.getimages();
		// System.out.println("TYPEGBGG=========="+vehicleimage.getType());
		if (vehicleimage == null) {
			vehicleimage = new VehicleInfo();
		} else {
			adapter = new Vehiclereceiptadapter(getActivity(), vehicleimage);
			gridview.setAdapter(adapter);
		}
		setParametersForImage(addimagebutton);
		gridview.setOnItemClickListener(this);
		addimagebutton.setOnClickListener(this);
		((MainActivity) getActivity()).layoutShown();
		((MainActivity) getActivity()).deals
				.setBackgroundResource(R.drawable.deals);
		((MainActivity) getActivity()).inventory
				.setBackgroundResource(R.drawable.inventory);
		((MainActivity) getActivity()).news
				.setBackgroundResource(R.drawable.news);
		((MainActivity) getActivity()).tools
				.setBackgroundResource(R.drawable.pressedtools);
		((MainActivity) getActivity()).info
				.setBackgroundResource(R.drawable.information);
		((MainActivity) getActivity()).service
				.setBackgroundResource(R.drawable.services);
		return view;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GeneralHelper.getInstance(getActivity()).setIscheckreceipt(false);
		((MainActivity) getActivity())
				.openNewFragment(new FullScreenVehicleReceiptImage(position));
	}

	public void setParametersForImage(ImageView imageview) {

		LayoutParams lp = (LayoutParams) imageview.getLayoutParams();
		lp.height = screensize.y / 19;
		lp.width = screensize.x / 12;
		imageview.setLayoutParams(lp);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonadd:
			try {
				if (user.getHashid() == null) {
					Toast.makeText(getActivity(),
							"Error Occur!!You Can't Upload Receipt..",
							Toast.LENGTH_LONG).show();
				} else {

					((MainActivity) getActivity()).imageOpen(
							VehicleReceiptImages.this, 15, 10);
				}

				System.out.println("Pathname" + pathName);

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		default:
			break;
		}

	}

	public class Task extends AsyncTask<Void, Void, String> {

		ProgressDialog mProgressDialog;

		@Override
		protected String doInBackground(Void... params) {

			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(Constants.BASEURL
						+ "/api/carImageUpload");

				// ArrayList<MultipartEntity> param = new
				// ArrayList<MultipartEntity>();
				MultipartEntity pasrd = new MultipartEntity();

				System.out.println("USER HASHID===" + user.getId());
				pasrd.addPart("carId", new StringBody(vehicleid));
				pasrd.addPart("server_key", new StringBody(Constants.SERVER_KEY));
				// System.out.println("Hashid " + user.getHashid());
				// param.add(pasrd);

				File file = new File(pathName);
				// MultipartEntity pasrdd = new MultipartEntity();
				pasrd.addPart("image", new FileBody(file));

				// param.add(pasrdd);
				System.out.println("image " + pathName);

				httppost.setEntity(pasrd);
				HttpResponse httpresponse = httpclient.execute(httppost);

				InputStream inputstream = httpresponse.getEntity().getContent();

				String result = convertStreamToString(inputstream);

				System.out.println("Result api" + result);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";

		}

		@Override
		protected void onPreExecute() {
			 mProgressDialog = new ProgressDialog(getActivity());
			 mProgressDialog.setMessage("Loading..");
			 mProgressDialog.setCancelable(false);
			 mProgressDialog.show();
//			relativeprogress.setVisibility(View.VISIBLE);

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			 mProgressDialog.dismiss();

			relativeprogress.setVisibility(View.GONE);


			try {
				JSONObject object=new JSONObject(result);
				((MainActivity) getActivity()).loadData();

				GeneralHelper.getInstance(getActivity())
						.setIscheckreceipt(true);
				// Notify("CAR RECEIPT", "Car Receipt Information");

			} catch (Exception e) {
				Toast.makeText(getActivity(),
						"Try Again!!!!!",
						Toast.LENGTH_LONG).show();
			
				e.printStackTrace();
			}
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

	@SuppressWarnings({ "unused", "deprecation" })
	private void Notify(String notificationTitle, String notificationMessage) {
		NotificationManager notificationManager = (NotificationManager) getActivity()
				.getSystemService(getActivity().NOTIFICATION_SERVICE);
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.transparentlogo,
				"New Message", System.currentTimeMillis());

		Intent notificationIntent = new Intent(getActivity(),
				MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
				0, notificationIntent, 0);

		notification.setLatestEventInfo(getActivity(), notificationTitle,
				notificationMessage, pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		notificationManager.notify(0, notification);
	}

	@Override
	public void onPause() {
		System.out.println("ON PAUSE CALL");
		SharedPreferences shre = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor edit = shre.edit();
		edit.putString("imagepath", pathName);
		edit.commit();
		// GeneralHelper.getInstance(getActivity()).setIscheckfragment(false);

		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("ON RESUME CALL");
		super.onResume();
	}
}
