package com.awrtechnologies.carbudgetsales.fragements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.fragements.Signin_fragment.Task;
import com.awrtechnologies.carbudgetsales.helper.ConnectionDetector;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class SignUp_fragment extends Fragment implements OnClickListener {

	EditText name, email, phono, address, zipcode, password;
	Button save;
	long Userphono;
	String Userzipcode;
	String Username, Useremail, Useraddress, Userpassword;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	public SignUp_fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View informationview = inflater.inflate(R.layout.signup, null);
		name = (EditText) informationview.findViewById(R.id.edittext_Name);
		email = (EditText) informationview.findViewById(R.id.edittext_Email);
		phono = (EditText) informationview.findViewById(R.id.edittext_Phonenum);
		address = (EditText) informationview
				.findViewById(R.id.edittext_Address);
		zipcode = (EditText) informationview
				.findViewById(R.id.edittext_Zipcode);
		save = (Button) informationview.findViewById(R.id.button_save);
		password = (EditText) informationview
				.findViewById(R.id.edittext_password);

		// creating connection detector class instance
		cd = new ConnectionDetector(getActivity());
		((MainActivity) getActivity()).layoutNotShown();

		((MainActivity) getActivity()).deals
				.setBackgroundResource(R.drawable.deals);
		((MainActivity) getActivity()).inventory
				.setBackgroundResource(R.drawable.inventory);
		((MainActivity) getActivity()).news
				.setBackgroundResource(R.drawable.news);
		((MainActivity) getActivity()).tools
				.setBackgroundResource(R.drawable.tools);
		((MainActivity) getActivity()).info
				.setBackgroundResource(R.drawable.pressedinformation);
		((MainActivity) getActivity()).service
				.setBackgroundResource(R.drawable.services);

		save.setOnClickListener(this);
		return informationview;
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button_save) {
			if (name.getText().toString().isEmpty()
					|| email.getText().toString().isEmpty()
					|| phono.getText().toString().isEmpty()
					|| address.getText().toString().isEmpty()
					|| zipcode.getText().toString().isEmpty()
					|| password.getText().toString().isEmpty()) {
				Toast.makeText(getActivity(), "Insert all the queries first",
						Toast.LENGTH_LONG).show();
			} else if (!emailValidator(email.getText().toString())) {
				Toast.makeText(getActivity(), "Email is not valid",
						Toast.LENGTH_LONG).show();
			} else {

				Username = name.getText().toString();
				Useremail = email.getText().toString();
				Useraddress = address.getText().toString();
				Userpassword = password.getText().toString();
				Userzipcode = zipcode.getText().toString();
				try {

					Userphono = Long.parseLong(phono.getText().toString());

				} catch (Exception e) {
					e.printStackTrace();
				}
				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();

				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					// make HTTP requests
					new Task().execute();
					emptyTextFields();
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					((MainActivity)getActivity()).showAlertDialog(getActivity(), "No Internet Connection",
							"You don't have internet connection.", false);
				}

			}
		}
	}


	public boolean emailValidator(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	public class Task extends AsyncTask<Void, Void, String> {

		ProgressDialog mProgressDialog;

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(Constants.BASEURL
						+ "/api/signup");
				ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
				BasicNameValuePair pasrd = new BasicNameValuePair("appid",
						Constants.APPID);
				param.add(pasrd);
				BasicNameValuePair pasrdd = new BasicNameValuePair(
						"server_key", Constants.SERVER_KEY);
				param.add(pasrdd);
				BasicNameValuePair pasrd1 = new BasicNameValuePair("email",
						Useremail);
				param.add(pasrd1);
				BasicNameValuePair pasrd2 = new BasicNameValuePair("password",
						Userpassword);
				param.add(pasrd2);
				BasicNameValuePair pasrd3 = new BasicNameValuePair("phone",
						Userphono + "");
				param.add(pasrd3);
				BasicNameValuePair pasrd4 = new BasicNameValuePair("address",
						Useraddress);
				param.add(pasrd4);
				BasicNameValuePair pasrd5 = new BasicNameValuePair("zipcode",
						Userzipcode);
				param.add(pasrd5);
				BasicNameValuePair pasrd6 = new BasicNameValuePair("name",
						Username);
				param.add(pasrd6);

				BasicNameValuePair deviceType = new BasicNameValuePair("deviceType",
						"android");
				param.add(deviceType);


				BasicNameValuePair token = new BasicNameValuePair("token",
						PreferencesManager.getPreferenceByKey(
								getActivity(), PreferencesManager.KEYREGID));
				param.add(token);

				BasicNameValuePair deviceId = new BasicNameValuePair("deviceId",
						PreferencesManager.getPreferenceByKey(
								getActivity(), "DEVICEID"));
				param.add(deviceId);

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
											((MainActivity) getActivity())
													.openNewFragment(new Signin_fragment());
										}
									});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();

				} else if (rescode == 0) {
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
											emptyTextFields();

										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				} else if (rescode == 2) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					// set dialog message
					alertDialogBuilder
							.setMessage("User Inactive")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											dialog.cancel();

										}
									});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
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

	public void emptyTextFields() {
		email.setText("");
		name.setText("");
		phono.setText("");
		address.setText("");
		zipcode.setText("");
		password.setText("");

	}
}
