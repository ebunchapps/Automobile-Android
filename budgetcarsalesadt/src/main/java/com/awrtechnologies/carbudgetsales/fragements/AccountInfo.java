package com.awrtechnologies.carbudgetsales.fragements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.FirstActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.DealCategory;
import com.awrtechnologies.carbudgetsales.data.DealerInfo;
import com.awrtechnologies.carbudgetsales.data.Deals;
import com.awrtechnologies.carbudgetsales.data.DocumentMake;
import com.awrtechnologies.carbudgetsales.data.DocumentModel;
import com.awrtechnologies.carbudgetsales.data.DocumentModelDetails;
import com.awrtechnologies.carbudgetsales.data.Garage;
import com.awrtechnologies.carbudgetsales.data.Inventory;
import com.awrtechnologies.carbudgetsales.data.ModelDetails;
import com.awrtechnologies.carbudgetsales.data.News;
import com.awrtechnologies.carbudgetsales.data.ServiceData;
import com.awrtechnologies.carbudgetsales.data.Social;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class AccountInfo extends Fragment implements OnClickListener {

	EditText name, phone, address, zippcode,uniqueid;
	TextView email;
	Button save;
	private User user;
	ImageView imagesharing;
	Button logout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View aview = inflater.inflate(R.layout.accountinfo, null);

		name = (EditText)aview.findViewById(R.id.edittext_Name);
		email = (TextView) aview.findViewById(R.id.edittext_Email);
		phone = (EditText) aview.findViewById(R.id.edittext_Phonenum);
		address = (EditText) aview.findViewById(R.id.edittext_Address);
		zippcode = (EditText) aview.findViewById(R.id.edittext_Zipcode);
		uniqueid= (EditText) aview.findViewById(R.id.edittext_uniqueId);
		imagesharing=(ImageView) aview.findViewById(R.id.imageview);
		save = (Button) aview.findViewById(R.id.button_save);
		
		logout = (Button) aview.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteAll();

			}
		});

		user = User.getUser();
		name.setText(user.getName());
		email.setText(user.getEmail());
		phone.setText(user.getPhone());
		address.setText(user.getAddress());
		zippcode.setText(user.getZipcode());
		uniqueid.setText(user.getHashid());
		uniqueid.setKeyListener(null);
		save.setOnClickListener(this);
		imagesharing.setOnClickListener(this);
		
		return aview;
	}

	
	protected void deleteAll() {
		new Delete().from(ServiceData.class).execute();
		new Delete().from(DealCategory.class).execute();
		new Delete().from(DealerInfo.class).execute();
		new Delete().from(Deals.class).execute();
		new Delete().from(DocumentMake.class).execute();
		new Delete().from(DocumentModel.class).execute();
		new Delete().from(DocumentModelDetails.class).execute();
		new Delete().from(User.class).execute();
		new Delete().from(Garage.class).execute();
		new Delete().from(Inventory.class).execute();
		new Delete().from(ModelDetails.class).execute();
		new Delete().from(News.class).execute();
		new Delete().from(Social.class).execute();
		PreferencesManager.clearAllPreferences(getActivity());
		Intent i = new Intent(getActivity(), FirstActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	
	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.button_save) {
			if (name.getText().toString().trim().equals("")) {
				showMessage("Enter Name!");
				return;
			}
			if (!isValidEmail(email.getText().toString())) {
				showMessage("Enter Email!");
				return;
			}
			if (phone.getText().toString().trim().equals("")) {
				showMessage("Enter Phone!");
				return;
			}

			if (phone.getText().toString().trim().equals("")) {
				showMessage("Enter Phone!");
				return;
			}
			if (address.getText().toString().trim().equals("")) {
				showMessage("Enter Address!");
				return;
			}
			if (zippcode.getText().toString().trim().equals("")) {
				showMessage("Enter Zip Code!");
				return;
			}
			new Task().execute();

		}
		if (id == R.id.imageview) {
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			sharingIntent.putExtra(Intent.EXTRA_TEXT, "Your HashId is :"+" "+user.getHashid());
			startActivity(Intent.createChooser(sharingIntent,"Share using"));
		}

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
						+ "/api/update_profile");
				ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
				BasicNameValuePair pasrd = new BasicNameValuePair("appid",
						Constants.APPID);
				param.add(pasrd);
				BasicNameValuePair pasrdd = new BasicNameValuePair(
						"server_key", Constants.SERVER_KEY);
				param.add(pasrdd);

				BasicNameValuePair pasrd1 = new BasicNameValuePair("email",
						email.getText().toString());
				param.add(pasrd1);
				BasicNameValuePair pasrd2 = new BasicNameValuePair("userid",
						user.getUserid());
				param.add(pasrd2);
				BasicNameValuePair pasrd3 = new BasicNameValuePair("phone",
						phone.getText().toString());
				param.add(pasrd3);
				BasicNameValuePair pasrd4 = new BasicNameValuePair("address",
						address.getText().toString());
				param.add(pasrd4);
				BasicNameValuePair pasrd5 = new BasicNameValuePair("zipcode",
						zippcode.getText().toString());
				param.add(pasrd5);
				BasicNameValuePair pasrd6 = new BasicNameValuePair("name", name
						.getText().toString());
				param.add(pasrd6);
				httppost.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse httpresponse = httpclient.execute(httppost);

				InputStream inputstream = httpresponse.getEntity().getContent();

				String result = convertStreamToString(inputstream);
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
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();
			try {
				JSONObject obj = new JSONObject(result);
				int rescode = obj.getInt("response_code");

				if (rescode == 1) {
					user.setName(name.getText().toString());
					user.setAddress(address.getText().toString());
					user.setEmail(email.getText().toString());
					user.setPhone(phone.getText().toString());
					user.setZipcode(zippcode.getText().toString());
					user.save();
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					alertDialogBuilder
							.setMessage("You are successfully updated")
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
				else if(rescode==0)
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					alertDialogBuilder
							.setMessage(obj.getString("message"))
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
		}
	}

	public void showMessage(String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		alertDialogBuilder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

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
