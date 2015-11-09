package com.awrtechnologies.carbudgetsales;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class FacebookLogin extends Activity {
	private ProgressDialog mDialog;

	protected String userEmail;
	protected String userId;
	protected String userNameLastName;

	public static final ArrayList<String> FACEBOOK_PERMISSIONS = new ArrayList<String>() {
		/**
		 * UID for serializable class
		 */
		private static final long serialVersionUID = -2400817127912127646L;
		{
			add("public_profile");
			add("email");
		}
	};

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

		}
	};

	private ProgressDialog mProgressDialog;

	protected String address;

	protected String phone;

	protected String zipcode;

	protected String type;

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		try {
			InputMethodManager input = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			input.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			InputMethodManager input = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			input.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
		}
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_facebook_login);
		initiateFacebookLogin();
	}

	public void initiateFacebookLogin() {
		SessionStatusCallback statusCallback = new SessionStatusCallback();
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			try {
				session.openForRead(new Session.OpenRequest(this)
						.setPermissions(FACEBOOK_PERMISSIONS).setCallback(
								statusCallback));
			} catch (Exception e) {
				e.printStackTrace();
				session.closeAndClearTokenInformation();
				Toast.makeText(
						this,
						"Unexcepted error has been encountered! Please try again",
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Session.openActiveSession(this, true, statusCallback);
		}

	}

	void getMeRequest(final Session session) {
		for (String s : session.getPermissions()) {
			System.out.println(s);
		}

		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					// callback after Graph API response
					// with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {

							userEmail = user.asMap().get("email").toString();
							userNameLastName = user.getFirstName() + " "
									+ user.getLastName();
							address = "";
							phone = "";
							zipcode = "";
							userId = user.getId();
							type = "facebook";
							System.out.println("CALLING FACEBOOK GET FRIENDS");
							callThirdPartyLoginApi();
						} else {
							System.out.println("USER NOT FOUND");
						}
					}

				});
		request.executeAsync();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("ON ACTIVITY RESULT");
		if (resultCode == Activity.RESULT_CANCELED) {
			setResult(Activity.RESULT_CANCELED);
			finish();
		} else {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session.isOpened()) {
				System.out
						.println("Access Token : " + session.getAccessToken());
				getMeRequest(session);
			}
		}
	}

	protected void callThirdPartyLoginApi() {
		new SignupTask().execute();
	}

	class SignupTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			showProgress();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String url = Constants.BASEURL + "/api/social_signup";
			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(url);
				ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
				BasicNameValuePair pasrd = new BasicNameValuePair("appid",
						Constants.APPID);
				param.add(pasrd);
				BasicNameValuePair pasrdd = new BasicNameValuePair(
						"server_key", Constants.SERVER_KEY);
				param.add(pasrdd);
				// name,email,address,phone,zipcode,appid,socialid,type

				param.add(new BasicNameValuePair("name", userNameLastName));
				param.add(new BasicNameValuePair("email", userEmail));
				param.add(new BasicNameValuePair("address", address));
				param.add(new BasicNameValuePair("phone", phone));
				param.add(new BasicNameValuePair("zipcode", zipcode));
				param.add(new BasicNameValuePair("socialid", userId));
				param.add(new BasicNameValuePair("type", type));
				param.add(new BasicNameValuePair("deviceType", "android"));
				param.add(new BasicNameValuePair("token", PreferencesManager.getPreferenceByKey(
						FacebookLogin.this, PreferencesManager.KEYREGID)));
				param.add(new BasicNameValuePair("deviceId", PreferencesManager.getPreferenceByKey(
						FacebookLogin.this, "DEVICEID")));

				httppost.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse httpresponse = httpclient.execute(httppost);

				InputStream inputstream = httpresponse.getEntity().getContent();

				String result = convertStreamToString(inputstream);

				System.out.println(result);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
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

		@Override
		protected void onPostExecute(String result) {

			/**
			 * { "response_code": 1, "message": "", "response": { "user": {
			 * "id": "50", "name": "NItesh", "email": "n@p.com", "phone":
			 * "we3r", "address": "assd", "zipcode": "88384343", "createdate":
			 * "2014-11-28 00:30:05" } } }
			 */

			dissmissProgressBar();
			JSONObject jobject = null;
			try {
				jobject = new JSONObject(result);
				if (jobject.getInt("response_code") == 1) {
					try {
						JSONObject userJsonObj = jobject.getJSONObject(
								"response").getJSONObject("user");
						new Delete().from(User.class).execute();
						User user = new User();
						user.setHashid(userJsonObj.getString("hashId"));
						user.setAddress(userJsonObj.getString("address"));
						user.setDate(userJsonObj.getString("createdate"));
						user.setEmail(userJsonObj.getString("email"));
						user.setName(userJsonObj.getString("name"));
						user.setPhone(userJsonObj.getString("phone"));
						user.setUserid(userJsonObj.getString("id"));
						user.setZipcode(userJsonObj.getString("zipcode"));
						user.save();
					} catch (JSONException e) {
						finish();
						setResult(Activity.RESULT_CANCELED);
						try {
							Toast.makeText(FacebookLogin.this,
									jobject.getString("message"),
									Toast.LENGTH_SHORT).show();
						} catch (JSONException e2) {
							Toast.makeText(FacebookLogin.this,
									"Undefined Error!", Toast.LENGTH_SHORT)
									.show();
						}
						e.printStackTrace();
					}
					setResult(Activity.RESULT_OK);
					finish();
				} else {
					finish();
					setResult(Activity.RESULT_CANCELED);
					try {
						Toast.makeText(FacebookLogin.this,
								jobject.getString("message"),
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						Toast.makeText(FacebookLogin.this, "Undefined Error!",
								Toast.LENGTH_SHORT).show();
					}

				}
			} catch (JSONException e) {
				finish();
				setResult(Activity.RESULT_CANCELED);
				try {
					Toast.makeText(FacebookLogin.this,
							jobject.getString("message"), Toast.LENGTH_SHORT)
							.show();
				} catch (JSONException e1) {
					Toast.makeText(FacebookLogin.this, "Undefined Error!",
							Toast.LENGTH_SHORT).show();
				}
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}

	void showProgress() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Loading..");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	void dissmissProgressBar() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

}
