package com.zeeshanak.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity 
{
	
	public static final String EXTRA_ANSWER_IS_TRUE =
			"com.zeeshanak.geoquiz.answer_is_true";
	public static final String ANSWER_IS_SHOWN =
			"com.zeeshanak.geoquiz.answer_is_shown";
	private static final String KEY_CHEAT = "cheat";
	
	private boolean mAnswerIsTrue;
	private Button mShowAnswer;
	private TextView mAnswerTextView;
	private boolean mIsCheater = false;
	private TextView sdkVer;
	private int buildVer = Build.VERSION.SDK_INT;
	
	
	private void setAnswerisShown ()
	{
		Intent data = new Intent();
		data.putExtra(ANSWER_IS_SHOWN, mIsCheater);
		setResult(RESULT_OK, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
		if (savedInstanceState != null) 
		{
				mIsCheater = savedInstanceState.getBoolean(KEY_CHEAT);
		}
		
		setAnswerisShown();
		
		mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
		
		mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
		mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
		mShowAnswer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mAnswerIsTrue)
				{
					mAnswerTextView.setText(R.string.true_button);
				} else
				{
					mAnswerTextView.setText(R.string.false_button);
				}
				mIsCheater = true;
				setAnswerisShown();
			}
		});
		
		sdkVer = (TextView) findViewById(R.id.sdkVer);
		sdkVer.setText("API Level: " + buildVer);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean(KEY_CHEAT, mIsCheater);
	}

}
