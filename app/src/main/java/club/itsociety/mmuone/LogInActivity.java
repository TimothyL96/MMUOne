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

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity
{
	String loginURL = "https://mmuone.com/api/users/loginUser.php";

	//	@Override prevents or disables method overloading
	//	If the parameters in new method is entered wrongly from the parent method
	//	It will then become overloading instead of overriding, @Override check for this
	//	An error will be shown if it is overloaded with @Override
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//	Allow ScrollView to scroll
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		/*
			View decorView = getWindow().getDecorView();
			Hide the status bar
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
			Remember that you should never show the action bar
			 if the status bar is hidden, so hide that too if necessary
			ActionBar actionBar = getActionBar();
			actionBar.hide();
		*/

		//	Create events listener
		View.OnFocusChangeListener noInputFocusListener = new noInputFocusChangeListener();
		View.OnClickListener logInClickListener = new logInClickListener();
		View.OnTouchListener logInTouchListener = new logInTouchListener();

		//	Create widgets objects
		ConstraintLayout cl = findViewById(R.id.constraintLayout);
		Button logIn = findViewById(R.id.btn_login);
		TextView signUpText = findViewById(R.id.signUpText);
		TextView forgotPasswordText = findViewById(R.id.forgotPassword);
		ImageView signUpIcon = findViewById(R.id.signUpIcon);
		EditText editTextStudentID = findViewById(R.id.input_studentID);
		EditText editTextPassword = findViewById(R.id.input_password);

		//	Set the widget object to event listener
		//	OnFocusChangeListener
		cl.setOnFocusChangeListener(noInputFocusListener);
		logIn.setOnFocusChangeListener(noInputFocusListener);
		editTextStudentID.setOnFocusChangeListener(noInputFocusListener);
		editTextPassword.setOnFocusChangeListener(noInputFocusListener);

		//	OnClickListener
		logIn.setOnClickListener(logInClickListener);
		signUpText.setOnClickListener(logInClickListener);
		signUpIcon.setOnClickListener(logInClickListener);
		forgotPasswordText.setOnClickListener(logInClickListener);

		//	OnTouchListener
		signUpText.setOnTouchListener(logInTouchListener);
		signUpIcon.setOnTouchListener(logInTouchListener);
		forgotPasswordText.setOnTouchListener(logInTouchListener);

		//	Gradient Background Animation
		//	Enter Fade Time: 500
		//	Exit Fade Time: 1200
		AnimationDrawable animationDrawable = (AnimationDrawable) cl.getBackground();
		animationDrawable.setEnterFadeDuration(500);
		animationDrawable.setExitFadeDuration(1200);
		animationDrawable.start();
	}

	//	Class for OnFocusChangeListener events
	private class noInputFocusChangeListener implements View.OnFocusChangeListener
	{
		//	Method that needs to be overridden
		@Override
		public void onFocusChange(View view, boolean b)
		{
			//	Widget objects
			EditText editTextStudentID = findViewById(R.id.input_studentID);
			EditText editTextPassword = findViewById(R.id.input_password);
			TextInputLayout textInputLayoutPassword = findViewById(R.id.input_layout_password);
			TextInputLayout textInputLayoutStudentID = findViewById(R.id.input_layout_studentID);

			//	If focused and it is the login button or the constraint layout
			if (b && view.getId() == R.id.constraintLayout)
			{
				//This will hide the keyboard or soft input
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null)
				{
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}

			//	Next two if else perform same thing over different views - studentID and password
			//
			//	If focused:
			//	If textInputLayout error is present at the bottom of input
			//	Remove the error for editText that will appear on the right side
			//	Else show the error again but remove the error icon of editText
			//	As the icon will overlap with the password visibility icon
			//
			//	If lost focus:
			//	Check the strings if empty ask them to enter their student ID or password
			//	If not empty but length not enough, ask them to check their student ID and password
			//	Lastly remove the error from editText as there's error display from TextInputLayout

			//	If the focused or unfocused widget is the studentID input field
			if (view.getId() == R.id.input_studentID)
			{
				if (b)
				{
					//	If focused
					if (textInputLayoutStudentID.getError() != null)
					{
						editTextStudentID.setError(null);
					}
					else
					{
						editTextStudentID.setError(editTextStudentID.getError(), null);
					}
				}
				else
				{
					//	If unfocused
					if (editTextStudentID.getText().toString().trim().length() < 10 && !editTextStudentID.getText().toString().isEmpty())
					{
						textInputLayoutStudentID.setError("Please check your student ID");
					}
					else
					{
						textInputLayoutStudentID.setError(null);
					}
					editTextStudentID.setError(null);
				}
			}

			//	If the focused or unfocused widget is the password input field
			if (view.getId() == R.id.input_password)
			{
				if (b)
				{
					//	If focused
					if (textInputLayoutPassword.getError() != null)
					{
						editTextPassword.setError(null);
					}
					else
					{
						//If unfocused
						editTextPassword.setError(editTextPassword.getError(), null);
					}

					// Enable the password visibility toggle if the input is focused
					textInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
				}
				else
				{
					if (editTextPassword.getText().toString().trim().length() < 6 && !editTextPassword.getText().toString().isEmpty())
					{
						textInputLayoutPassword.setError("Please check your password");
					}
					else
					{
						textInputLayoutPassword.setError(null);
					}
					editTextPassword.setError(null);

					//	Remove the password visibility toggle if focus is not on password field
					textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
				}
			}
		}
	}

	//	Class for OnClickListener events
	private class logInClickListener implements View.OnClickListener
	{
		//	Method that needs to be overridden
		@Override
		public void onClick(View view)
		{
			//	Widget objects
			TextInputLayout textInputLayoutPassword = findViewById(R.id.input_layout_password);
			TextInputLayout textInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
			EditText editTextStudentID = findViewById(R.id.input_studentID);
			EditText editTextPassword = findViewById(R.id.input_password);
			ConstraintLayout cl = findViewById(R.id.constraintLayout);

			//	If clicked widget is the sign up texts or sign up icon at the bottom of page
			if (view.getId() == R.id.signUpText || view.getId() == R.id.signUpIcon)
			{
				//	Underline the text
				//signUpText.setPaintFlags(signUpText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

				//	Start the Log In Activity
				startActivity(new Intent(LogInActivity.this, RegisterActivity.class));

				//	End current activity so the screen will not come back when back button pressed
				finish();
			}

			//	If clicked widget is forgot password text
			if (view.getId() == R.id.forgotPassword)
			{
				//	Start the Forgot Password Activity
				startActivity(new Intent(LogInActivity.this, ForgotPassword.class));

				//	Clear password field
				editTextPassword.setText("");

				//	Clear errors except error for studentID in TextInputLayout
				if (editTextPassword.getError() != null)
					editTextPassword.setError(null);

				if (editTextStudentID.getError() != null)
					editTextStudentID.setError(null);

				if (textInputLayoutPassword.getError() != null)
					textInputLayoutPassword.setError(null);
			}

			//	If clicked widget is the login button
			//	We will login the user in here
			if (view.getId() == R.id.btn_login)
			{
				//	Remove all focus when button clicked and instead focus the constraint layout
				cl.requestFocus();

				//	Check for studentID and password field when login button clicked
				//	Set errors with error icon if they are empty and not focused
				//	The errors will show up once they focus a particular input field
				//	If focused, show the error but not the error icon
				//	As password visibility icon might overlap with the error icon
				//	Errors will also be shown if minimum length not met
				//	Minimum and maximum length of each input field:
				//	StudentID 	->		10 	-	15
				//	Password	->		6 	- 	25
				if (editTextStudentID.getText().toString().trim().equals(""))
				{
					if (editTextStudentID.isFocused())
					{
						//	null to remove the error icon
						editTextStudentID.setError("Please enter your student ID", null);
					}
					else
					{
						editTextStudentID.setError("Please enter your student ID");
					}
				}
				else if (editTextStudentID.getText().toString().trim().length() < 10)
				{
					if (editTextStudentID.isFocused())
					{
						editTextStudentID.setError("Please check your student ID", null);
					}
					else
					{
						editTextStudentID.setError("Please check your student ID");
					}
				}

				if (editTextPassword.getText().toString().trim().equals(""))
				{
					if (editTextPassword.isFocused())
					{
						editTextPassword.setError("Please enter your password", null);
					}
					else
					{
						editTextPassword.setError("Please enter your password");
					}

				}
				else if (editTextPassword.getText().toString().trim().length() < 6)
				{
					if (editTextPassword.isFocused())
					{
						editTextPassword.setError("Please check your password", null);
					}
					else
					{
						editTextPassword.setError("Please check your password");
					}
				}

				//	If all data verified, log the user in
				if (editTextStudentID.getError() == null && editTextPassword.getError() == null && textInputLayoutStudentID.getError() == null & textInputLayoutPassword.getError() == null)
				{
					//	TODO when input data verified
					VolleyActivity volleyActivity = new VolleyActivity();

					Map<String, String> params = new HashMap<>();
					params.put("student_id", editTextStudentID.getText().toString().trim());
					params.put("password_mmuone", editTextPassword.getText().toString().trim());

					volleyActivity.setParams(params);
					volleyActivity.volleyJsonObjectRequest(loginURL, view.getContext());
				}
			}
		}
	}

	//	Class for OnTouchListener events
	private class logInTouchListener implements View.OnTouchListener
	{
		//	Rect or Rectangle records positions to detect the motion or position of cursor
		//	whether it is still hovering or not from a widget
		Rect rect;

		//	Method that needs to be overridden
		@Override
		public boolean onTouch(View view, MotionEvent event)
		{
			//	Widget object
			TextView signUpText = findViewById(R.id.signUpText);
			TextView forgotPasswordText = findViewById(R.id.forgotPassword);

			//	If touched widget is sign up text or icon
			if (view.getId() == R.id.signUpText || view.getId() == R.id.signUpIcon)
			{
				//	For touch event that is action down or "mouse down"
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					//	Set font weight to bold
					signUpText.setTypeface(null, Typeface.BOLD);

					//	Record the rectangle position
					rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
				}

				//	If the touch event is move and the rect or "cursor" is not hovering the widget
				if (event.getAction() == MotionEvent.ACTION_MOVE && !rect.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY()))
				{
					//	Set font weight to normal
					signUpText.setTypeface(null, Typeface.NORMAL);
				}

				//	For event that is action up and is inside the widget, we want to perform click
				if (event.getAction() == MotionEvent.ACTION_UP && rect.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY()))
				{
					view.performClick();
				}
			}

			//	If touched widget is forgot password text
			if (view.getId() == R.id.forgotPassword)
			{
				//	For touch event that is action down or "mouse down"
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					//	Set font weight to bold
					forgotPasswordText.setTypeface(null, Typeface.BOLD);

					//	Record the rectangle position
					rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
				}

				//	If the touch event is move and the rect or "cursor" is not hovering the widget
				if (event.getAction() == MotionEvent.ACTION_MOVE && !rect.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY()))
				{
					//	Set font weight to normal
					forgotPasswordText.setTypeface(null, Typeface.NORMAL);
				}

				//	For event that is action up and is inside the widget, we want to perform click
				if (event.getAction() == MotionEvent.ACTION_UP && rect.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY()))
				{
						view.performClick();
				}
			}

			return false;
		}
	}
}
