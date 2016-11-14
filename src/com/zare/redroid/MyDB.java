package com.zare.redroid;

import android.content.ContentValues;
import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteException; 
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class MyDB
{ 
	private SQLiteDatabase db=null; 
	private final Context context; 
	private final MyDBhelper dbhelper;
	 
	
	public MyDB(Context c){
		context = c;
		dbhelper = new MyDBhelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	} 
	
	public void close()
{
	db.close(); 
	db=null;
} 
	public void open()
	{
		if(db!=null){
		try {
			db = dbhelper.getWritableDatabase();			
			Log.d("opening", "Succesfully!");
		} catch(SQLiteException ex)
		{
			Log.d("Open database exception caught", ex.getMessage()); 
			db = dbhelper.getReadableDatabase();
			Log.d("writable db", "opeened!");
		} 
		}
	} 

	public long insertHall(String name,String show1,String show2,String show3,String time1, String time2,String time3){
		try{
			ContentValues val = new ContentValues(); 
			val.put(Constants.H_hallName, name); 
			val.put(Constants.S_TIME_1, time1); 
			val.put(Constants.S_TIME_2,time2); 
			val.put(Constants.S_TIME_3, time3); 
			val.put(Constants.H_SHOW1, show1); 
			val.put(Constants.H_SHOW2,show2); 
			val.put(Constants.H_SHOW3,show3); 
			return db.insert(Constants.H_TABLE_NAME, null, val);
		  }catch(SQLiteException ex){
				Log.v("Insert into database exception caught",ex.getMessage());
				return -1; 
		}
	} 
	public Cursor getHall(){
	    	db=dbhelper.getReadableDatabase();
			Cursor c =  db.query(Constants.H_TABLE_NAME, null, null,null, null, null, null);
			return c; 
	}
  	
	public boolean insertHallShow(HallShow hallshow){
		boolean st=false;
		try{
			ContentValues val = new ContentValues(); 			
			val.put(Constants.hall_show, hallshow.getShow());
			val.put(Constants.show_day, hallshow.getDay());
			val.put(Constants.show_duration, hallshow.getDuration());
			val.put(Constants.genre, hallshow.getGenre());
			val.put(Constants.hall, hallshow.getHall());
			val.put(Constants.show_time, hallshow.getTime());
			val.put(Constants.about_show, hallshow.getAbout());
			db.insert(Constants.hall_show, null, val);
			 Log.d("MyDB====>","Hall show insertion succesfully inserted");
			st=true;
		  }catch(SQLiteException ex){
		   Log.d("MyDB","Hall show insertion failed");
		}	
		return st;
	}
	public Cursor getHallShow(String hall,String day){
    	String col[]={hall,day};
    	db=dbhelper.getReadableDatabase();
    	Cursor cr=db.query(Constants.hall_show,null,Constants.hall+"=? and "+Constants.show_day+" =?",col,null,null,null);
    	return cr;
    }
	
	public void insertHall(byte[] Image){
	    String sql = "INSERT INTO "+Constants.H_TABLE_NAME+ "("+Constants.H_Logo+") VALUES(?)";
	    SQLiteStatement insertStmt = db.compileStatement(sql);
	    insertStmt.clearBindings();	    
	    insertStmt.bindBlob(1, Image);
	    insertStmt.executeInsert();
	   
	}
	public byte[] getHallimages() {
		 byte[] accImage = null ;
	    String sql =   "SELECT * FROM "+Constants.H_TABLE_NAME;
	    Cursor cursor = db.rawQuery(sql, new String[] {});
	    if(cursor.moveToFirst()){	       
	    	accImage= cursor.getBlob(2);
	    }
	    if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }
	    if(cursor.getCount() == 0){
	        return null;
	    } else {
	        return accImage;
	    }
	}
}