package com.colormem.ui;

import com.colormem.text.SetTextFeatures;

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
		
		Typeface typeFace = Typeface.createFromAsset(getAssets(),
	            "fonts/KaushanScript-Regular.otf");
		
		TextView heading = (TextView) findViewById(R.id.heading);
		SetTextFeatures.setFeatures(heading, typeFace, "COLOR MEMORY", 40f);
		
		
		Button startGame = (Button) findViewById(R.id.gamestarter);
		SetTextFeatures.setFeatures(startGame, typeFace, "START");
		startGame.setOnClickListener(new Onclick());
		
		Button settingsGame = (Button) findViewById(R.id.gamesettings);
		SetTextFeatures.setFeatures(settingsGame, typeFace, "SETTINGS");
		
		
		Button exitGame = (Button) findViewById(R.id.gameExit);
		SetTextFeatures.setFeatures(exitGame, typeFace, "EXIT");
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
