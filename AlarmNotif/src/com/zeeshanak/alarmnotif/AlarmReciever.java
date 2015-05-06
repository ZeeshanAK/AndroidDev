package com.zeeshanak.alarmnotif;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReciever extends BroadcastReceiver {
	   private int notificationID = 100;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
        Toast.makeText(context, "I'm running, YO!", Toast.LENGTH_LONG).show();
        	
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Mute activated for the time: !" );

        NotificationManager mNotificationManager =
        	    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        	    
        	// notificationID allows you to update the notification later on.
        	mNotificationManager.notify(notificationID, mBuilder.build());

	}

}
