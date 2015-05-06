package com.zeeshanak.alarmnotif;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	private AudioManager ringer;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		ringer = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		TextView textRinger = (TextView) findViewById(R.id.getRinger);
		int currentRinger = ringer.getRingerMode();		
		switch (currentRinger)
		{
			case 0:
				textRinger.setText("Silent");
				break;
			case 1:
				textRinger.setText("Vibrate");
				break;
			case 2:
				textRinger.setText("Normal");
				break;
				default:
					textRinger.setText("Cannot read ringer mode from settings!");
				
				
		}
		
		
		
		
		alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(MainActivity.this, AlarmReciever.class);
		alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

		
		Button setButton = (Button) findViewById(R.id.setAlarm);
		
		setButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText startTime = (EditText) findViewById(R.id.getStartTime);
				EditText endTime = (EditText) findViewById(R.id.getEndTime);
				
				int start = Integer.decode(startTime.getText().toString());
				int end = Integer.decode(endTime.getText().toString());

				// Set the alarm
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, start);
				calendar.set(Calendar.MINUTE, end);
				
				alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
		        Toast.makeText(MainActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();				
			}
		});
		
		
		
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
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//	
//	public int getStart() {return start;}
//	public int getEnd() { return end;}
}
