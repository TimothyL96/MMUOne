package club.itsociety.mmuone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		//View decorView = getWindow().getDecorView();
		//Hide the status bar
		//int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		//decorView.setSystemUiVisibility(uiOptions);
		//Remember that you should never show the action bar
		// if the status bar is hidden, so hide that too if necessary
		//ActionBar actionBar = getActionBar();
		//actionBar.hide();

		View.OnFocusChangeListener noInputFocusListener = new noInputFocusChangeListener();
		View.OnClickListener logInClickListener = new logInClickListener();
		View.OnTouchListener logInTouchListener = new logInTouchListener();

		ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintlayout);
		cl.setOnFocusChangeListener(noInputFocusListener);

		Button logIn = (Button) findViewById(R.id.btn_login);
		logIn.setOnFocusChangeListener(noInputFocusListener);
		logIn.setOnClickListener(logInClickListener);

		TextView signUpText = (TextView) findViewById(R.id.signUpText);
		signUpText.setOnClickListener(logInClickListener);
		signUpText.setOnTouchListener(logInTouchListener);

		ImageView signUpIcon = (ImageView) findViewById(R.id.signUpIcon);
		signUpIcon.setOnTouchListener(logInTouchListener);
		signUpIcon.setOnClickListener(logInClickListener);

		//Background Animation
		AnimationDrawable animationDrawable = (AnimationDrawable) cl.getBackground();
		animationDrawable.setEnterFadeDuration(500);
		animationDrawable.setExitFadeDuration(1200);
		animationDrawable.start();

		EditText editText = (EditText) findViewById(R.id.input_password);
		editText.setOnFocusChangeListener(noInputFocusListener);
	}

	private class noInputFocusChangeListener implements View.OnFocusChangeListener {

		public void onFocusChange(View view, boolean b)
		{
			if (b && (view.getId() == R.id.btn_login || view.getId() == R.id.constraintlayout))
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}

			if (b && view.getId() == R.id.input_password)
			{
				TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password);
				textInputLayout.setPasswordVisibilityToggleEnabled(true);
			}

			if (!b && view.getId() == R.id.input_password)
			{
				TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password);
				textInputLayout.setPasswordVisibilityToggleEnabled(false);
			}
		}
	}

	private class logInClickListener implements View.OnClickListener {

		public void onClick(View view)
		{
			if (view.getId() != R.id.btn_login)
			{
				TextView signUpText = (TextView) findViewById(R.id.signUpText);
				//signUpText.setPaintFlags(signUpText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
				signUpText.setTypeface(null, Typeface.BOLD);
				startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
				finish();
			}

			if (view.getId() == R.id.btn_login)
			{

			}
		}
	}

	private class logInTouchListener implements View.OnTouchListener {

		Rect rect;
		public boolean onTouch(View view, MotionEvent event)
		{
			TextView signUpText = (TextView) findViewById(R.id.signUpText);

			if (view.getId() != R.id.btn_login)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					signUpText.setTypeface(null, Typeface.BOLD);
					rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
				}

				if (event.getAction() == MotionEvent.ACTION_MOVE && !rect.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY())) {
					signUpText.setTypeface(null, Typeface.NORMAL);
				}
			}

			if (view.getId() == R.id.btn_login)
			{
				//Register
			}

			return false;
		}
	}

/*	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if ( v instanceof EditText) {
				Rect outRect = new Rect();
				v.getGlobalVisibleRect(outRect);
				if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
					v.clearFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		}
		return super.dispatchTouchEvent( event );
	}*/
}
