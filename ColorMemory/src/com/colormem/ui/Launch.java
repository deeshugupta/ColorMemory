package com.colormem.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
 //============================Start Button Click Handler=========================================//
				final Dialog userDialog = new Dialog(Launch.this);
				userDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				View userDialogView = getLayoutInflater().inflate(R.layout.dialog_user, null);
				userDialog.setContentView(userDialogView);
				
				final RadioButton radioCheckActiveUser = (RadioButton) userDialogView.
						findViewById(R.id.radioCheck1);
				SetTextFeatures.setFeatures(radioCheckActiveUser, typeFace, "PLAY AS : ", 25f);
				TextView activeUsername  = (TextView) userDialogView.findViewById(R.id.username);
				radioCheckActiveUser.setChecked(true);
				
				final RadioButton radioCheckNewUser = (RadioButton) userDialogView.
						findViewById(R.id.radioCheck2);
				SetTextFeatures.setFeatures(radioCheckNewUser, typeFace, "PLAY AS : ", 25f);
				radioCheckNewUser.setChecked(false);
				final EditText newUsername = (EditText) userDialogView.findViewById(R.id.newusername);
				newUsername.setFocusableInTouchMode(true);
				newUsername.setBackground(null);
				SetTextFeatures.setFeatures(newUsername, typeFace, "Name till 8 characters", 25f);
				
				final RadioButton radioCheckUserList = (RadioButton) userDialogView.
						findViewById(R.id.radioCheck3);
				radioCheckUserList.setChecked(false);
				SetTextFeatures.setFeatures(radioCheckUserList, typeFace, "PLAY AS : ", 25f);
				Spinner userList = (Spinner) userDialog.findViewById(R.id.userList);
				
				List<String> usernames = userDAO.getAllUserNames();
				ArrayAdapter<String> dataAdapter = new UsersAdapter(Launch.this,
						android.R.layout.simple_spinner_item, usernames, typeFace);
				
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
				
				newUsername.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						newUsername.setText("");
					}
				});
				
				if(usernames.size()==0){
					radioCheckActiveUser.setVisibility(View.GONE);
					activeUsername.setVisibility(View.GONE);
					radioCheckUserList.setVisibility(View.GONE);
					userList.setVisibility(View.GONE);
					radioCheckNewUser.setChecked(true);
				}
				else if(activeuser==null){
					radioCheckActiveUser.setVisibility(View.GONE);
					activeUsername.setVisibility(View.GONE);
					radioCheckUserList.setChecked(true);
				}
				else{
					SetTextFeatures.setFeatures(activeUsername, typeFace, activeuser.getName(), 25f);
				}
				
				Button okButton = (Button) userDialogView.findViewById(R.id.okbuttonuserdialog);
				SetTextFeatures.setFeatures(okButton, typeFace, "OK");
				okButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(radioCheckNewUser.isChecked()){
							userNameSelected = newUsername.getText().toString();
							if(userNameSelected.length()>8)
								userNameSelected = userNameSelected.substring(0, 8);
							if(userNameSelected.length()==0)
								userNameSelected = "John Doe";
							activeuser = userDAO.createorgetUser(userNameSelected.toUpperCase());
						}
						else if(radioCheckUserList.isChecked()){
							activeuser = userDAO.createorgetUser(userNameSelected);
							userDialog.dismiss();
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
 //==================================Start Button Click Ends=======================================//
				
			case R.id.gameStats:
				List<User> users = userDAO.getAllUsers();
				Dialog statsDialog =  new Dialog(Launch.this);
				statsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				View statsView = getLayoutInflater().inflate(R.layout.dialog_stats_list,null);
				statsDialog.setContentView(statsView);
				
				TextView username = (TextView) statsView.findViewById(R.id.main_username);
				TextView level = (TextView) statsView.findViewById(R.id.main_level);
				TextView cheats = (TextView) statsView.findViewById(R.id.main_cheats);
				
				
				SetTextFeatures.setFeatures(username, typeFace, "USERNAME", 22f);
				SetTextFeatures.setFeatures(level, typeFace, "LEVEL", 22f);
				SetTextFeatures.setFeatures(cheats, typeFace, "CHEATS", 22f);
				
				TableLayout statsLayout = (TableLayout) statsView.findViewById(R.id.statsTable);
				
				for(User user : users){
					
				TableRow statsRow =(TableRow) getLayoutInflater().inflate(R.layout.stats_list_item, null);
				final TextView itemUsername = (TextView) statsRow.findViewById(R.id.item_username);
				TextView itemLevel = (TextView) statsRow.findViewById(R.id.item_level);
				TextView itemCheats = (TextView) statsRow.findViewById(R.id.item_cheats);
				SetTextFeatures.setFeatures(itemUsername, typeFace, user.getName(), 18f);
				SetTextFeatures.setFeatures(itemLevel, typeFace, user.getLevel().toString(), 18f);
				SetTextFeatures.setFeatures(itemCheats, typeFace, user.getCheats().toString(), 18f);
				
				statsRow.setOnLongClickListener(new OnLongPress(itemUsername.getText().toString(),
						statsLayout,statsRow));
				
				statsRow.setOnClickListener(new OnStatsClick());
				statsLayout.addView(statsRow);
				}
				
				statsDialog.show();
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
	
	private class OnLongPress implements OnLongClickListener{

		private String username;
		TableLayout statsLayout;
		TableRow statsRow;
		public OnLongPress(String username, TableLayout statsLayout, TableRow statsRow) {
			this.username =username;
			this.statsLayout=statsLayout;
			this.statsRow=statsRow;
		}

		@Override
		public boolean onLongClick(View v) {
			final Dialog alertDialog = new Dialog(Launch.this);
			alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View alertView = getLayoutInflater().inflate(R.layout.edit_user, null);
			alertDialog.setContentView(alertView);
			
			Button cancel = (Button) alertView.findViewById(R.id.cancel);
			SetTextFeatures.setFeatures(cancel, typeFace, "CANCEL");
			
			Button delete = (Button) alertView.findViewById(R.id.delete);
			SetTextFeatures.setFeatures(delete, typeFace, "DELETE");
			
			TextView deleteUser = (TextView) alertView.findViewById(R.id.deleteuser);
			SetTextFeatures.setFeatures(deleteUser, typeFace, "Are you sure that " +
					"you want to delete : "+ username+"?", 20f);
			
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					statsLayout.removeView(statsRow);
					userDAO.deleteUser(username);
					alertDialog.dismiss();
					
				}
			});
			
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					alertDialog.dismiss();
				}
			});
			alertDialog.show();
			
			return false;
		}
		
	}

	private class OnStatsClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			final Dialog changeUsername = new Dialog(Launch.this);
			changeUsername.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View changeView = getLayoutInflater().inflate(R.layout.edit_username, null);
			changeUsername.setContentView(changeView);
			TextView changeTitle = (TextView) changeView.findViewById(R.id.changeusername);
			SetTextFeatures.setFeatures(changeTitle, typeFace, "CHANGE USERNAME", 25f);
			
			final EditText changeusernameEditText = (EditText) changeView.findViewById(R.id.changeUserName);
			SetTextFeatures.setFeatures(changeusernameEditText, typeFace, "Change username here", 20f);
			changeusernameEditText.setBackground(null);
			changeusernameEditText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					changeusernameEditText.setText("");
					
				}
			});
			
			Button okchange = (Button) changeView.findViewById(R.id.Ok);
			SetTextFeatures.setFeatures(okchange, typeFace, "OK");
			Button cancelChange  = (Button) changeView.findViewById(R.id.Cancel);
			SetTextFeatures.setFeatures(cancelChange, typeFace, "CANCEL");
			
			changeUsername.show();
			
		}
		
	}
}
