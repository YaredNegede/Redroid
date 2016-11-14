package com.zare.redroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Shows extends Activity {
	private int dayIndex = 0;
	private String day="Monday";
	MyDB db = new MyDB(this);
	Cursor c = null;
	Intent intent = new Intent();
	private Context context = this;
	private String[] days = { "Monday", "Teusday", "Wednesday", "Thursday",
			"Friday", "Saturday", "Sunday" };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shows);
		final TextView tv = (TextView) findViewById(R.id.tvDay);
		this.dayIndex=setDayIndex(tv.getText().toString());		
		this.db.open();
		Bind();
		this.db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_hall_show, menu);		
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {		
		case R.menu.Next:
			new Transverse().execute("1");
			return true;
		case R.menu.Prev:
			new Transverse().execute("0");
			return true;
		}
		return true;
	}

	@Override
	protected void onRestart() {
		// testing method
		super.onRestart();
		this.db.open();
		Bind();
		this.db.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.db.open();
		Bind();
		this.db.close();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.db.open();
		Bind();
		this.db.close();
	}

	public void onBackPressed() {
		db.close();
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		super.onStop();
		db.close();
	}

	public void Bind() {
		// here the intent data is used to filter specific hall data
		String hall = getIntent().getStringExtra("hall");
		day = getIntent().getStringExtra("day");
		TextView tvDay = (TextView) findViewById(R.id.tvDay);
		tvDay.setText(day);
		ListView lv = (ListView) findViewById(R.id.shows);
		String[] from = { Constants.hall_show, Constants.show_time,
				Constants.genre, Constants.about_show, Constants.show_duration };
		int[] to = { R.id.hallShow, R.id.time, R.id.tvGenre, R.id.tvAbout,
				R.id.duration };

		try {
			c = db.getHallShow(hall, day);
			Log.d("Shows", "has got the schedules!" + c.getCount());
		} catch (Exception e) {
			Log.d("col count", "" + c.getColumnCount());
		}
		lv.setAdapter(new SimpleCursorAdapter(this, R.layout.hall_schedule, c,
				from, to));

	}
	class Update extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String st = "";
			try {
				Log.d("do in background", "...calling to start update service.");
				startService(new Intent(context, UpdateService.class));
				st = params[0];
				Log.d("do in background", "started update service.");
			} catch (Exception ex) {
				Log.d("home", "Updater Service starting failed!");
				st = "failed to update from internate";
			}

			return st;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				Log.d("post execute", "starting ...");
				Toast.makeText(context, "completing update", Toast.LENGTH_SHORT)
						.show();
				Log.d("post execute", "completed ...");
			} catch (Exception ex) {
				Log.d("home", "refreshing");
			}
		}
	}

	private int setDayIndex(String d) {
		int ret=0;
		for(int j=0;j<=6;j++){
			if(d.equals(days[j])){ret= j=j+1;}
		}
		return ret;
	}

    class Transverse extends AsyncTask<String, String, String>{
	@Override
	protected String doInBackground(String... params) {	
		String st="";	
		final TextView tv = (TextView) findViewById(R.id.tvDay);
		try {		
// write some code to traverse the days
			switch (Integer.parseInt(params[0])) {
			case 0:
				db.open();
				if(dayIndex==0)dayIndex=6;
				else
					dayIndex--;
					day=days[dayIndex];
					tv.setText(day);
					String hall = getIntent().getStringExtra("hall");
					c=db.getHallShow(hall, day);
					c.requery();
					Log.d("shows prev","updated cursor "+days[dayIndex]+"  "+hall);
					db.close();
				break;
			case 1:
				db.open();
		        if(dayIndex==6)dayIndex=0;
		        else
		        	dayIndex++;
			        day=days[dayIndex];
			        tv.setText(day);
			        String _hall = getIntent().getStringExtra("hall");
			        c=db.getHallShow(_hall, day);
			        c.requery();
			        Log.d("shows next","updated cursor "+days[dayIndex]+" "+_hall);
			        db.close();
				break;
			}
			st=params[0];
			Log.d("do in background", "wait...");
		} catch (Exception ex) {			
			Log.d("shows", "Traversing failed!");
			st="failed to traverse";
		}	
		
		return st;	
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			Log.d("post execute", "starting ...");			
			Toast.makeText(context, "wait", Toast.LENGTH_SHORT).show();
			Log.d("post execute", "completed ...");
		} catch (Exception ex) {
			Log.d("home", "Transverse error...");
		}		
	}
 }
}
