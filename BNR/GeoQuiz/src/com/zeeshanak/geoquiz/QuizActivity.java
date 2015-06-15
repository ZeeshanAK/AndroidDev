package com.zeeshanak.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity 
{
	private Button mTrueButton;
	private Button mFalseButton;
	private Button mCheatButton;
	private ImageButton mNextButton;
	private ImageButton mPrevButton;
	private TextView mQuestionTextView;
	
	private static final String KEY_INDEX = "index";
	private static final String TAG = "QuizActivity";
	
	private TrueFalse[] mQuestionBank = new TrueFalse[]
			{
				new TrueFalse(R.string.question_oceans, true),
				new TrueFalse(R.string.question_mideast, false),
				new TrueFalse(R.string.question_africa, false),
				new TrueFalse(R.string.question_americas, true),
				new TrueFalse(R.string.question_asia, true),
				new TrueFalse(R.string.question_mount, false),
				new TrueFalse(R.string.question_obama, false)
				
			};
	
	private int mCurrentIndex =0;
	
	private void updateQuestion()
	{
		mQuestionTextView = (TextView)findViewById(R.id.questionTextView);
		int question = mQuestionBank[mCurrentIndex].getQuestion();
		mQuestionTextView.setText(question);	
	}
	
	private void checkAnswer(boolean userPressedTrue)
	{
		boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
		int messageResID = 0;
		
		if(userPressedTrue == answerIsTrue)
		{
			messageResID = R.string.correct_toast;
		} else 
		{
			messageResID = R.string.incorrect_toast;
		}
		
		Toast.makeText(this, messageResID, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
			}
		
		updateQuestion();
		
		mNextButton = (ImageButton) findViewById(R.id.nxtButton);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();
			}
		});
		
		mPrevButton = (ImageButton) findViewById(R.id.prevButton);
		mPrevButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCurrentIndex == 0)
				{
					mCurrentIndex = mQuestionBank.length-1;
					updateQuestion();				
				}
				mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
				updateQuestion();				
			}
		});
		
		mCheatButton = (Button) findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(QuizActivity.this, CheatActivity.class);
				boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
				i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
				startActivityForResult(i, 0);
			}
		});
		
		
		mQuestionTextView = (TextView)findViewById(R.id.questionTextView);
		mQuestionTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();				
			}
		});
		
		mTrueButton = (Button) findViewById(R.id.trueButton);
		mTrueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(true);
			}
		});
		
		mFalseButton = (Button) findViewById(R.id.falseButton);
		mFalseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(false);
			}
		});
		
		
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	Log.i(TAG, "onSaveInstanceState");
	savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
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
}
