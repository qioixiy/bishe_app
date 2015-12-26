package com.qioixiy.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBMisc extends SQLiteOpenHelper {
	private static final String DBNAME = "misc.db";
	private static final int VERSION = 1;

	public DBMisc(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS updateTable (id integer primary key autoincrement, filename varchar(100), size varchar(100), time varchar(100), version varchar(100), date varchar(100))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS updateTable");
		onCreate(db);
	}
}