package com.colormem.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {
	
	private ColorDatabaseHelper colorDatabaseHelper;
	private SQLiteDatabase sqLiteDatabase;
	private String[] columns = {
			ColorDatabaseHelper.COLUMN_ID,
			ColorDatabaseHelper.COLUMN_NAME,
			ColorDatabaseHelper.COLUMN_LEVEL,
			ColorDatabaseHelper.COLUMN_CHEATS
	};
	
	public UserDAO(Context context) {
		colorDatabaseHelper = new ColorDatabaseHelper(context);
	}
	
	public void open() throws SQLException {
	    sqLiteDatabase = colorDatabaseHelper.getWritableDatabase();
	  }

	public void close() {
	    colorDatabaseHelper.close();
	  }
	  
	public User createorgetUser(String name){
		
		Cursor search =  sqLiteDatabase.query(ColorDatabaseHelper.TABLE_USER, columns
				, ColorDatabaseHelper.COLUMN_NAME+"=?", new String[]{name.trim()}, null, null, null);
		User activeUser = getActiveUser();
		if(search.getCount()!=0){
			search.moveToFirst();
			User existingUser = cursorToUser(search);
			if(activeUser !=null && !existingUser.getName().equalsIgnoreCase(activeUser.getName()))
			{
				updateActiveUser(activeUser.getName(), 0);
				updateActiveUser(existingUser.getName(),1);
			}
				
			
			search.close();
			return existingUser;
		}
		
		if(activeUser!=null)
		updateActiveUser(activeUser.getName(), 0);
		
		ContentValues values = new ContentValues();
		values.put(ColorDatabaseHelper.COLUMN_NAME, name);
		values.put(ColorDatabaseHelper.COLUMN_LEVEL, 0);
		values.put(ColorDatabaseHelper.COLUMN_CHEATS, 20);
		
		Long id = sqLiteDatabase.insert(ColorDatabaseHelper.TABLE_USER, null, values);
		Cursor cursor = sqLiteDatabase.query(ColorDatabaseHelper.TABLE_USER,
				columns, ColorDatabaseHelper.COLUMN_ID+ "="+id, null, null, null, null);
		cursor.moveToFirst();
		User newUser = cursorToUser(cursor);
		cursor.close();
		return newUser;
	}
	
	public void updateLevel(String user, Long level){
		ContentValues values = new ContentValues();
		values.put(ColorDatabaseHelper.COLUMN_LEVEL, level);
		sqLiteDatabase.update(ColorDatabaseHelper.TABLE_USER, 
				values, ColorDatabaseHelper.COLUMN_NAME+"=?", new String[]{user.trim()});
	}
	
	public void updateCheats(String user, Long cheats){
		ContentValues values = new ContentValues();
		values.put(ColorDatabaseHelper.COLUMN_CHEATS, cheats);
		sqLiteDatabase.update(ColorDatabaseHelper.TABLE_USER, 
				values, ColorDatabaseHelper.COLUMN_NAME+"=?", new String[]{user.trim()});
	}
	
	public void updateActiveUser(String name, int value){
		ContentValues values = new ContentValues();
		values.put(ColorDatabaseHelper.COLUMN_ACTIVE, value);
		sqLiteDatabase.update(ColorDatabaseHelper.TABLE_USER, 
				values, ColorDatabaseHelper.COLUMN_NAME+"=?", new String[]{name.trim()});
	}
	
	public User getActiveUser(){
		Cursor cursor = sqLiteDatabase.query(ColorDatabaseHelper.TABLE_USER, columns,
				ColorDatabaseHelper.COLUMN_ACTIVE+"=1", null, null, null, null);
		if(cursor.getCount()!=0)
		{
			cursor.moveToFirst();
			User activeUser = cursorToUser(cursor);
			cursor.close();
			return activeUser;
		}
		return null;
	}
	
	public List<String> getAllUserNames(){
		List<String> users = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query(ColorDatabaseHelper.TABLE_USER, 
				new String[]{ColorDatabaseHelper.COLUMN_NAME},
				null, null, null, null, null);
		if(cursor.getCount()!=0)
		{
			cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			users.add(cursor.getString(0));
			cursor.moveToNext();
		}
		}
		return users;
	}
	
	public List<User> getAllUsers(){
		List<User> users = new ArrayList<User>();
		Cursor cursor = sqLiteDatabase.query(ColorDatabaseHelper.TABLE_USER, 
				columns,
				null, null, null, null, null);
		if(cursor.getCount()!=0)
		{
			cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			users.add(cursorToUser(cursor));
			cursor.moveToNext();
		}
		}
		return users;
	}
	
	public void deleteUser(String name){
		sqLiteDatabase.delete(ColorDatabaseHelper.TABLE_USER, ColorDatabaseHelper.COLUMN_NAME+"=?",
				new String[]{name.trim()});
	}

	private User cursorToUser(Cursor cursor) {
		User user = new User(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3));
		return user;
	}

	public boolean editUserName(String username, String newUsername) {
		
		Cursor search =  sqLiteDatabase.query(ColorDatabaseHelper.TABLE_USER, columns
				, ColorDatabaseHelper.COLUMN_NAME+"=?", new String[]{newUsername.trim()}, null, null, null);
		
		if(search.getCount()==0)
		{
			ContentValues values = new ContentValues();
			values.put(ColorDatabaseHelper.COLUMN_NAME, newUsername);
			sqLiteDatabase.update(ColorDatabaseHelper.TABLE_USER, 
			values, ColorDatabaseHelper.COLUMN_NAME+"=?", new String[]{username.trim()});
			return true;
		}
		
		return false;
	}
}
