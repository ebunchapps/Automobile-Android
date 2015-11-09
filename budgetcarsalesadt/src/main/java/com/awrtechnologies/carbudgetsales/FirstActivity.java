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
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.helper.GeneralHelper;
import com.awrtechnologies.carbudgetsales.helper.PreferencesManager;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class FirstActivity extends Activity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener {

	TextView signup;

	String userid;
	RelativeLayout fb, google, twitter;

	private GoogleApiClient mGoogleApiClient;

	private boolean mIntentInProgress;

	private ConnectionResult mConnectionResult;

	private boolean mSignInClicked;

	private ProgressDialog mProgressDialog;

	private String name;

	private String email;

	private String address;

	private String gplusid;
	private static final int FBLOGIN = 1003;
	private static final int RC_SIGN_IN = 1004;
	private static final int TWITTERLOGIN = 1005;

	@Override
	public void onNewIntent(Intent intent) {
		/**
		 * 
		 * 
		 * notificationIntent.putExtra("FROMNOTIFICATION", true);
		 */
		try {
			if (intent.getExtras().getBoolean("FROMNOTIFICATION", false)) {
				intent.getExtras().remove("FROMNOTIFICATION");
				String time = intent.getExtras().getString("TIME");

				PreferencesManager.setPreferenceByKey(FirstActivity.this,
						"SERVICETIME", time);
				GeneralHelper.getInstance(FirstActivity.this).setIscheck(true);
				Intent i = new Intent(FirstActivity.this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			} else if (intent.getExtras().getBoolean("FROMNOTIFICATIONTIME",
					false)) {
				intent.getExtras().remove("FROMNOTIFICATIONTIME");
				String timedone = intent.getExtras().getString("TIME");
				PreferencesManager.setPreferenceByKey(FirstActivity.this,
						"SERVICETIMEDONE", timedone);
				GeneralHelper.getInstance(FirstActivity.this)
						.setIscheckdonetime(true);
				Intent i = new Intent(FirstActivity.this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			} else {
				GeneralHelper.getInstance(FirstActivity.this).setIscheck(false);
				GeneralHelper.getInstance(FirstActivity.this)
				.setIscheckdonetime(false);
				GeneralHelper.getInstance(FirstActivity.this)
						.setIscheckdonetime(false);
				User user = User.getUser();
				if (user != null) {
					finish();
					Intent i = new Intent(FirstActivity.this,
							MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);

				}
			}

		} catch (Exception e) {

			User user = User.getUser();
			if (user != null) {
				finish();
				Intent i = new Intent(FirstActivity.this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);

			}

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);

		String device_id = Secure.getString(
				FirstActivity.this.getContentResolver(), Secure.ANDROID_ID);
		PreferencesManager.setPreferenceByKey(FirstActivity.this, "DEVICEID",
				device_id);

		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
		} else {
			PreferencesManager.setPreferenceIntByKey(this,
					PreferencesManager.KEYGCMSTATUS,
					GCMIntentService.KEYREGISTERED);
			PreferencesManager.setPreferenceByKey(this,
					PreferencesManager.KEYREGID, regId);
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		fb = (RelativeLayout) findViewById(R.id.relativelayoutfb);
		google = (RelativeLayout) findViewById(R.id.relativelayoutgoogle);
		twitter = (RelativeLayout) findViewById(R.id.relativetwitter);
		signup = (TextView) findViewById(R.id.textsignin);

		signup.setOnClickListener(this);
		fb.setOnClickListener(this);
		twitter.setOnClickListener(this);
		userid = PreferencesManager.getPreferenceByKey(FirstActivity.this,
				"Userid");
		fb.setOnClickListener(this);
		twitter.setOnClickListener(this);
		google.setOnClickListener(this);

//		onNewIntent(getIntent());
		 User user = User.getUser();
		 if (user != null) {
		 finish();
		 Intent i = new Intent(FirstActivity.this, MainActivity.class);
		 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
		 | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(i);

		 }
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.textsignin) {
			Intent i = new Intent(FirstActivity.this, MainActivity.class);

			startActivity(i);

		}
		if (id == R.id.relativelayoutfb) {
			fbfunc();
		}
		if (id == R.id.relativetwitter) {

			Intent i = new Intent(FirstActivity.this, TwitterActivity.class);
			startActivityForResult(i, TWITTERLOGIN);
		}
		if (id == R.id.relativelayoutgoogle) {
			if (!mGoogleApiClient.isConnecting()) {
				mSignInClicked = true;
				showProgress();
				resolveSignInError();
			}
		}

	}

	private void fbfunc() {

		sendFBRequest();
	}

	private void sendFBRequest() {
		startActivityForResult(new Intent(this, FacebookLogin.class), FBLOGIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
			return;
		}

		if (requestCode == FBLOGIN || requestCode == TWITTERLOGIN) {
			if (resultCode == Activity.RESULT_OK) {
				User user = User.getUser();
				if (user != null) {
					finish();
					Intent i = new Intent(FirstActivity.this,
							MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);

				} else {
					Toast.makeText(FirstActivity.this, "Unable to Login!",
							Toast.LENGTH_SHORT).show();

				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Session openActiveSession(FirstActivity firstActivity, boolean b,
			StatusCallback fbStatusCallback, ArrayList<String> arrayList) {

		OpenRequest openRequest = new OpenRequest(firstActivity)
				.setPermissions(arrayList).setCallback(fbStatusCallback);

		Session session = new Session.Builder(firstActivity).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || b) {
			Session.setActiveSession(session);
			session.openForRead(openRequest);

			return session;
		}
		return null;
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * G+ call backs
	 */

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;

		// Get user's information
		getProfileInformation();

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		dissmissProgressBar();
		mGoogleApiClient.connect();

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		dissmissProgressBar();
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	/**
	 * G+ call backs END
	 */
	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	/**
	 * Sign-out from google
	 * */
	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();

		}
	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				name = currentPerson.getDisplayName();
				email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				address = "";// currentPerson.getPlacesLived().get(0).getValue();
				try {
					address = currentPerson.getPlacesLived().get(0).getValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dissmissProgressBar();
				gplusid = currentPerson.getId();
				new SignupTask().execute();

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				param.add(new BasicNameValuePair("name", name));
				param.add(new BasicNameValuePair("email", email));
				param.add(new BasicNameValuePair("address", address));
				param.add(new BasicNameValuePair("phone", ""));
				param.add(new BasicNameValuePair("zipcode", ""));
				param.add(new BasicNameValuePair("socialid", gplusid));
				param.add(new BasicNameValuePair("type", "google"));
				param.add(new BasicNameValuePair("deviceType", "android"));
				param.add(new BasicNameValuePair("token", PreferencesManager.getPreferenceByKey(
						FirstActivity.this, PreferencesManager.KEYREGID)));
				param.add(new BasicNameValuePair("deviceId", PreferencesManager.getPreferenceByKey(
						FirstActivity.this, "DEVICEID")));

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
			signOutFromGplus();
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
						finish();
						Intent i = new Intent(FirstActivity.this,
								MainActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					} catch (JSONException e) {

						try {
							Toast.makeText(FirstActivity.this,
									jobject.getString("message"),
									Toast.LENGTH_SHORT).show();
						} catch (JSONException e2) {
							Toast.makeText(FirstActivity.this,
									"Undefined Error!", Toast.LENGTH_SHORT)
									.show();
						}
						e.printStackTrace();
					}

				} else {

					try {
						Toast.makeText(FirstActivity.this,
								jobject.getString("message"),
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						Toast.makeText(FirstActivity.this, "Undefined Error!",
								Toast.LENGTH_SHORT).show();
					}

				}
			} catch (JSONException e) {
				String message ="Internet Error!";
				try {
					 message = jobject.getString("message");
				}catch (Exception em)
				{

				}
				try {
					Toast.makeText(FirstActivity.this,
							message, Toast.LENGTH_SHORT)
							.show();
				} catch (Exception e1) {
					Toast.makeText(FirstActivity.this, "Undefined Error!",
							Toast.LENGTH_SHORT).show();
				}
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}

	void showProgress() {
		dissmissProgressBar();
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
