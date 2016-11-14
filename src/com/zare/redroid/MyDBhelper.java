package com.zare.redroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBhelper extends SQLiteOpenHelper {
	private static final String CREATE_TABLE = "create table "
			+ Constants.H_TABLE_NAME + "("
			+ Constants.H_KEY_ID + " integer primary key autoincrement, " 
			+ Constants.H_hallName	+ " text not null, "
			+ Constants.S_TIME_1	+ " text not null, "
			+ Constants.S_TIME_2 + " text not null, "
			+ Constants.S_TIME_3 + " text not null,"			
			+ Constants.H_SHOW1	+ " text not null, "
			+ Constants.H_SHOW2 + " text not null, "
			+ Constants.H_SHOW3 + " text not null, " 
			+ Constants.H_Logo	+ " Blob);";
	private static final String CREATE_HALL_SHOW = "create table "
			+ Constants.hall_show + "("
			+ Constants.H_KEY_ID + " integer primary key autoincrement, " 
			+ Constants.about_show + " text not null, " 
			+ Constants.hall_show	+ " text not null, "
			+ Constants.show_day	+ " text not null, "
			+ Constants.genre	+ " text not null, "
			+ Constants.hall	+ " text not null, "
			+ Constants.show_duration	+ " text not null, "
			+ Constants.show_time + " text not null, " 
			+ Constants.Logo	+ " Blob) ;";
	public MyDBhelper(Context context, String name, CursorFactory factory,	int version) {
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("Running SQL Script for hall for home...","Started");
		try {
			db.execSQL(CREATE_TABLE);
			Log.d("Running SQL Script...","create table Successful!!");			
		} catch (SQLiteException ex) {
			Log.v("Create table exception", ex.getMessage());
			Log.d("Running SQL Script failed!",CREATE_TABLE);
		}
	  	
		try {
			db.execSQL(CREATE_HALL_SHOW);
			Log.d("Running SQL Script for hall shows...","Successful!!");			
		} catch (SQLiteException ex3) {
			Log.v("Create table exception", ex3.getMessage());
			Log.d("Running SQL Script for hall shows failed!",CREATE_HALL_SHOW);
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("drop table if exists " + Constants.H_TABLE_NAME);
		onCreate(db);
	}
}