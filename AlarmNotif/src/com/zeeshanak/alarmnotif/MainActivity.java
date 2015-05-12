package com.zeeshanak.alarmnotif;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	private AudioManager ringer;
	private static String whichTextView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Displaying current Ringer mode on the phone
		ringerMode();

		//Populate the start and end time fields with current time
		final EditText startTime = (EditText) findViewById(R.id.startTime);
		final EditText endTime = (EditText) findViewById(R.id.endTime);
		populateFields(startTime, endTime);
		
		
		
		alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(MainActivity.this, AlarmReciever.class);
		alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

		
		Button setButton = (Button) findViewById(R.id.setAlarm);
		setButton.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				
				String startTextString = startTime.getText().toString();
				String[] time = startTextString.split(":");
				String hrStartString = time[0];
				String mnStartStringAM = time[1];
				String[] mnStartStringWithoutAM = mnStartStringAM.split(" ");
				String mnStartString = mnStartStringWithoutAM[0];
				
				int startHR = Integer.parseInt(hrStartString);
				int startMNT = Integer.parseInt(mnStartString);

				 //Set the alarm
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, startHR);
				calendar.set(Calendar.MINUTE, startMNT);
				
				alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
		        Toast.makeText(MainActivity.this, "Alarm Set " + mnStartString, Toast.LENGTH_LONG).show();				
			}
		});

		
		startTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				whichTextView = "startTime";
				DialogFragment newFragment = new TimePickerFragment();
			    newFragment.show(getFragmentManager(), "timePickerStart");				
			}
		});
		
		endTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				whichTextView = "endTime";
				DialogFragment newFragment = new TimePickerFragment();
			    newFragment.show(getFragmentManager(), "timePickerStart");					
			}
		});
		
	}
	
	
	
	public void ringerMode ()
	{
		ringer = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		TextView textRinger = (TextView) findViewById(R.id.getRinger);
		int currentRinger = ringer.getRingerMode();		
		switch (currentRinger)
		{
			case 0: textRinger.setText("Silent");
				break;
			case 1: textRinger.setText("Vibrate");
				break;
			case 2: textRinger.setText("Normal");
				break;
			default: textRinger.setText("Cannot read ringer mode from settings!");
		}
	}
	
	public void populateFields(View startTime, View endTime)
	{
		
		Calendar mCal = Calendar.getInstance();
		int hr = mCal.get(Calendar.HOUR_OF_DAY);
		int mnt = mCal.get(Calendar.MINUTE);
		String amPM = "";
		if(hr < 12) {
			amPM = " AM";
		} else {
			amPM = " PM";
		}
		
		if (hr > 12)
		{
			hr = hr - 12;
		}
		
		((TextView) startTime).setText(String.format("%02d",hr) + ":" + String.format("%02d", mnt) 
				+ amPM);
		
		((TextView) endTime).setText(String.format("%02d", hr + 1) + ":" + String.format("%02d", mnt) 
				+amPM);
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
	
	//Class to implement timePicker dialog fragment
	public class TimePickerFragment extends DialogFragment
	implements TimePickerDialog.OnTimeSetListener 
	{
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
	
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, 
				DateFormat.is24HourFormat(getActivity()));
		}	
	
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
		{
									
			String amPM = "";
			if(hourOfDay < 12) {
				amPM = " AM";
			} else {
				amPM = " PM";
			}
			
			if (hourOfDay > 12)
			{
				hourOfDay = hourOfDay - 12;
			}
	
			if (whichTextView.equals("startTime"))
				{
					final EditText startTime = (EditText) findViewById(R.id.startTime);
					((TextView) startTime).setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + amPM);
				} 
			else 
				{
					final EditText endTime = (EditText) findViewById(R.id.endTime);
					((TextView) endTime).setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + amPM);
				}
		}
	}

}



