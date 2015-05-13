package com.zeeshanak.alarmnotif;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AlarmReciever extends BroadcastReceiver 
{
	   private int notificationID = 100;
	   private AudioManager ringer;


	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle extraMsg = intent.getExtras();
				
		String setRingerModeRec = extraMsg.getString("setRingerMode");
		
		
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Profile Activated!");
        mBuilder.setContentText("Changed Ringer Mode to: " + setRingerModeRec );

        NotificationManager mNotificationManager =
        	    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        	    
        	// notificationID allows you to update the notification later on.
        	mNotificationManager.notify(notificationID, mBuilder.build());
        	
        	
    		ringer = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    		
    		if (setRingerModeRec.equals("Silent"))
    		{
    			ringer.setRingerMode(0);
    		} else if (setRingerModeRec.equals("Vibrate"))
    		{
    			ringer.setRingerMode(1);
    		} else if (setRingerModeRec.equals("Normal"))
    		{
    			ringer.setRingerMode(2);
    		}
    		
    	}
}






