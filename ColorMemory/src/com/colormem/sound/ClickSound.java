package com.colormem.sound;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;

public class ClickSound {
	
	private final static MediaPlayer mp = new MediaPlayer();

	public ClickSound() {
		// TODO Auto-generated constructor stub
	}
	
	public static void clickSound(AssetManager assetManager){
		

		
		        if(mp.isPlaying())
		        {  
		            mp.stop();
		        } 

		        try {
		            mp.reset();
		            AssetFileDescriptor afd;
		            afd = assetManager.openFd("colorclick.mp3");
		            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
		            mp.prepare();
		            mp.start();
		        } catch (IllegalStateException e) {
		            Log.d("ClickSound", e.getMessage());
		        } catch (IOException e) {
		        	Log.d("ClickSound", e.getMessage());
		        }



		    }
	
	public static void release(){
		mp.release();
	}
		
	

}
