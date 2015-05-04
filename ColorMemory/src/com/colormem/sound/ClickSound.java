package com.colormem.sound;

import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;

import com.colormem.ui.R;

public class ClickSound {
	
	private static MediaPlayer mp = new MediaPlayer();


	public ClickSound() {
	}
	
	public static void clickSound(AssetManager assetManager){
		
		
			try {
		        if(mp.isPlaying())
		        {  
		            mp.stop();
		        } 

		        
		            mp.reset();
		            AssetFileDescriptor afd;
		            afd = assetManager.openFd("colorclick.mp3");
		            mp.setAudioStreamType(AudioManager.STREAM_DTMF);
		            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
		            mp.prepare();
		            mp.start();
		        } catch (IllegalStateException e) {
		        	if(mp!=null)
		        		mp = new MediaPlayer();
		        } catch (IOException e) {
		        	if(mp!=null)
		        		mp = new MediaPlayer();
		        }



		    }
	
	public static void release(){
		mp.release();
	}

	
		
	

}
