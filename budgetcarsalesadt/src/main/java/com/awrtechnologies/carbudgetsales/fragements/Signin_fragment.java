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
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.MainActivity;
import com.awrtechnologies.carbudgetsales.R;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.ConnectionDetector;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;

public class Signin_fragment extends Fragment implements OnClickListener {

	EditText email, password;
	Button signin;
	String emailaddress, passwrd;
	TextView forgetPassword;
	Button signUp;

	
	// flag for Internet connection status
		Boolean isInternetPresent = false;
		
		// Connection detector class
		ConnectionDetector cd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View signinview = inflater.inflate(R.layout.signin, null);
		forgetPassword = (TextView) signinview
				.findViewById(R.id.textview_forgotpassword);
		email = (EditText) signinview.findViewById(R.id.edittext_email);
		password = (EditText) signinview.findViewById(R.id.edittext_password);
		signin = (Button) signinview.findViewById(R.id.button_signin);
		signUp = (Button) signinview.findViewById(R.id.button_signup);
		// creating connection detector class instance
				cd = new ConnectionDetector(getActivity());
		signin.setOnClickListener(this);
		signUp.setOnClickListener(this);
		forgetPassword.setOnClickListener(this);
		((MainActivity) getActivity()).layoutNotShown();
		return signinview;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button_signup) {
			((MainActivity) getActivity())
					.openNewFragment(new SignUp_fragment());
			
		}
		if (id == R.id.button_signin) {
			emailaddress = email.getText().toString();
			passwrd = password.getText().toString();
			if (emailaddress.isEmpty() || passwrd.isEmpty()) {
				Toast.makeText(getActivity(), "Insert Username and password",
						Toast.LENGTH_LONG).show();
			} else if (!emailValidator(emailaddress)) {
				Toast.makeText(getActivity(), "Email is not valid",
						Toast.LENGTH_LONG).show();

			} else {
				
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
		} else if (id == R.id.textview_forgotpassword) {
			emailaddress = email.getText().toString();
			if(emailaddress.isEmpty())
			{
				Toast.makeText(getActivity(), "Please Insert Email Address",
						Toast.LENGTH_LONG).show();
			}
			else {
				new ForgotPassword().execute();
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
		password.setText("");
	}

	public class Task extends AsyncTask<Void, Void, String> {

		ProgressDialog mProgressDialog;

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(Constants.BASEURL
						+ "/api/login");
				ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
				BasicNameValuePair pasrd = new BasicNameValuePair("appid",
						Constants.APPID);
				param.add(pasrd);
				BasicNameValuePair pasrdd = new BasicNameValuePair(
						"server_key", Constants.SERVER_KEY);
				param.add(pasrdd);
				BasicNameValuePair pasrd1 = new BasicNameValuePair("email",
						emailaddress);
				param.add(pasrd1);
				BasicNameValuePair pasrd2 = new BasicNameValuePair("password",
						passwrd);
				param.add(pasrd2);
				param.add(new BasicNameValuePair("deviceType", "android"));
				param.add(new BasicNameValuePair("token", PreferencesManager.getPreferenceByKey(
						getActivity(), PreferencesManager.KEYREGID)));
				param.add(new BasicNameValuePair("deviceId", PreferencesManager.getPreferenceByKey(
						getActivity(), "DEVICEID")));

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

					try {

						PreferenceManager
								.getDefaultSharedPreferences(getActivity())
								.edit().putBoolean("loggedin", true).commit();

						JSONObject responceobj = obj.getJSONObject("response");

						JSONObject userJsn = responceobj.getJSONObject("user");

						String userid = getString(userJsn, "id");
						String name = getString(userJsn, "name");
						String email = getString(userJsn, "email");
						String phone = getString(userJsn, "phone");
						String zipcode = getString(userJsn, "zipcode");
						String address = getString(userJsn, "address");
						String date = getString(userJsn, "createdate");
						String hashid=getString(userJsn,"hashId");
						new Delete().from(User.class).execute();
						User d = new User();

						d.setAddress(address);
						d.setDate(date);
						d.setEmail(email);
						d.setUserid(userid);
						d.setName(name);
						d.setPhone(phone);
						d.setZipcode(zipcode);
						d.setHashid(hashid);
						d.save();
						((MainActivity) getActivity()).loadData();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (rescode == 0) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());
					alertDialogBuilder
							.setMessage("Login credentials are invalid")
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

				} else if (rescode == 2) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());
					alertDialogBuilder
							.setMessage("User Inactive")
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
			
			super.onPostExecute(result);
		}
	}

	public class ForgotPassword extends AsyncTask<Void, Void, String> {

		ProgressDialog mProgressDialog;

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(Constants.BASEURL
						+ "/api/forget_password");
				ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
				BasicNameValuePair pasrd = new BasicNameValuePair("appid",
						Constants.APPID);
				param.add(pasrd);

				BasicNameValuePair pasrd1 = new BasicNameValuePair("email",
						emailaddress);
				param.add(pasrd1);
				BasicNameValuePair pasrd2 = new BasicNameValuePair("server_key",
						Constants.SERVER_KEY);
				param.add(pasrd2);

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
							.setMessage(
									"A reset password link has been sent to your mail id.")
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
				if(rescode==0)
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					alertDialogBuilder
							.setMessage(
									obj.getString("message"))
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
			super.onPostExecute(result);
		}
	}

	public String getString(JSONObject obj, String key) {
		try {
			return obj.getString(key);
		} catch (Exception e) {
			return "";
		}
	}


}
