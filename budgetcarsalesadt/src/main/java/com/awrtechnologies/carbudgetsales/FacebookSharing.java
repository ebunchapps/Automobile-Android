package com.awrtechnologies.carbudgetsales;

import java.util.ArrayList;

import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class FacebookSharing extends Activity {

	private ProgressDialog mDialog;
	protected String userEmail;
	protected String userId;
	protected String userNameLastName;

	protected String address;

	protected String phone;

	protected String zipcode;
	private String userLastName;

	protected String type;

	String url = Constants.SHARINGURL;

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

	@Override
	public void onResume() {
		finish();
		System.out.println("com.awrtechnologies.carbudgetsales.FacebookSharing onResume");
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
		System.out.println("com.awrtechnologies.carbudgetsales.FacebookSharing onPause");
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
		
		System.out.println("com.awrtechnologies.carbudgetsales.FacebookSharing onDestroy");
		super.onDestroy();
		uiHelper.onDestroy();
		

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	public FacebookSharing() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.println("com.awrtechnologies.carbudgetsales.FacebookSharing onCreate");
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.googleplus);

		mDialog = new ProgressDialog(FacebookSharing.this);
		mDialog.setMessage("Loading..");
		mDialog.setCancelable(false);
		mDialog.show();
		int dealsId = getIntent().getExtras().getInt("dealId");
		initiateFacebookLogin();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setPackage("com.facebook.katana");
		intent.setType("text/plain");
		System.out.println("dealsIdddddd" +dealsId);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, url + dealsId);
		startActivity(Intent.createChooser(intent, "Share Via"));

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

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							userEmail = user.asMap().get("email").toString();

							userNameLastName = user.getFirstName();
							userLastName = user.getLastName();
							address = "";
							phone = "";
							zipcode = "";
							userId = user.getId();
							type = "facebook";
							System.out.println("CALLING FACEBOOK GET FRIENDS");
							callThirdPartyLoginApi();
							Log.e("FacebookActivity.this", "userEmail"
									+ userEmail);
							System.out.println("useremail "
									+ user.asMap().get("email").toString());    
							System.out.println("userNameLastName "
									+ user.getFirstName() + " "
									+ user.getLastName());
							System.out.println("userId " + user.getId());
						} else {
							System.out.println("USER NOT FOUND");
						}
					}

					
				});
		request.executeAsync();
	}

	protected void callThirdPartyLoginApi() {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("onactivityresultbefore");
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onactivityresultafter");
		System.out.println("ON ACTIVITY RESULT");
		if (resultCode == Activity.RESULT_CANCELED) {
			setResult(Activity.RESULT_CANCELED);
			finish();

			System.out.println("onactivityresultif");
		} else {
			uiHelper.onActivityResult(requestCode, resultCode, data);
			System.out.println("onactivityresultelse");
			finish();
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

}
