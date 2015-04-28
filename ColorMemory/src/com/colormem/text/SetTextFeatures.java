package com.colormem.text;

import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SetTextFeatures {

	public SetTextFeatures() {
		// TODO Auto-generated constructor stub
	}
	public static void setFeatures(Button view, Typeface face, String text){
		
		view.setTypeface(face);
		view.setText(text);
		view.setGravity(Gravity.CENTER);
	}
	public static void setFeatures(TextView view, Typeface face, String text, float size){
		
		view.setTypeface(face);
		view.setText(text);
		view.setGravity(Gravity.CENTER);
		view.setTextSize(size);
	}
	
	public static void setFeatures(RadioButton view, Typeface face, String text, float size){
		
		view.setTypeface(face);
		view.setText(text);
		view.setGravity(Gravity.CENTER);
		view.setTextSize(size);
	}
	
	public static void setFeatures(EditText view, Typeface face, String text, float size){
		
		view.setTypeface(face);
		view.setText(text);
		view.setGravity(Gravity.CENTER);
		view.setTextSize(size);
	}
}
