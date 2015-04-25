package com.colormem.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LevelResult extends Activity {

	private int backPressed = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_result);
		
		Typeface startFace = Typeface.createFromAsset(getAssets(),
	            "fonts/KaushanScript-Regular.otf");
		TextView correct = (TextView) findViewById(R.id.Correct);
		correct.setTypeface(startFace);
		ShapeDrawable correctDrawable = new ShapeDrawable(new OvalShape());
		TextView wrong = (TextView) findViewById(R.id.Wrong);
		wrong.setTypeface(startFace);
		ShapeDrawable wrongDrawable = new ShapeDrawable(new OvalShape());
		
		Button continueButton = (Button) findViewById(R.id.Continue);
		TextView result = (TextView) findViewById(R.id.Result);
		result.setTypeface(startFace);
		result.setTextSize(40f);
		continueButton.setTypeface(startFace);
		Intent intent = getIntent();
		final int level = intent.getExtras().getInt("level");
		String Result = intent.getExtras().getString("Result");
		
		if("Failed".equals(Result)){
			result.setText("Level "+level+" - Failed");
			result.setTextColor(Color.parseColor("#CC0000"));
			continueButton.setText("START AGAIN");
			continueButton.setGravity(Gravity.CENTER);
			continueButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
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
		else{
			result.setText("Level "+level+" - Passed");
			result.setTextColor(Color.parseColor("#006B24"));
			continueButton.setText("CONTINUE");
			continueButton.setGravity(Gravity.CENTER);
			continueButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LevelResult.this, MainActivity.class);
					intent.putExtra("Level", level+1);
					startActivity(intent);
					
				}
			});
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
