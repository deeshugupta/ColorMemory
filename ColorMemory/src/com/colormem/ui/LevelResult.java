package com.colormem.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

public class LevelResult extends Activity {

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_result);
		
//		ImageView correct = (ImageView) findViewById(R.id.Correct);
//		ImageView wrong = (ImageView) findViewById(R.id.Wrong);
		TextView correct = (TextView) findViewById(R.id.Correct);
		ShapeDrawable correctDrawable = new ShapeDrawable(new OvalShape());
//		correctDrawable.getPaint().setColor(Color.TRANSPARENT);
//		correct.setBackground(correctDrawable);
		TextView wrong = (TextView) findViewById(R.id.Wrong);
		ShapeDrawable wrongDrawable = new ShapeDrawable(new OvalShape());
//		wrongDrawable.getPaint().setColor(Color.TRANSPARENT);
//		wrong.setBackground(wrongDrawable);
		
		Button continueButton = (Button) findViewById(R.id.Continue);
		TextView result = (TextView) findViewById(R.id.Result);
		
		Intent intent = getIntent();
		final int level = intent.getExtras().getInt("level");
		String Result = intent.getExtras().getString("Result");
		
		if("Failed".equals(Result)){
			result.setText("Level "+level+" - Failed");
			continueButton.setText("START AGAIN");
			continueButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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
			continueButton.setText("CONTINUE");
			continueButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_result, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private int getTextColor(int color){
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		double y = (299 * red + 587 * green + 114 * blue) / 1000;
		return y >= 128 ? Color.BLACK : Color.WHITE;
	}
}
