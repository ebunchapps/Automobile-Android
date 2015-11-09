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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.awrtechnologies.carbudgetsales.data.User;
import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.awrtechnologies.carbudgetsales.twitter.Twitter;
import com.awrtechnologies.carbudgetsales.twitter.TwitterRequest;
import com.awrtechnologies.carbudgetsales.twitter.TwitterUser;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthAccessToken;

public class TwitterActivity extends Activity {
	private Twitter mTwitter;
	Button mBtnTwitter;
	protected String name;
	protected String twitterid;
	private static final String CONSUMER_KEY = "OVENFuZW2cUjsecmHbqvfKosE";
	private static final String CONSUMER_SECRET = "IDpawidJsRmv8afCslEMI9vssHWE1E3sJPBqpfbXhZ1M3wFOQY";
	public static final String CALLBACK_URL = "http://awrtechnologies.com/clients/paul/automobile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		mTwitter = new Twitter(this, CONSUMER_KEY, CONSUMER_SECRET,
				CALLBACK_URL);

		if (mTwitter.sessionActive()) {
			mTwitter.clearSession();
		}
		signinTwitter();

	}

	private void signinTwitter() {
		mTwitter.signin(new Twitter.SigninListener() {
			@Override
			public void onSuccess(OauthAccessToken accessToken, String userId,
					String screenName) {
				// success

				getCredentials();
			}

			@Override
			public void onError(String error) {
				// error
			}
		});
	}

	private void getCredentials() {
		final ProgressDialog progressDlg = new ProgressDialog(this);

		progressDlg.setMessage("Getting credentials...");
		progressDlg.setCancelable(false);

		progressDlg.show();

		TwitterRequest request = new TwitterRequest(mTwitter.getConsumer(),
				mTwitter.getAccessToken());

		request.verifyCredentials(new TwitterRequest.VerifyCredentialListener() {

			@Override
			public void onSuccess(TwitterUser user) {
				progressDlg.dismiss();
				name = user.name;
				twitterid = user.userId;

				new SignupTask().execute();
			}

			@Override
			public void onError(String error) {
				progressDlg.dismiss();

			}
		});
	}

	private ProgressDialog mProgressDialog;

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
				param.add(new BasicNameValuePair("email", ""));
				param.add(new BasicNameValuePair("address", ""));
				param.add(new BasicNameValuePair("phone", ""));
				param.add(new BasicNameValuePair("zipcode", ""));
				param.add(new BasicNameValuePair("socialid", twitterid));
				param.add(new BasicNameValuePair("type", "com/awrtechnologies/carbudgetsales/twitter"));

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
							Toast.makeText(TwitterActivity.this,
									jobject.getString("message"),
									Toast.LENGTH_SHORT).show();
						} catch (JSONException e2) {
							Toast.makeText(TwitterActivity.this,
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
						Toast.makeText(TwitterActivity.this,
								jobject.getString("message"),
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						Toast.makeText(TwitterActivity.this,
								"Undefined Error!", Toast.LENGTH_SHORT).show();
					}

				}
			} catch (JSONException e) {
				finish();
				setResult(Activity.RESULT_CANCELED);
				try {
					Toast.makeText(TwitterActivity.this,
							jobject.getString("message"), Toast.LENGTH_SHORT)
							.show();
				} catch (JSONException e1) {
					Toast.makeText(TwitterActivity.this, "Undefined Error!",
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