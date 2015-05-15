package com.zeeshanak.alarmnotif;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.widget.TextView;

public class RingerMode extends Activity
{
	private AudioManager ringer;
	private String textRinger = "";
	private int ringerModeInt; 	
	
	public void ringerMode ()
	{
		ringer = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		ringerModeInt = ringer.getRingerMode();	
		switch (ringerModeInt)
			{
				case 0: textRinger = "Silent";
						
					break;
				case 1: textRinger = "Vibrate";
	
					break;
				case 2: textRinger = "Normal";
	
					break;
				default: textRinger = "Cannot read ringer mode from settings!";
			}
		}
}