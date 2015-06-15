package com.zeeshanak.geoquiz;

import android.app.Activity;
import android.content.Intent;
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
	
	private boolean mAnswerIsTrue;
	private Button mShowAnswer;
	private TextView mAnswerTextView;
	
	
	private void setAnswerisShown (boolean isAnswerShown)
	{
		Intent data = new Intent();
		data.putExtra(ANSWER_IS_SHOWN, isAnswerShown);
		setResult(RESULT_OK, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
		
		setAnswerisShown(false);
		
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
				setAnswerisShown(true);
			}
		});
	}

}
