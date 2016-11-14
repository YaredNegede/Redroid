package com.zare.redroid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class NetwokCatch {
	private static final String IMAGEURL = "http://10.0.2.2:8080/cinema/API.php?images=halls";
	public String getHome() {
		final  String url = "http://10.0.2.2:8080/cinema/API.php?Home=Home";
		HttpGet request = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		HttpClient client = new DefaultHttpClient();
		String responseBody = null;
		try {			
				Log.d("Netwok catch for home", "Starting request for home");
				responseBody =client.execute(request, responseHandler);	
				Log.d("Netwok catch for home", "completing request for home");
			
		} catch (ClientProtocolException e) {
			Log.d("Network Fetch", "failed to  fetch home contents");
		} catch (IOException e) {
			Log.d("Netwok get failed", "can not execute request");
		}			
		return responseBody;
	}
	public String getHallShow() {
		final  String url = "http://10.0.2.2:8080/cinema/API.php?Shows=Shows";
		HttpGet request = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		HttpClient client = new DefaultHttpClient();
		String responseBodyHallShow= null;
		try {			
				Log.d("Netwok  catch shows", "Starting request for shows.....");
				responseBodyHallShow =client.execute(request, responseHandler);	
				Log.d("Netwok  catch show", "completing request for shows.....");

			
		} catch (ClientProtocolException e) {
			Log.d("Network catch", "failed fetching hall contents");
		} catch (IOException e) {
			Log.d("Netwok get failed", "can not execute request");
		}		
		return responseBodyHallShow;
	}
	private byte[] getLogoImage(String url){
	     try {
	             URL imageUrl = new URL(url);
	             URLConnection ucon = imageUrl.openConnection();
	             InputStream is = ucon.getInputStream();
	             BufferedInputStream bis = new BufferedInputStream(is);
	             ByteArrayBuffer baf = new ByteArrayBuffer(500);
	             int current = 0;
	             while ((current = bis.read()) != -1) {
	                     baf.append((byte) current);
	             }
	             return baf.toByteArray();
	     } catch (Exception e) {
	             Log.d("ImageManager", "Error: " + e.toString());
	     }
	     return null;
	}	
}
