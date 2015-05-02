package com.colormem.ui;


import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.colormem.color.ColorHelper;
import com.colormem.color.ColorPicker;
import com.colormem.sound.ClickSound;
import com.colormem.text.SetTextFeatures;

public class MainActivity extends Activity {
	//Sequence of color which will be represented
	int[] colorSequence;
	
	//Intended touch sequence
	int[] touchSequence;
	
	//Number which is touched at the moment
	int touched =0;
	
	//number for Touch Sequence generation
	int touch=0;
	
	
	
	Bitmap bitmap;
	BitmapDrawable bitmapDrawable;
	
	//Number of Maximum Touches. Changes with every level
	int maxTouch;
	
	//Back pressed Number
	int backPressed=0;
	
	//Game Font
	Typeface dialog_butons;
	
	//Cheat Touch iterator
	int cheatTouch =0;
	
	//UserName for which the game is being played
	String userName;
	
	//Level at which we are playing
	Long level=0l;
		
	//Cheats remaining
	Long cheats=0l;	
	
	//Remaining Time
	Long remainingTime=0l;
	
	//Total Time
	Long totalTime=0l;
	
	//Countdown Timer
	CountDownTimer cdt;
	
	//Faults Allowed
	int faults=0;
	int maxfaults=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//GETTING All INTENT DATA
		userName = getIntent().getExtras().getString("UserName");
		level = getIntent().getLongExtra("Level", 1l);
		cheats = getIntent().getLongExtra("Cheats", 1l);
		
		//Defining TypeFace for Text
		dialog_butons = Typeface.createFromAsset(getAssets(),
	            "fonts/KaushanScript-Regular.otf");
		
		/*
		 * Length of Animation sequence. This is a naive logic and
		 * will be changed accordingly.
		 * The comments will be changed accordingly also.
		 */
		
		maxTouch = ((level.intValue()-1)/4)+2;
		totalTime = 31 - ((level-1)%4)*3+((maxTouch-2)/4)*10; 
		touchSequence=new int[maxTouch];
		if(maxTouch%2==0)
			maxfaults=(maxTouch-2)/2;
		else if((maxTouch-1)%2==0)
			maxfaults=(maxTouch-3)/2;
		faults=maxfaults;
		
		final Button cheatButton = (Button) findViewById(R.id.cheat);
		cheatButton.setVisibility(View.INVISIBLE);
		
		final TextView username = (TextView) findViewById(R.id.main_user);
		username.setVisibility(View.INVISIBLE);
		SetTextFeatures.setFeatures(username, dialog_butons, "USER - "+userName, 15f);
		
		final TextView timer = (TextView) findViewById(R.id.timer);
		timer.setVisibility(View.INVISIBLE);
		
		final TextView levelIndicator = (TextView) findViewById(R.id.Level);
		SetTextFeatures.setFeatures(levelIndicator, dialog_butons, "LEVEL - "+level, 20f);
		levelIndicator.setVisibility(View.INVISIBLE);
		
		final TextView faultIndicatior = (TextView) findViewById(R.id.Fault);
		SetTextFeatures.setFeatures(faultIndicatior, dialog_butons, "FAULTS : "+maxfaults, 20f);
		faultIndicatior.setVisibility(View.INVISIBLE);
		
		
		/*
		 * Defining sequence of colors which will be shown in the grid
		 */
		ColorPicker colorPicker = new ColorPicker(5, 5);
		colorSequence=colorPicker.getColor();
		
		
		/*
		 * Picking up random Colors according to the sequence of colors defined above
		 * The sequence then needs to be remembered by the player.
		 */
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
	
// ============================== Animation Dialog Starts ==============================================//		
		/*
		 * Following lines are the code for the Dialog Animation
		 * The animation sequence will be shown in this dialog and two buttons
		 * will be provided for continue and Repeat which are enabled after the animation ends.
		 */
		final Dialog animdialog = new Dialog(MainActivity.this);
		animdialog.setCanceledOnTouchOutside(false);
		animdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View animDialogView = getLayoutInflater().inflate(R.layout.dialog_color_animation, null);
		animdialog.setContentView(animDialogView);
		
		TextView dialog_heading = (TextView)animdialog.findViewById(R.id.dialog_heading);
		SetTextFeatures.setFeatures(dialog_heading, dialog_butons, "SEQUENCE", 40f);
		
		
		final Button continueButton = (Button)animDialogView.findViewById(R.id.dialog_continue);
		SetTextFeatures.setFeatures(continueButton, dialog_butons, "CONTINUE");
		
		
		final Button repeatButton = (Button) animDialogView.findViewById(R.id.dialog_repeat);
		SetTextFeatures.setFeatures(repeatButton, dialog_butons, "REPEAT");
		
		final TextView sequenceNumber = (TextView)animDialogView.findViewById(R.id.sequenceNumber);
		sequenceNumber.setVisibility(View.GONE);
		
		ImageView animatorCircle = (ImageView) animDialogView.findViewById(R.id.animatorCircle);
		
		final ShapeDrawable animcircle = new ShapeDrawable(new OvalShape());
		animatorCircle.setBackground(animcircle);
		animationSequence(animcircle, continueButton, repeatButton);
		
		continueButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animdialog.dismiss();
				//Setting up the grid 
				setupGrid(cheatButton,timer,username,levelIndicator, faultIndicatior);
			}
		});
		
		repeatButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animationSequence(animcircle, continueButton, repeatButton);
			}
		});
		
		animdialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                	animdialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), Launch.class);
        			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			intent.putExtra("EXIT", true);
        			startActivity(intent);
                }
                return true;
            }
        });
		
		
		animdialog.show();
		
// ============================== Animation Dialog Ends ==============================================//			
	}

// =============================== Function for Setting up the GRID of colors ========================//
	private void setupGrid(Button cheatButton, final TextView timer, TextView username,
			TextView levelIndicator, TextView faultIndicatior){
		LinearLayout gameLayout  = (LinearLayout) findViewById(R.id.game);
		
		//Defining rows
		for(int row=0;row<5;row++)
			{
			View child1 = getLayoutInflater().inflate(R.layout.circle_row, null);
			setView((TableRow)child1,row,faultIndicatior);
			gameLayout.addView(child1);
			}
		levelIndicator.setVisibility(View.VISIBLE);
		faultIndicatior.setVisibility(View.VISIBLE);
		username.setVisibility(View.VISIBLE);
		cheatButton.setVisibility(View.VISIBLE);
		cheatButton.setOnClickListener(new OnClickListner(cheatButton));
		if(cheats <= 0)
			cheatButton.setEnabled(false);
		timer.setVisibility(View.VISIBLE);
		SetTextFeatures.setFeatures(timer, dialog_butons, "", 40f);
		SetTextFeatures.setFeatures(cheatButton, dialog_butons, "CHEATS : "+cheats.toString());
		cdt = new CountDownTimer(totalTime.longValue()*1000,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				remainingTime = millisUntilFinished/1000;
				timer.setText(Long.toString(millisUntilFinished/1000));
				
			}
			
			@Override
			public void onFinish() {
				if(maxTouch!=touched){
//					Toast.makeText(MainActivity.this, "Time Elapsed", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this,LevelResult.class);
		        	intent.putExtra("Result", "Time Elapsed");
		        	intent.putExtra("UserName", userName);
					intent.putExtra("Level", level);
					intent.putExtra("Cheats", cheats);
		        	intent.putExtra("TotalTime", totalTime);
		        	startActivity(intent);
				}
				
			}
		}.start();
		
	}
// =============================== Function for Setting up the GRID of colors Ends=====================//

// =============================== Helper Functions to set Row of circles =============================//
	private void setView(TableRow child1, int row, TextView faultIndicatior) {
		
		ImageView imageView = (ImageView) child1.findViewById(R.id.Circle1);
		ColorHelper.setColor(imageView, colorSequence[row*5], getResources(), new OntouchListen(faultIndicatior));
			
		imageView = (ImageView) child1.findViewById(R.id.Circle2);
		ColorHelper.setColor(imageView, colorSequence[row*5+1], getResources(), new OntouchListen(faultIndicatior));
				
		imageView = (ImageView) child1.findViewById(R.id.Circle3);
		ColorHelper.setColor(imageView, colorSequence[row*5+2], getResources(), new OntouchListen(faultIndicatior));
				
		imageView = (ImageView) child1.findViewById(R.id.Circle4);
		ColorHelper.setColor(imageView, colorSequence[row*5+3], getResources(), new OntouchListen(faultIndicatior));
				
		imageView = (ImageView) child1.findViewById(R.id.Circle5);
		ColorHelper.setColor(imageView, colorSequence[row*5+4], getResources(), new OntouchListen(faultIndicatior));
		
		
	}
// =============================== Helper Functions to set Row of circles  Ends=========================//
	
// =============================== Touch Listener of  circles   =========================================//	
	private class OntouchListen implements OnTouchListener{	
		TextView faultIndicator;
	public OntouchListen(TextView faultIndicatior) {
		this.faultIndicator = faultIndicatior;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
			v.performClick();
			ClickSound.clickSound(getAssets());
			backPressed=0;
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
//	        	Toast.makeText(MainActivity.this, "Wrong Sequence", Toast.LENGTH_SHORT).show();
	        	if(faults==0)
	        	{
	        		cdt.cancel();
		        	intent.putExtra("Result", "Failed");
		        	intent.putExtra("UserName", userName);
					intent.putExtra("Level", level);
					intent.putExtra("Cheats", cheats);
		        	intent.putExtra("CorrectColor", touchSequence[touched]);
		        	intent.putExtra("WrongColor", color);
		        	intent.putExtra("TotalTime", totalTime);
		        	startActivity(intent);
	        	}
	        	else{
	        		faults--;
	        		Toast.makeText(MainActivity.this, 
	        				"Wrong color clicked. Click the correct color.", Toast.LENGTH_SHORT).show();
	        		faultIndicator.setText("FAULTS : "+faults);
	        	}
	        }
	        
	        if(touched == maxTouch){
//	        	Toast.makeText(MainActivity.this, "Correctly done", Toast.LENGTH_SHORT).show();
	        	cdt.cancel();
	        	intent.putExtra("Result", "Passed");
	        	intent.putExtra("UserName", userName);
				intent.putExtra("Level", level);
				intent.putExtra("Cheats", cheats);
				intent.putExtra("TotalTime", totalTime);
				intent.putExtra("RemaingTime", remainingTime);
	        	startActivity(intent);
	        }
	        return false;
	}
	}
// =============================== Touch Listener of  circles  Ends =====================================//	

// =============================== Animation function ===================================================//
	private void animationSequence(ShapeDrawable circle, final Button continueButton, final Button repeatButton){
		
		final ShapeDrawable animCircle = circle;
		Animator[] items = new Animator[maxTouch];
		for(touch=0;touch<maxTouch;touch++)
		{
						
			ObjectAnimator objAnimator = ObjectAnimator.
					ofObject(animCircle, "Color", new ArgbEvaluator(),touchSequence[touch],Color.TRANSPARENT);
			objAnimator.setDuration(2500);
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
		animSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				continueButton.setEnabled(false);
				repeatButton.setEnabled(false);
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				continueButton.setEnabled(true);
				repeatButton.setEnabled(true);
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		animSet.playSequentially(items);
		animSet.start();
	}
// =============================== Animation function Ends ==============================================//

	@Override
	public void onBackPressed(){
		if(backPressed!=1){
			Toast.makeText(this, "Press back again to go to main menu.", Toast.LENGTH_SHORT).show();
			backPressed++;
		}
		else{
			if(cdt!=null)
			cdt.cancel();
			Intent intent = new Intent(getApplicationContext(), Launch.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			startActivity(intent);
		}
	}
	
//================================ Cheat Button CLick Listener ==========================================//
	private class OnClickListner implements OnClickListener{
		
		Button cheatButton;
		public OnClickListner(Button cheatButton) {
			this.cheatButton=cheatButton;
		}

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.cheat){
				cheats--;
				SetTextFeatures.setFeatures(cheatButton, dialog_butons, "CHEATS : "+cheats.toString());
				final Dialog animdialog = new Dialog(MainActivity.this);
				animdialog.setCanceledOnTouchOutside(false);
				animdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				View animDialogView = getLayoutInflater().inflate(R.layout.dialog_color_animation, null);
				animdialog.setContentView(animDialogView);
				
				TextView dialog_heading = (TextView)animdialog.findViewById(R.id.dialog_heading);
				SetTextFeatures.setFeatures(dialog_heading, dialog_butons, "SEQUENCE", 40f);
				
				final TextView sequenceNumber = (TextView)animDialogView.findViewById(R.id.sequenceNumber);
				SetTextFeatures.setFeatures(sequenceNumber, dialog_butons, "", 20f);
				
				
				final Button continueButton = (Button)animDialogView.findViewById(R.id.dialog_continue);
				SetTextFeatures.setFeatures(continueButton, dialog_butons, "OK");
				
				
				final Button repeatButton = (Button) animDialogView.findViewById(R.id.dialog_repeat);
				SetTextFeatures.setFeatures(repeatButton, dialog_butons, "NEXT");
				
				
				ImageView animatorCircle = (ImageView) animDialogView.findViewById(R.id.animatorCircle);
				
				cheatTouch=0;
				if(cheatTouch==maxTouch-1){
					repeatButton.setText("REPEAT");
				}
				final ShapeDrawable animcircle = new ShapeDrawable(new OvalShape());
				animcircle.getPaint().setColor(touchSequence[cheatTouch]);
				animatorCircle.setBackground(animcircle);
				sequenceNumber.setText(cheatTouch+1+"/"+maxTouch);
				
				continueButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						animdialog.dismiss();
					}
				});
				
				repeatButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						animcircle.invalidateSelf();
						
						if(cheatTouch<maxTouch-1)
						{
							repeatButton.setText("NEXT");
							cheatTouch++;
							sequenceNumber.setText(cheatTouch+1+"/"+maxTouch);
							animcircle.getPaint().setColor(touchSequence[cheatTouch]);
						}
						if(cheatTouch == maxTouch-1){
							repeatButton.setText("REPEAT");
							cheatTouch=-1;
						}
					}
				});
				
				
				animdialog.show();
			}
			
		}
		
	}
}
