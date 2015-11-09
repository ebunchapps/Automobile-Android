package com.awrtechnologies.carbudgetsales;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

import com.awrtechnologies.carbudgetsales.helper.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GooglePlusSharing extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	private static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "com.awrtechnologies.carbudgetsales.MainActivity";

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	private boolean mIntentInProgress;

	private boolean mSignInClicked;
	private ProgressDialog mDialog;

	private ConnectionResult mConnectionResult;

	String personName;
	String pEmail;
	String type;
	String userId;
	String url = Constants.SHARINGURL;
	public GooglePlusSharing() {
     
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.googleplus);

		mDialog = new ProgressDialog(GooglePlusSharing.this);
		mDialog.setMessage("Loading..");
		mDialog.setCancelable(false);
		mDialog.show();
		
		int dealsId = getIntent().getExtras().getInt("dealId");
		System.out.println("dealsidddddd" +dealsId);
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setPackage("com.google.android.apps.plus");

		intent.setType("text/plain");
		intent.putExtra(
				android.content.Intent.EXTRA_TEXT,
				url + dealsId);
		startActivity(Intent.createChooser(intent, "Share Via"));

	}

	@Override
	protected void onStart() {
		System.out.println("onStart");
		super.onStart();
		mGoogleApiClient.connect();
		finish();
	}
	
	
	
	@Override
	protected void onStop() {

		super.onStop();
		
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}
	

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

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				resolveSignInError();
			} else {
				signInWithGplus();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();

			}
		}
	}

	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
			
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;

		getProfileInformation();

	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);

				personName = currentPerson.getDisplayName();

				pEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
				type = "googleplus";
				userId = currentPerson.getId();

				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();

				Log.e(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + pEmail
						+ ", Image: " + personPhotoUrl);

				System.out.println("name" + personName);
				System.out.println("Uid" + userId);


			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
