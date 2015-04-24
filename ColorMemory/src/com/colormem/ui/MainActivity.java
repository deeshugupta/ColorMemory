package com.colormem.ui;


import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.colormem.color.ColorPicker;

public class MainActivity extends Activity {
	//Sequence of color which will be represented
	int[] colorSequence;
	
	//Intended touch sequence
	int[] touchSequence;
	
	//Number which is touched at the moment
	int touched =0;
	
	//number for Touch Sequence generation
	int touch=0;
	
	//Level at which we are playing
	int level=0;
	
	Bitmap bitmap;
	BitmapDrawable bitmapDrawable;
	
	//Number of Maximum Touches. Changes with every level
	int maxTouch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//Setting up the grid 
		setupGrid();
		
		// Defining the Test colors
		
		ImageView im1 = (ImageView) findViewById(R.id.imageView1);
		ShapeDrawable animCircle = new ShapeDrawable(new OvalShape());
		im1.setBackground(animCircle);
		//Length of Sequence
		level = getIntent().getIntExtra("Level", 1);
		maxTouch= level;
		touchSequence=new int[maxTouch];
		
		for(int touch=0;touch<maxTouch;){
			Random rand = new Random();
			int seq = rand.nextInt(25);
			if(touch>0 && touchSequence[touch-1]==colorSequence[seq])
			{
				continue;
			}
			touchSequence[touch] = colorSequence[seq];
			touch++;
		}
		
		//Sequence for Animation
		animationSequence(animCircle);
				
	}
	
	private void setupGrid(){
		LinearLayout gameLayout  = (LinearLayout) findViewById(R.id.game);
		
		//Defining sequence of colors
		ColorPicker colorPicker = new ColorPicker(5, 5);
		colorSequence=colorPicker.getColor();
		
		
		//Defining rows
		for(int row=0;row<5;row++)
			{
			View child1 = getLayoutInflater().inflate(R.layout.circle_row, null);
			setView((TableRow)child1,row);
			gameLayout.addView(child1);
			}
	}

	private void setView(TableRow child1, int row) {
		
		ImageView imageView = (ImageView) child1.findViewById(R.id.Circle1);
		setColor(imageView, row*5);
			
		imageView = (ImageView) child1.findViewById(R.id.Circle2);
		setColor(imageView, row*5+1);
				
		imageView = (ImageView) child1.findViewById(R.id.Circle3);
		setColor(imageView, row*5+2);
				
		imageView = (ImageView) child1.findViewById(R.id.Circle4);
		setColor(imageView, row*5+3);
				
		imageView = (ImageView) child1.findViewById(R.id.Circle5);
		setColor(imageView, row*5+4);
		
		
	}

	public void setColor(ImageView imageView,int seq){
		
		ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
		shapeDrawable.getPaint().setColor(colorSequence[seq]);
		shapeDrawable.setIntrinsicHeight(imageView.getLayoutParams().height);
		shapeDrawable.setIntrinsicWidth(imageView.getLayoutParams().width);
		Bitmap bMap = drawableToBitmap(shapeDrawable);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bMap);
		imageView.setBackground(bitmapDrawable);
		imageView.setOnTouchListener(new OntouchListen());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up ImageView, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class OntouchListen implements OnTouchListener{	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
			v.performClick();
			ImageView imageView = (ImageView) v;
			bitmap = ((BitmapDrawable)imageView.getBackground()).getBitmap();
		 	int x = (int)event.getX();
	        int y = (int)event.getY();
	        int pixel = bitmap.getPixel(x,y);

	        //then do what you want with the pixel data, e.g
	        int redValue = Color.red(pixel);
	        int blueValue = Color.blue(pixel);
	        int greenValue = Color.green(pixel);    
	        int color = Color.argb(255, redValue, greenValue, blueValue);
	        Intent intent = new Intent(MainActivity.this,LevelResult.class);
	        if(touched<maxTouch && touchSequence[touched]==color){
	        	touched++;
	        }
	        else if(touched<maxTouch && touchSequence[touched]!=color){
	        	Toast.makeText(MainActivity.this, "Wrong Sequence", Toast.LENGTH_SHORT).show();
	        	intent.putExtra("Result", "Failed");
	        	intent.putExtra("level", level);
	        	intent.putExtra("CorrectColor", touchSequence[touched]);
	        	intent.putExtra("WrongColor", color);
	        	startActivity(intent);
	        }
	        
	        if(touched == maxTouch){
	        	Toast.makeText(MainActivity.this, "Correctly done", Toast.LENGTH_SHORT).show();
	        	intent.putExtra("Result", "Passed");
	        	intent.putExtra("level", level);
	        	startActivity(intent);
	        }
	        return false;
	}
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	private void animationSequence(ShapeDrawable circle){
		
		final ShapeDrawable animCircle = circle;
		Animator[] items = new Animator[maxTouch];
		for(touch=0;touch<maxTouch;touch++)
		{
						
			ObjectAnimator objAnimator = ObjectAnimator.
					ofObject(animCircle, "Color", new ArgbEvaluator(),touchSequence[touch],Color.TRANSPARENT);
			objAnimator.setDuration(2000);
			objAnimator.setInterpolator(new TimeInterpolator() {
				
				@Override
				public float getInterpolation(float input) {
					if(input<0.7)
						return 0;
					return 1.0f;
				}
			});
			objAnimator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int color = (int) animation.getAnimatedValue();
					animCircle.getPaint().setColor(color);
					animCircle.invalidateSelf();
				}
			});
			items[touch] = objAnimator;
			
		}
		AnimatorSet animSet = new AnimatorSet();
		animSet.playSequentially(items);
		animSet.start();
	}
}
