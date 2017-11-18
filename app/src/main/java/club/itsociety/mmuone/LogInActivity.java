package club.itsociety.mmuone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity
{
	String loginURL = "https://mmuone.com/api/users/loginUser.php";
	JSONObject reply;
	ProgressBar progressBar;

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

		CookieHandler.setDefault(new java.net.CookieManager( null, CookiePolicy.ACCEPT_ALL ) );

		//	Create events listener
		View.OnFocusChangeListener noInputFocusListener = new noInputFocusChangeListener();
		View.OnClickListener logInClickListener = new logInClickListener();
		View.OnTouchListener logInTouchListener = new logInTouchListener();
		TextWatcher textWatcher = new onTextWatchChanged();

		//	Create widgets objects
		ConstraintLayout cl = findViewById(R.id.constraintLayout);
		Button logIn = findViewById(R.id.btn_login);
		TextView signUpText = findViewById(R.id.signUpText);
		TextView forgotPasswordText = findViewById(R.id.forgotPassword);
		ImageView signUpIcon = findViewById(R.id.signUpIcon);
		EditText editTextStudentID = findViewById(R.id.input_studentID);
		EditText editTextPassword = findViewById(R.id.input_password);
		TextView textViewVolley = findViewById(R.id.textViewVolley);

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

		//	OnTextChange
		textViewVolley.addTextChangedListener(textWatcher);

		//	Gradient Background Animation
		//	Enter Fade Time: 500
		//	Exit Fade Time: 1200
		AnimationDrawable animationDrawable = (AnimationDrawable) cl.getBackground();
		animationDrawable.setEnterFadeDuration(500);
		animationDrawable.setExitFadeDuration(1000);
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
			Button button = findViewById(R.id.btn_login);
			TextView textViewForgotPassword = findViewById(R.id.forgotPassword);
			TextView textViewSignUpText = findViewById(R.id.signUpText);
			ImageView imageViewSignUpIcon = findViewById(R.id.signUpIcon);

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
					//	Disable the widgets
					button.setEnabled(false);
					editTextStudentID.setEnabled(false);
					editTextPassword.setEnabled(false);
					textViewForgotPassword.setEnabled(false);
					textViewSignUpText.setEnabled(false);
					imageViewSignUpIcon.setEnabled(false);

					//	Get the data into a Hash Map
					Map<String, String> params = new HashMap<>();
					params.put("student_id", editTextStudentID.getText().toString().trim());
					params.put("password_mmuone", editTextPassword.getText().toString().trim());

					//	Display progress bar
					progressBar = findViewById(R.id.progressBar);
					progressBar.setVisibility(View.VISIBLE);
					progressBar.animate();

					// New object for VolleyActivity class
					VolleyActivity volleyActivity = new VolleyActivity();


					volleyActivity.setParams(params);
					volleyActivity.volleyJsonObjectRequest(loginURL, view.getContext(), 1);
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
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					//	Set font weight to normal
					signUpText.setTypeface(null, Typeface.NORMAL);
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
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					//	Set font weight to normal
					forgotPasswordText.setTypeface(null, Typeface.NORMAL);
				}
			}

			return false;
		}
	}

	private class onTextWatchChanged implements TextWatcher
	{
		@Override
		public void onTextChanged(CharSequence charSequence, int start, int before, int count)
		{
			if (!charSequence.toString().isEmpty())
			{
				//	Get Text Input Layout widgets
				final TextInputLayout textInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
				final TextInputLayout textInputLayoutPassword = findViewById(R.id.input_layout_password);
				final Button buttonLogIn = findViewById(R.id.btn_login);
				final TextView textViewSignUp = findViewById(R.id.signUpText);
				final TextView textViewForgotPassword = findViewById(R.id.forgotPassword);
				final ImageView imageViewSignUp = findViewById(R.id.signUpIcon);
				final EditText editTextStudentID = findViewById(R.id.input_studentID);
				final EditText editTextPassword = findViewById(R.id.input_password);

				try
				{
					//	Put reply response into class variable 'reply'
					LogInActivity.this.reply = new JSONObject(charSequence.toString());

					//	Hide Progress bar
					progressBar.setVisibility(View.GONE);

					//	Check status
					if (reply.getString("status").contains("succeed"))
					{
						//If login succeeded

						//	Set XML animations to variables
						Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
						Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
						Animation slide_up_fast = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_fast);

						final Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

						//	Set drawable from XML
						final Drawable drawableLockToTick = ContextCompat.getDrawable(LogInActivity.this, R.drawable.lock_to_tick);

						//	Set the Constraint Layout
						final ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

						//	Create an ImageView for the animation
						final ImageView imageViewTick = new ImageView(LogInActivity.this);

						//	Set Text Input Layout visibility when animations end
						slide_up_fast.setAnimationListener(new Animation.AnimationListener()
						{
							@Override
							public void onAnimationStart(Animation animation)
							{

							}

							@Override
							public void onAnimationEnd(Animation animation)
							{
								//	Remove visibility
								textViewForgotPassword.setVisibility(View.GONE);
							}

							@Override
							public void onAnimationRepeat(Animation animation)
							{

							}
						});

						slide_down.setAnimationListener(new Animation.AnimationListener()
						{
							@Override
							public void onAnimationStart(Animation animation)
							{

							}

							@Override
							public void onAnimationEnd(Animation animation)
							{
								textInputLayoutStudentID.setVisibility(View.GONE);
								textInputLayoutPassword.setVisibility(View.GONE);
							}

							@Override
							public void onAnimationRepeat(Animation animation)
							{

							}
						});

						slide_up.setAnimationListener(new Animation.AnimationListener()
						{

							@Override
							public void onAnimationStart(Animation animation)
							{
								//	Show Signed In animation:
								//	Set a unique ID for setting up constraint layout later
								imageViewTick.setId(View.generateViewId());

								//	Set scale type to fit center
								imageViewTick.setScaleType(ImageView.ScaleType.CENTER);

								//	Set drawable to widget
								imageViewTick.setImageDrawable(drawableLockToTick);

								//	Add Constraint to the ImageView with ConstraintSet
								ConstraintSet constraintSet = new ConstraintSet();
								constraintSet.connect(imageViewTick.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
								constraintSet.connect(imageViewTick.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
								constraintSet.connect(imageViewTick.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
								constraintSet.connect(imageViewTick.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

								//	Set the constraint set to the current constraint layout
								constraintLayout.setConstraintSet(constraintSet);

								//	Add the ImageView to the Constraint Layout
								constraintLayout.addView(imageViewTick);

								//	Set fade in animation
								imageViewTick.setAnimation(fade_in);
							}

							@Override
							public void onAnimationEnd(Animation animation)
							{
								//	Hide visibility
								buttonLogIn.setVisibility(View.GONE);
								textViewSignUp.setVisibility(View.GONE);
								imageViewSignUp.setVisibility(View.GONE);
							}

							@Override
							public void onAnimationRepeat(Animation animation)
							{

							}
						});

						//	We want to run the lock to tick vector animation when fade in completes
						fade_in.setAnimationListener(new Animation.AnimationListener()
						{
							@Override
							public void onAnimationStart(Animation animation)
							{

							}

							@Override
							public void onAnimationEnd(Animation animation)
							{
								//	Set Signed In animation duration + display it on screen time
								//	Animation time: 1000
								//	Display on screen time: 1000
								int animationWaitTime = 2000;

								//	Start the Signed In animation
								((Animatable) drawableLockToTick).start();

								constraintLayout.postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										startActivity(new Intent(LogInActivity.this, MainActivity.class));
										finish();
									}
								}, animationWaitTime);
							}

							@Override
							public void onAnimationRepeat(Animation animation)
							{

							}
						});

						//	Start the animations
						textInputLayoutStudentID.startAnimation(slide_down);
						textInputLayoutPassword.startAnimation(slide_down);

						textViewForgotPassword.startAnimation(slide_up_fast);

						buttonLogIn.startAnimation(slide_up);
						textViewSignUp.startAnimation(slide_up);
						imageViewSignUp.startAnimation(slide_up);
					}
					else if (reply.getString("status").contains("failed"))
					{
						//	If login failed
						//	Error Code:
						//	10611	-	NO ACCOUNT FOUND
						//	10612	-	PASSWORD ERROR
						//	10613	-	FATAL ERROR: NON UNIQUE ID WHILE SIGNING IN
						//	10614	-	FATAL ERROR: COUNT IS NEGATIVE WHILE SIGNING IN

						switch (reply.getInt("code"))
						{
							case 10611:
								textInputLayoutStudentID.setError("No account exist for this student ID. Have you registered?");
								editTextStudentID.requestFocus();
								break;
							case 10612:
								textInputLayoutPassword.setError("Password incorrect. Please check again.");
								editTextPassword.requestFocus();
								break;
							case 10613:
								textInputLayoutStudentID.setError("FATAL ERROR: NON UNIQUE ID");
								editTextStudentID.requestFocus();
								break;
							case 10614:
								textInputLayoutStudentID.setError("FATAL ERROR: COUNT IS NEGATIVE");
								editTextStudentID.requestFocus();
								break;
							default:
								textInputLayoutStudentID.setError("FATAL ERROR: UNKNOWN ERROR");
								editTextStudentID.requestFocus();
						}
						//	TODO timeout on brute force login
						//	TODO Set IP and phone model

						//	Enable the widgets
						editTextStudentID.setEnabled(true);
						editTextPassword.setEnabled(true);
						textViewForgotPassword.setEnabled(true);
						buttonLogIn.setEnabled(true);
						textViewSignUp.setEnabled(true);
						imageViewSignUp.setEnabled(true);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
		{

		}

		@Override
		public void afterTextChanged(Editable editable)
		{

		}
	}
}