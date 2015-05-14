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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	private AlarmManager alarmMgr;
	private AlarmManager alarmMgrEnd;

	private PendingIntent alarmIntent;
	private PendingIntent alarmIntentEnd;

	private AudioManager ringer;
	private EditText startTime;
	private EditText startTimeSelected;
	private EditText endTime; 
	private Button setButton;
	private static String whichTextView;
	private static String setRingerMode;
	private static String revRingerMode;
	private int currentRinger; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get required views
		startTime = (EditText) findViewById(R.id.startTime);
		startTimeSelected =(EditText) findViewById(R.id.startTimeSelected ); 
		endTime = (EditText) findViewById(R.id.endTime);
		setButton = (Button) findViewById(R.id.setAlarm);

		//Displaying current Ringer mode on the phone
		ringerMode();

		//Populate the start and end time fields with current time
		populateFields(startTime, endTime);
		
		//Launch the Time Picker dialog when user clicks the Start Time field
		startTime.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				whichTextView = "startTime";
				DialogFragment newFragment = new TimePickerFragment();
			    newFragment.show(getFragmentManager(), "timePickerStart");				
			}
		});
		
		//Launch the Time Picker dialog when user clicks the End Time field
		endTime.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				whichTextView = "endTime";
				DialogFragment newFragment = new TimePickerFragment();
			    newFragment.show(getFragmentManager(), "timePickerStart");					
			}
		});
		
		//Set the alarm when user clicks the Set Alarm button
		setButton.setOnClickListener(new OnClickListener() 
		{
	
			@Override
			public void onClick(View v) 
			{
				setStartTime();
				//setEndTime();
			}
		});
	}
	
	
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_normal:
	            if (checked)
	            	setRingerMode = "Normal";
	            break;
	        case R.id.radio_vibrate:
	            if (checked)
	            	setRingerMode = "Vibrate";
	            break;
	        case R.id.radio_silent:
	            if (checked)
	            	setRingerMode = "Silent";
	            break;
	    }
	}
	
	public void ringerMode ()
	{
		ringer = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		TextView textRinger = (TextView) findViewById(R.id.getRinger);
		currentRinger = ringer.getRingerMode();		
		switch (currentRinger)
		{
			case 0: textRinger.setText("Silent");
					revRingerMode = "Silent";
				break;
			case 1: textRinger.setText("Vibrate");
					revRingerMode = "Vibrate";
				break;
			case 2: textRinger.setText("Normal");
					revRingerMode = "Normal";
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
	
	
	public void setStartTime()
	{
		alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(MainActivity.this, AlarmReciever.class);
		intent.putExtra("setRingerMode",setRingerMode );
		alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

		//We receive the string as "12:12 AM", so splitting it to get the HR first 
		String startTextString = startTime.getText().toString();
		String[] getHR = startTextString.split(":");
		String hrStartString = getHR[0]; //Hour selected by user
		String mnStartStringAM = getHR[1];
		
		//Split the second portion to drop the AM from the second part
		String[] getMN = mnStartStringAM.split(" ");
		String mnStartString = getMN[0]; //Min selected by used
		
		//getting hourOfDay from startTimeSelected
		String startTimeSelectedStr = startTimeSelected.getText().toString();
		
		int startHR = Integer.parseInt(startTimeSelectedStr);
		int startMNT = Integer.parseInt(mnStartString);

		 //Set the alarm
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, startHR);
		calendar.set(Calendar.MINUTE, startMNT);
		
		alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        Toast.makeText(MainActivity.this, "Alarm Set for " + startTimeSelectedStr + ":" + mnStartString, Toast.LENGTH_LONG).show();	
	}

	public void setEndTime()
	{
		alarmMgrEnd = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent endIntent = new Intent(MainActivity.this, AlarmReciever.class);
		endIntent.putExtra("setRingerMode",revRingerMode );
		alarmIntentEnd = PendingIntent.getBroadcast(MainActivity.this, 0, endIntent, 0);

		//We receive the string as "12:12 AM", so splitting it to get the HR first 
		String endTextString = endTime.getText().toString();
		String[] getHR = endTextString.split(":");
		String hrEndString = getHR[0];
		String mnEndStringAM = getHR[1];
		
		//Split the second portion to drop the AM from the second part
		String[] getMN = mnEndStringAM.split(" ");
		String mnEndString = getMN[0];
		
		int endHR = Integer.parseInt(hrEndString);
		int endMNT = Integer.parseInt(mnEndString);

		 //Set the alarm
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, endHR);
		calendar.set(Calendar.MINUTE, endMNT);
		
		alarmMgrEnd.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntentEnd);
        Toast.makeText(MainActivity.this, "Alarm will end at " + hrEndString + ":" + mnEndString, Toast.LENGTH_LONG).show();	
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
			
			//setting just the hourofDay to this hidden field because the following conversion from 
			//24hrs to 12hr seems to be messing the Alarm.
			final EditText startTimeSelected = (EditText) findViewById(R.id.startTimeSelected);
			((TextView) startTimeSelected).setText(String.valueOf(hourOfDay));
									
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



