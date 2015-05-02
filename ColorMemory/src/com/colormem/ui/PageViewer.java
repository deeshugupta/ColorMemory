package com.colormem.ui;

import com.colormem.text.SetTextFeatures;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PageViewer extends Fragment {
	
	private int pageNumber;
	Typeface typeFace;
	Context context;
	
	public PageViewer(Context context,int pageNumber, AssetManager assetManager) {
		this.context=context;
		this.pageNumber=pageNumber;
		typeFace = Typeface.createFromAsset(assetManager,
	            "fonts/KaushanScript-Regular.otf");
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		ViewGroup rootView = null;
		if(pageNumber==0)
        {
			rootView = (ViewGroup) inflater.inflate(
                R.layout.help_page_1, container, false);
			setGamePlay(rootView, inflater);
			TextView gamePlay = (TextView) rootView.findViewById(R.id.gamehelp_title);
			SetTextFeatures.setFeatures(gamePlay, typeFace, "GAMEPLAY", 40f);
			Toast.makeText(context, "Swipe right to see more.", Toast.LENGTH_SHORT).show();
        }
		if(pageNumber==1){
			rootView = (ViewGroup) inflater.inflate(
	                R.layout.help_page_1, container, false);
				setUserOptions(rootView, inflater);
				TextView gamePlay = (TextView) rootView.findViewById(R.id.gamehelp_title);
				SetTextFeatures.setFeatures(gamePlay, typeFace, "USER OPTIONS", 40f);
		}
        
        return rootView;
    }

	private void setUserOptions(ViewGroup rootView, LayoutInflater inflater) {
		TableLayout gameHow = (TableLayout) rootView.findViewById(R.id.gamehow);
		addRow(gameHow,"1.","CHANGE VOLUME:\n" +
				"Click on SETTINGS to see volume control of the game.",inflater);
		addRow(gameHow,"2.","EDIT PLAYER:\n " +
				"Click on STATS and then click on player name to change the name of the player.",inflater);
		addRow(gameHow,"3.","DELETE PLAYER:\n " +
				"Click on STATS and then long press a player to delete the player.",inflater);
		
	}

	private void setGamePlay(ViewGroup rootView, LayoutInflater inflater) {
		
		TableLayout gameHow = (TableLayout) rootView.findViewById(R.id.gamehow);
		addRow(gameHow,"1.","Player will create a new user or " +
				"play with an existing user by clicking on START button.",inflater);
		addRow(gameHow,"2.","A sequence of color will be displayed on the screen. " +
				"Remember the sequence and click on CONTINUE. " +
				"Player can click on REPEAT to see the sequence again as many times as he/she wants.",inflater);
		addRow(gameHow,"3.","After clicking on CONTINUE, " +
				"the player will be presented with	" +
				"a grid of circles of various colors. " +
				"Touch the colors in the same sequence as shown before in the given time. ",inflater);
		addRow(gameHow,"4.","The player can also see the sequence one by one using the CHEATS. " +
				"Every new player gets 20 CHEATS for the first time. " +
				"Try to finish the game in half the time to earn a CHEAT.",inflater);
		addRow(gameHow,"5.","In later stages, FAULTS allowed will increase to give the player " +
				"a second chance to touch the correct color. FAULTS increase as the number" +
				" of colors shown in the sequence increases.",inflater);
		
		
		
	}

	
	private void addRow(TableLayout gameHow, String step, String desc, LayoutInflater inflater) {
		TableRow howRow = (TableRow) inflater.inflate(R.layout.game_how_row, null);
		TextView stepTextView = (TextView) howRow.findViewById(R.id.gamehowstep);
		SetTextFeatures.setFeatures(stepTextView, typeFace, step, 18f);
		stepTextView.setGravity(Gravity.TOP);
		TextView descTextView = (TextView) howRow.findViewById(R.id.gamehowdesc);
		SetTextFeatures.setFeatures(descTextView, typeFace, desc, 18f);
		gameHow.addView(howRow);
		
	}

	
}
