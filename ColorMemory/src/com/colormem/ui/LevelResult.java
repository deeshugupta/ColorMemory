package com.colormem.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.colormem.database.UserDAO;
import com.colormem.text.SetTextFeatures;

public class LevelResult extends Activity {

	private int backPressed = 0;
	
	//Intent Values
	String userName;
	Long level;
	Long cheats;
	
	//Database 
	UserDAO userDAO;
	
	//Time values
	Long totalTime;
	Long remainingTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_result);
		
		
		//Getting Intents
		Intent intent = getIntent();
		String Result = intent.getExtras().getString("Result");
		userName = intent.getExtras().getString("UserName");
		level = intent.getLongExtra("Level", 0l);
		cheats = intent.getLongExtra("Cheats", 0l);
		totalTime = intent.getLongExtra("TotalTime", 30l);
		remainingTime = intent.getLongExtra("RemaingTime", 0l);
		
		//Saving values to Database
		userDAO = new UserDAO(LevelResult.this);
		userDAO.open();
		
		//Update values for user
		if(remainingTime>=totalTime*0.5)
			{
			cheats++;
			Toast.makeText(LevelResult.this, "Cheats Increased", Toast.LENGTH_SHORT).show();
			}
		userDAO.updateCheats(userName, cheats);
		
		
		
		//Setting TypeFace
		Typeface startFace = Typeface.createFromAsset(getAssets(),
	            "fonts/KaushanScript-Regular.otf");
		TextView username = (TextView) findViewById(R.id.result_user);
		SetTextFeatures.setFeatures(username, startFace, "USER : "+userName, 15f);
		
		TextView correct = (TextView) findViewById(R.id.Correct);
		correct.setTypeface(startFace);
		ShapeDrawable correctDrawable = new ShapeDrawable(new OvalShape());
		TextView wrong = (TextView) findViewById(R.id.Wrong);
		wrong.setTypeface(startFace);
		ShapeDrawable wrongDrawable = new ShapeDrawable(new OvalShape());
		
		Button continueButton = (Button) findViewById(R.id.Continue);
		TextView result = (TextView) findViewById(R.id.Result);
		SetTextFeatures.setFeatures(result, startFace, "", 40f);
		
		
		
		if("Failed".equals(Result)){
			result.setText("Level "+level+" - Failed");
			result.setTextColor(Color.parseColor("#CC0000"));
			SetTextFeatures.setFeatures(continueButton, startFace, "START AGAIN");
			
			continueButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					userDAO.close();
					startActivity(new Intent(LevelResult.this, Launch.class));
					
				}
			});
			
			//Setting Correct color
			int correctColor = intent.getExtras().getInt("CorrectColor");
			correctDrawable.getPaint().setColor(correctColor);
			correct.setBackground(correctDrawable);
			correct.setText("CORRECT");
			correct.setGravity(Gravity.CENTER);
			correct.setTextColor(getTextColor(correctColor));
			
			//Setting wrong color
			int wrongColor = intent.getExtras().getInt("WrongColor");
			wrongDrawable.getPaint().setColor(wrongColor);
			wrong.setBackground(wrongDrawable);
			wrong.setText("YOUR");
			wrong.setGravity(Gravity.CENTER);
			wrong.setTextColor(getTextColor(wrongColor));
			
		}
		else if("Passed".equalsIgnoreCase(Result)){
			userDAO.updateLevel(userName, level);
			result.setText("Level "+level+" - Passed");
			result.setTextColor(Color.parseColor("#006B24"));
			SetTextFeatures.setFeatures(continueButton, startFace, "CONTINUE");
			
			continueButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					userDAO.close();
					Intent intent = new Intent(LevelResult.this, MainActivity.class);
					intent.putExtra("Level", level+1);
					intent.putExtra("UserName", userName);
					intent.putExtra("Cheats", cheats);
					startActivity(intent);
					
				}
			});
		}
		else if("Time Elapsed".equalsIgnoreCase(Result)){
			result.setText("Level "+level+" - Failed");
			result.setTextColor(Color.parseColor("#CC0000"));
			SetTextFeatures.setFeatures(continueButton, startFace, "START AGAIN");
			
			continueButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					userDAO.close();
					startActivity(new Intent(LevelResult.this, Launch.class));
					
				}
			});
			
			correct.setVisibility(View.GONE);
			SetTextFeatures.setFeatures(wrong, startFace, "TIME ELAPSED", 20f);
		}
	}

	
	
	private int getTextColor(int color){
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		double y = (299 * red + 587 * green + 114 * blue) / 1000;
		return y >= 128 ? Color.BLACK : Color.WHITE;
	}
	
	@Override
	public void onBackPressed(){
		if(backPressed !=1){
			Toast.makeText(this, "Press back again to go to main menu.", Toast.LENGTH_SHORT).show();
			backPressed++;
		}
		else{
			Intent intent = new Intent(getApplicationContext(), Launch.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			startActivity(intent);
		}
	}
}
