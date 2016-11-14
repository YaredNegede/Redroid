package com.zare.redroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {
	MyDB recDb = new MyDB(this);
	String responseBody = null;
	String showsResponce = null;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Updater service","service created");
	}
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("Updater service","service bound");
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			refreshHome();
			Log.d("Updater service","service started home");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("updater service","home activity contents");
		}
		try {
			Log.d("Updater service","service starting for show");
			refreshHallShows();
			Log.d("Updater service","completed starting service for show");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("updater service","cannot fetch show of the day");
		}
		
		return super.onStartCommand(intent, flags, startId);	
	}	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	public void refreshHome() throws JSONException{
		new Thread() {
			public void run() {				
				 responseBody = new NetwokCatch().getHome();
				 try {
					 JSONArray jArray=null;
						if (responseBody != null && responseBody.length() > 0) {	
							jArray=new JSONArray(responseBody);
							recDb.open();
							for(int i=0;i<jArray.length();i++){
								JSONObject ob=  jArray.getJSONObject(i);
								recDb.insertHall(
										ob.getString("hall"),
										ob.getString("show1"),
										ob.getString("show2"), 
										ob.getString("show3"),						
										ob.getString("time1"),
										ob.getString("time2"),
										ob.getString("time3")
										);				
								//Log.d("service get method token..."+i, 	ob.getString("hall"));
							}
							recDb.close();
						}	
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}.start();
			
	}
	public void refreshHallShows() throws JSONException{
		new Thread() {
		public void run() {				
			showsResponce = new NetwokCatch().getHallShow();
			 try {
				 JSONArray jArray=null;
					if (showsResponce != null && showsResponce.length() > 0) {
						//Log.d("updater service shows", showsResponce);
						jArray=new JSONArray(showsResponce);
						recDb.open();
						HallShow hallshow = new HallShow();
						for(int i=0;i<jArray.length();i++){
							JSONObject ob=  jArray.getJSONObject(i);							
							Log.d("service",ob.getString("day"));
							hallshow.setAbout(ob.getString("about"));
							hallshow.setDay(ob.getString("day"));
							hallshow.setDuration(ob.getString("duration"));
							hallshow.setGenre(ob.getString("genre"));
							hallshow.setHall(ob.getString("hall"));
							hallshow.setImage(ob.getString("image"));
							hallshow.setShow(ob.getString("show"));
							hallshow.setTime(ob.getString("time"));
							recDb.insertHallShow(hallshow);
						//	Log.d("service get method token..."+i, 	ob.getString("hall"));
						}
						recDb.close();
					}	
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}.start();
		}
}
