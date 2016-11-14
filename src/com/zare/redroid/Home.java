package com.zare.redroid;

import java.util.Calendar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Home extends Activity {
	private Intent intent = new Intent();
	private Context context=this;
	private MyDB recDb;
	Cursor curHall = null;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		recDb = new MyDB(this);
		recDb.open();	
		Bind();
		recDb.close();
	}

	@Override
	public void onBackPressed() {
		recDb.close();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		recDb.close();
	}

	@Override
	protected void onStop() {
		super.onStop();
		recDb.close();
		//Debug.stopMethodTracing();
	}

	private static final Cursor cursor = null;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Debug.startMethodTracing("trace");
		setContentView(R.layout.activity_home);
		recDb = new MyDB(this);
		recDb.open();	
		Bind();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		recDb = new MyDB(this);
		recDb.open();
		Bind();
	}

	@Override
	protected void onResume() {
		super.onResume();
		recDb = new MyDB(this);
		recDb.open();
		Bind();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.update:
			new Update().execute("update");
			return true;
		case R.id.refresh:			
			curHall.requery();
			return true;
		}	
		return true;
	}

	// bind method
	@SuppressWarnings("deprecation")
	public void Bind() {		
		intent.setClass(this, Shows.class);
		final ListView lv = (ListView) findViewById(R.id.shows);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cc=null;
				cc= (Cursor) lv.getItemAtPosition(position);
				intent.putExtra("hall", cc.getString(1));
				intent.putExtra("day",getDayOfWeek());
				Log.d("home list pos", cc.getString(1));
				startActivity(intent);
				Log.d("Home", "called startactivity from item clicked listener");
			}
		});
		getContent(lv);
	}

	private void getContent(ListView lv) {
		@SuppressWarnings("static-access")
		String[] from = { Constants.H_hallName, Constants.S_TIME_1,
				Constants.S_TIME_2, Constants.S_TIME_3, Constants.H_SHOW1,
				Constants.H_SHOW2, Constants.H_SHOW3 };
		int[] to = { R.id.hall_name, R.id.time_1, R.id.time_2, R.id.time_3,
				R.id.show_1, R.id.show_2, R.id.show_3 };			
		try {
			this.curHall = this.recDb.getHall();
		} catch (Exception ex) {
			Log.d("hall cursor", "unable to get cursor");
		}
		lv.setAdapter(new SimpleCursorAdapter(this, R.layout.row_layout,curHall, from, to));
		//setListAdapter(new MyAdapter(context, R.layout.row_layout, curHall, from, to));
		
	}

	public String getDayOfWeek(){		
		switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			return	"Monday";			
		case Calendar.TUESDAY:
			return "Teusday";			
		case Calendar.WEDNESDAY:
			return "Wednesday";			
		case Calendar.THURSDAY:
			return "Thursday";			
		case Calendar.FRIDAY:
			return "Friday";
		case Calendar.SATURDAY:
			return "Saturday";			
		case Calendar.SUNDAY:
			return "Sunday";			
		default:
			return "invalid";
		}
	}
    class Update extends AsyncTask<String, String, String>{
	@Override
	protected String doInBackground(String... params) {	
		String st="";		
		try {
			Log.d("do in background", "...calling to start update service.");
			startService(new Intent(context, UpdateService.class));
			st=params[0];
			Log.d("do in background", "started update service.");
		} catch (Exception ex) {			
			Log.d("home", "Updater Service starting failed!");
			st="failed to update from internate";
		}	
		
		return st;	
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			Log.d("post execute", "starting ...");			
			Toast.makeText(context, "completing update", Toast.LENGTH_SHORT).show();
			Log.d("post execute", "completed ...");
		} catch (Exception ex) {
			Log.d("home", "refreshing");
		}		
	}
 }
 
}
