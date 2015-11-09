package com.awrtechnologies.carbudgetsales.twitter.http;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthAccessToken;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthConsumer;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthHeader;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthSignature;
import com.awrtechnologies.carbudgetsales.twitter.oauth.OauthUtil;
import com.awrtechnologies.carbudgetsales.twitter.util.Debug;
import com.awrtechnologies.carbudgetsales.twitter.util.StringUtil;

/**
 * Http connection with Twiter API.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.ent>
 *
 */
public class HttpConn {
	private OauthConsumer mOauthConsumer;
	private OauthAccessToken mAcessToken;
	
	public static final String REQUEST_POST = "POST";
	public static final String REQUEST_GET = "GET";
	
	public HttpConn(OauthConsumer consumer, OauthAccessToken acessToken) {
		mOauthConsumer	= consumer;
		mAcessToken		= acessToken;
	}
	
	public String connectPost(String requestUri, List<NameValuePair> params) throws Exception {
		String response 		= "";
		
		InputStream stream = null;
		
		try {
			HttpParams httpParams 	= new HttpParams();
			
			String nonce			= OauthUtil.createNonce();
			String timestamp		= OauthUtil.getTimeStamp();
			
			httpParams.put("oauth_consumer_key", 		new HttpValues(mOauthConsumer.getConsumerKey()));
			httpParams.put("oauth_nonce", 				new HttpValues(nonce));
			httpParams.put("oauth_signature_method", 	new HttpValues(OauthUtil.SIGNATURE_METHOD));
			httpParams.put("oauth_timestamp", 			new HttpValues(timestamp));
			httpParams.put("oauth_token", 				new HttpValues(mAcessToken.getToken()));
			httpParams.put("oauth_version", 			new HttpValues(OauthUtil.OAUTH_VERSION));
			
			if (params != null) {
				int size = params.size();
				
				for (int i = 0; i < size; i++) {
					BasicNameValuePair param = (BasicNameValuePair) params.get(i);
					
					httpParams.put(param.getName(), new HttpValues(param.getValue()));
				}
			}
			
			OauthSignature reqSignature	= new OauthSignature();
			
			String sigBase		= reqSignature.createSignatureBase(REQUEST_POST, requestUri, httpParams.getQueryString());
			String signature	= reqSignature.createRequestSignature(sigBase, mOauthConsumer.getConsumerSecret(), mAcessToken.getSecret());
			
			String authHeader	= OauthHeader.buildRequestHeader( 
									mOauthConsumer.getConsumerKey(), 
									nonce,
									signature, 
									OauthUtil.SIGNATURE_METHOD, 
									timestamp,
									mAcessToken.getToken(),
									OauthUtil.OAUTH_VERSION);
					
			Debug.i("Signature base " + sigBase);
			Debug.i("Signature " + signature);
			
			Debug.i("POST " + requestUri);
			Debug.i("Authorization " + authHeader);
			
			java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
			java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);

			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
			System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
			System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
			
			HttpClient httpClient 	= new DefaultHttpClient();
			HttpPost httpPost 		= new HttpPost(requestUri); 
			
			httpPost.addHeader("Authorization", authHeader);			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		        
			HttpResponse httpResponse 	= httpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity 		= httpResponse.getEntity();
					
				if (httpEntity == null) {
					throw new Exception("Return value is empty");
				}
					
				stream  	= httpEntity.getContent();							
				response	= StringUtil.streamToString(stream);
				
				Debug.i("Response " + response);
			} else {
				throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		
		return response;
	}
	
	public String connectGet(String requestUri, List<NameValuePair> params) throws Exception {
		String response 		= "";
		
		InputStream stream = null;
		
		try {
			HttpParams httpParams 	= new HttpParams();
			
			String nonce			= OauthUtil.createNonce();
			String timestamp		= OauthUtil.getTimeStamp();
			
			httpParams.put("oauth_consumer_key", 		new HttpValues(mOauthConsumer.getConsumerKey()));
			httpParams.put("oauth_nonce", 				new HttpValues(nonce));
			httpParams.put("oauth_signature_method", 	new HttpValues(OauthUtil.SIGNATURE_METHOD));
			httpParams.put("oauth_timestamp", 			new HttpValues(timestamp));
			httpParams.put("oauth_token", 				new HttpValues(mAcessToken.getToken()));
			httpParams.put("oauth_version", 			new HttpValues(OauthUtil.OAUTH_VERSION));
			
			String requestUrl = requestUri;
			
			if (params != null) {
				StringBuilder requestParamSb = new StringBuilder();
				int size = params.size();
				
				for (int i = 0; i < size; i++) {
					BasicNameValuePair param = (BasicNameValuePair) params.get(i);
					
					httpParams.put(param.getName(), new HttpValues(param.getValue()));
					
					requestParamSb.append(param.getName() + "=" + param.getValue() + ((i != size-1) ? "&" : ""));
				}
				
				String requestParam	= requestParamSb.toString();
				
				requestUrl = requestUri + ((requestUri.contains("?")) ? "&" + requestParam : "?" + requestParam); 
 			}
			
			OauthSignature reqSignature	= new OauthSignature();
			
			String sigBase		= reqSignature.createSignatureBase(REQUEST_GET, requestUri, httpParams.getQueryString());
			String signature	= reqSignature.createRequestSignature(sigBase, mOauthConsumer.getConsumerSecret(), mAcessToken.getSecret());
			
			String authHeader	= OauthHeader.buildRequestHeader( 
									mOauthConsumer.getConsumerKey(), 
									nonce,
									signature, 
									OauthUtil.SIGNATURE_METHOD, 
									timestamp,
									mAcessToken.getToken(),
									OauthUtil.OAUTH_VERSION);
					
			Debug.i("Signature base " + sigBase);
			Debug.i("Signature " + signature);
			
			Debug.i("GET " + requestUrl);
			Debug.i("Authorization " + authHeader);

			java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
			java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);

			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
			System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
			System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
			
			HttpClient httpClient 	= new DefaultHttpClient();		
			HttpGet httpGet 		= new HttpGet(requestUrl); 

			httpGet.addHeader("Authorization", authHeader);			
			
			HttpResponse httpResponse 	= httpClient.execute(httpGet);		
			HttpEntity httpEntity 		= httpResponse.getEntity();
				
			if (httpEntity == null) {
				throw new Exception("Return value is empty");
			}
					
			stream  	= httpEntity.getContent();							
			response	= StringUtil.streamToString(stream);
				
			Debug.i("Response " + response);
			
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		
		return response;
	}
}