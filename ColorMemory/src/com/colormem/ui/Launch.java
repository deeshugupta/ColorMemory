package com.colormem.ui;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.colormem.database.User;
import com.colormem.database.UserDAO;
import com.colormem.text.SetTextFeatures;
import com.colormem.text.UsersAdapter;

public class Launch extends Activity {

	User activeuser;
	UserDAO userDAO;
	Typeface typeFace;
	String userNameSelected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		//Initialising the database and Opening it 
		userDAO = new UserDAO(Launch.this);
		userDAO.open();
		
		activeuser = userDAO.getActiveUser();
		//Selecting the Font Family
		typeFace = Typeface.createFromAsset(getAssets(),
	            "fonts/KaushanScript-Regular.otf");
		
		TextView heading = (TextView) findViewById(R.id.heading);
		SetTextFeatures.setFeatures(heading, typeFace, "COLOR MEMORY", 40f);
		
		
		Button startGame = (Button) findViewById(R.id.gamestarter);
		SetTextFeatures.setFeatures(startGame, typeFace, "START");
		startGame.setOnClickListener(new Onclick());
		
		Button settingsGame = (Button) findViewById(R.id.gamesettings);
		SetTextFeatures.setFeatures(settingsGame, typeFace, "SETTINGS");
		
		
		Button exitGame = (Button) findViewById(R.id.gameStats);
		SetTextFeatures.setFeatures(exitGame, typeFace, "STATS");
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
				final Dialog userDialog = new Dialog(Launch.this);
				userDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				View userDialogView = getLayoutInflater().inflate(R.layout.dialog_user, null);
				userDialog.setContentView(userDialogView);
				
				final RadioButton radioCheckActiveUser = (RadioButton) userDialogView.findViewById(R.id.radioCheck1);
				SetTextFeatures.setFeatures(radioCheckActiveUser, typeFace, "PLAY AS : ", 25f);
				TextView activeUsername  = (TextView) userDialogView.findViewById(R.id.username);
				radioCheckActiveUser.setChecked(true);
				
				final RadioButton radioCheckNewUser = (RadioButton) userDialogView.findViewById(R.id.radioCheck2);
				SetTextFeatures.setFeatures(radioCheckNewUser, typeFace, "PLAY AS : ", 25f);
				radioCheckNewUser.setChecked(false);
				final EditText newUsername = (EditText) userDialogView.findViewById(R.id.newusername);
				newUsername.setBackground(null);
				SetTextFeatures.setFeatures(newUsername, typeFace, "NEW USER", 25f);
				
				final RadioButton radioCheckUserList = (RadioButton) userDialogView.findViewById(R.id.radioCheck3);
				radioCheckUserList.setChecked(false);
				SetTextFeatures.setFeatures(radioCheckUserList, typeFace, "PLAY AS : ", 25f);
				Spinner userList = (Spinner) userDialog.findViewById(R.id.userList);
				
				List<String> users = userDAO.getAllUsers();
				ArrayAdapter<String> dataAdapter = new UsersAdapter(Launch.this,
						android.R.layout.simple_spinner_item, users, typeFace);
				
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				userList.setAdapter(dataAdapter);
				
				userList.setOnItemSelectedListener(new OnItemSelected());
				
				radioCheckActiveUser.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						radioCheckNewUser.setChecked(false);
						radioCheckUserList.setChecked(false);
						
					}
				});
				
				radioCheckNewUser.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						radioCheckActiveUser.setChecked(false);
						radioCheckUserList.setChecked(false);
						
					}
				});
				
				radioCheckUserList.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						radioCheckNewUser.setChecked(false);
						radioCheckActiveUser.setChecked(false);
					}
				});
				
				
				if(activeuser==null){
					radioCheckActiveUser.setVisibility(View.GONE);
					activeUsername.setVisibility(View.GONE);
					radioCheckUserList.setVisibility(View.GONE);
					userList.setVisibility(View.GONE);
					radioCheckNewUser.setChecked(true);
				}
				else{
					SetTextFeatures.setFeatures(activeUsername, typeFace, activeuser.getName(), 25f);
				}
				
				Button okButton = (Button) userDialogView.findViewById(R.id.okbuttonuserdialog);
				SetTextFeatures.setFeatures(okButton, typeFace, "OK");
				okButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						userDialog.dismiss();
						if(radioCheckNewUser.isChecked()){
							userNameSelected = newUsername.getText().toString();
							activeuser = userDAO.createorgetUser(userNameSelected.toUpperCase());
						}
						else if(radioCheckUserList.isChecked()){
							activeuser = userDAO.createorgetUser(userNameSelected);
						}
						userDAO.close();
						Intent intent = new Intent(Launch.this,MainActivity.class);
						intent.putExtra("UserName", activeuser.getName());
						intent.putExtra("Level", activeuser.getLevel()+1);
						intent.putExtra("Cheats", activeuser.getCheats());
						startActivity(intent);
						
					}
				});
				userDialog.show();
				break;
			case R.id.gameStats:
				break;
			}
		}
		
	}
	
	private class OnItemSelected implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			userNameSelected = parent.getItemAtPosition(position).toString();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
