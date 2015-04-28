package com.colormem.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ColorDatabaseHelper extends SQLiteOpenHelper {
	
	public final static String COLUMN_ID="ID";
	public final static String COLUMN_NAME="Name";
	public final static String COLUMN_LEVEL="Level";
	public final static String COLUMN_CHEATS="Cheats";
	public final static String COLUMN_ACTIVE="IsActive";
	public final static String TABLE_USER="User";
	public final static String DATABASE_NAME="ColorMemory";
	public final static int VERSION = 1;
	
	//Database creation Query string
	private final String CREATE_TABLE="create table "
		      + TABLE_USER + "(" 
		      		+ COLUMN_ID + " integer primary key autoincrement, " 
		      		+ COLUMN_NAME+ " text not null, " 
		      		+ COLUMN_LEVEL+ " integer not null, "
		      		+ COLUMN_CHEATS+ " integer not null, "
		      		+ COLUMN_ACTIVE+ " integer default 1 "
		      		+		");";
	

	public ColorDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}
