package com.colormem.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Launch extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		TextView heading = (TextView) findViewById(R.id.heading);
		Typeface headingFace = Typeface.createFromAsset(getAssets(),
	            "fonts/KaushanScript-Regular.otf");
		heading.setTypeface(headingFace);
		heading.setText("COLOR MEMORY");
		heading.setGravity(Gravity.CENTER);
		heading.setTextSize(40f);
		
		
		Typeface startFace = Typeface.createFromAsset(getAssets(),
	            "fonts/KaushanScript-Regular.otf");
		Button startGame = (Button) findViewById(R.id.gamestarter);
		startGame.setText("START");
		startGame.setGravity(Gravity.CENTER);
		
		startGame.setTypeface(startFace);
	
		startGame.setOnClickListener(new Onclick());
		Button settingsGame = (Button) findViewById(R.id.gamesettings);
		settingsGame.setText("SETTINGS");
		settingsGame.setGravity(Gravity.CENTER);
		settingsGame.setTypeface(startFace);
		
		Button exitGame = (Button) findViewById(R.id.gameExit);
		exitGame.setText("EXIT");
		exitGame.setGravity(Gravity.CENTER);
		exitGame.setTypeface(startFace);
		exitGame.setOnClickListener(new Onclick());
	}

	
	
	@Override
	public void onBackPressed(){
		finish();
	}
	
	private class Onclick implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.gamestarter:
				Intent intent = new Intent(Launch.this,MainActivity.class);
				intent.putExtra("Level", 1);
				startActivity(intent);
				break;
			case R.id.gameExit:
				finish();
				break;
			}
		}
		
	}
}
