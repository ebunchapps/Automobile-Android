package com.awrtechnologies.carbudgetsales.twitter;

import java.net.URL;

import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthAccessToken;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthConsumer;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthProvider;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthUtil;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Main com.awrtechnologies.carbudgetsales.twitter android lib interface.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.ent>
 *
 */
public class Twitter {
	private Context mContext;
	
	private SharedPreferences mSharedPref;
	private Editor mEditor;
	
	private OauthConsumer mConsumer;
	private OauthAccessToken mAccessToken;
	private OauthProvider mProvider;
	
	private SigninListener mListener;
	
	private RequestTokenTask mRequestTokenTask;
	private AccesTokenTask mAccesTokenTask;

	private boolean mIsLoading = false;

	private static final String SHARED_PREF = "Android_Twitter_Preferences";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String ACCESS_TOKEN_SECRET = "access_token_secret";
	
	public Twitter(Context context, String clientId, String clientSecret, String callbackUrl, boolean signinOnly) {
		init(context, clientId, clientSecret, callbackUrl, signinOnly);
	}
	
	public Twitter(Context context, String clientId, String clientSecret, String callbackUrl) {
		init(context, clientId, clientSecret, callbackUrl, false);
	}
	
	public OauthConsumer getConsumer() {
		return mConsumer;
	}
	
	public OauthAccessToken getAccessToken() {
		return mAccessToken;
	}
	
	public boolean sessionActive() {
		return (mAccessToken == null) ? false : true;
	}
	
	public void clearSession() {
		mEditor.putString(ACCESS_TOKEN, null);
		mEditor.putString(ACCESS_TOKEN_SECRET, null);
		
		mEditor.commit();
	}
	
	public void signin(SigninListener listener) {
		mListener = listener;
		
		(mRequestTokenTask = new RequestTokenTask()).execute();
	}
	
	public void cancel() {
		if (mIsLoading) {
			if (mAccesTokenTask != null) {
				mAccesTokenTask.cancel(true);
			}
			
			if (mRequestTokenTask == null) {
				mRequestTokenTask.cancel(true);
			}
		}
	}
	
	private void init(Context context, String clientId, String clientSecret, String callbackUrl, boolean signinOnly) {
		String url	= (signinOnly) ? OauthUtil.OAUTH_AUTHENTICATION_URL : OauthUtil.OAUTH_AUTHORIZATION_URL;
		
		mContext	= context;
		
		mConsumer 	= new OauthConsumer(clientId, clientSecret, callbackUrl);
		mProvider	= new OauthProvider(mConsumer, OauthUtil.OAUTH_REQUEST_URL, url, OauthUtil.OAUTH_ACCESSTOKEN_URL);
		
		mSharedPref	= context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);		
		mEditor		= mSharedPref.edit();
		
		loadAccessToken();
	}
	
	private void loadAccessToken() {
		String token 		= mSharedPref.getString(ACCESS_TOKEN, null);
		String tokenSecret 	= mSharedPref.getString(ACCESS_TOKEN_SECRET, null);
		
		if (token != null && tokenSecret != null) {
			mAccessToken = new OauthAccessToken(token, tokenSecret);
		}
	}
	
	private void storeAccessToken(String token, String tokenSecret) {
		mEditor.putString(ACCESS_TOKEN, token);
		mEditor.putString(ACCESS_TOKEN_SECRET, tokenSecret);
		
		mEditor.commit();
	}
	
	private void openAuthorizationDialog(final String url) {
		TwitterDialog dialog = new TwitterDialog(mContext, 
				mConsumer.getCallbackUrl(),
				url, new TwitterDialog.TwDialogListener() {
					
					@Override
					public void onError(String value) {
						mListener.onError("Authorization failed");
					}
					
					@Override
					public void onComplete(String verifier) {
						(mAccesTokenTask = new AccesTokenTask(verifier)).execute(); 
					}
				});
		
		dialog.show();
	}
	
	public class RequestTokenTask extends AsyncTask<URL, Integer, Long> {
		ProgressDialog progressDlg;
		
		String authUrl ="";
		
		public RequestTokenTask() {			
			progressDlg = new ProgressDialog(mContext);
			
			progressDlg.setMessage("Signing in Twitter...");
			progressDlg.setCancelable(true);
		}
		
		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			
			mIsLoading = false;
		}
		
    	@Override
		protected void onPreExecute() {
    		progressDlg.show();
    		
    		mIsLoading = true;
    	}
    
        @Override
		protected Long doInBackground(URL... urls) {         
            long result = 0;
            
            try {
            	authUrl = mProvider.getAuthorizationUrl();				
            } catch (Exception e) { 
            	e.printStackTrace();
            }
            
            return result;
        }

        @Override
		protected void onProgressUpdate(Integer... progress) {              	
        }

        @Override
		protected void onPostExecute(Long result) {
        	mIsLoading = false;
        	
        	progressDlg.dismiss();
        	
        	if (!authUrl.equals("")) {
        		openAuthorizationDialog(authUrl);
        	} else {
        		mListener.onError("Failed to get request token");
        	}
        }                
    }
	
	public class AccesTokenTask extends AsyncTask<URL, Integer, Long> {
		ProgressDialog progressDlg;
				
		String verifier="";
		
		OauthAccessToken accessToken;
		
		public AccesTokenTask(String verifier) {
			this.verifier	= verifier;
			
			progressDlg 	= new ProgressDialog(mContext);
			
			progressDlg.setMessage("Signing in Twitter...");
			progressDlg.setCancelable(true);
		}
		
		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			
			mIsLoading = false;
		}
		
    	@Override
		protected void onPreExecute() {
    		progressDlg.show();
    		
    		mIsLoading = true;
    	}
    
        @Override
		protected Long doInBackground(URL... urls) {         
            long result = 0;
            
            try {
            	accessToken = mProvider.retreiveAccessToken(verifier);            	
            } catch (Exception e) { 
            	e.printStackTrace();
            }
            
            return result;
        }

        @Override
		protected void onProgressUpdate(Integer... progress) {              	
        }

        @Override
		protected void onPostExecute(Long result) {
        	mIsLoading = false;
        	
        	progressDlg.dismiss();
        	
        	if (accessToken != null) {
        		mAccessToken = accessToken;
        		
        		storeAccessToken(accessToken.getToken(), accessToken.getSecret());
        		
        		mListener.onSuccess(accessToken, mProvider.getUserId(), mProvider.getScreenName());
        	} else {
        		mListener.onError("Failed to get access token");
        	}
        }                
    }
	
	public interface SigninListener {
		public abstract void onSuccess(OauthAccessToken accessToken, String userId, String screenName);
		public abstract void onError(String error);
	}
}