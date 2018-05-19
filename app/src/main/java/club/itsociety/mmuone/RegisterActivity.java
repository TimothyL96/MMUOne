package club.itsociety.mmuone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity
{
	String registerURL = "https://mmuone.com/api/users/create.php";
	JSONObject reply;
	ProgressBar progressBar;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		//	Create events listener
		View.OnClickListener registerClickListener = new registerClickListener();
		View.OnTouchListener registerTouchListener = new registerTouchListener();
		View.OnFocusChangeListener noInputFocusListener = new noInputFocusChangeListener();
		TextWatcher textWatcher = new onTextWatchChanged();

		//	Create widgets objects
		TextView logInText = findViewById(R.id.logInText);
		TextView textViewVolley = findViewById(R.id.textViewVolley);
		ConstraintLayout cl = findViewById(R.id.constraintLayout);
		Button register = findViewById(R.id.btn_register);
		ImageView logInIcon = findViewById(R.id.logInIcon);
		EditText editTextFullName = findViewById(R.id.input_fullName);
		EditText editTextEmail = findViewById(R.id.input_email);
		EditText editTextStudentID = findViewById(R.id.input_studentID);
		EditText editTextPassword = findViewById(R.id.input_password);

		//	Set the widget object to event listener
		//	OnClickListener
		logInText.setOnClickListener(registerClickListener);
		register.setOnClickListener(registerClickListener);
		logInIcon.setOnClickListener(registerClickListener);

		//	OnTouchListener
		logInText.setOnTouchListener(registerTouchListener);
		logInIcon.setOnTouchListener(registerTouchListener);

		//	OnFocusChangeListener
		cl.setOnFocusChangeListener(noInputFocusListener);
		register.setOnFocusChangeListener(noInputFocusListener);
		editTextFullName.setOnFocusChangeListener(noInputFocusListener);
		editTextEmail.setOnFocusChangeListener(noInputFocusListener);
		editTextStudentID.setOnFocusChangeListener(noInputFocusListener);
		editTextPassword.setOnFocusChangeListener(noInputFocusListener);

		//	On text changed listener
		textViewVolley.addTextChangedListener(textWatcher);

		//	Gradient Background Animation
		//	Enter Fade Time: 500
		//	Exit Fade Time: 1200
		AnimationDrawable animationDrawable = (AnimationDrawable) cl.getBackground();
		animationDrawable.setEnterFadeDuration(500);
		animationDrawable.setExitFadeDuration(1000);
		animationDrawable.start();

		//	TODO add contact us support - if student ID taken making them couldn't register
	}

	//	Class for OnClickListener events
	private class registerClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			//	Get all the widgets
			TextView logInText = findViewById(R.id.logInText);
			MyTextInputLayout MyTextInputLayoutFullName = findViewById(R.id.input_layout_fullName);
			MyTextInputLayout MyTextInputLayoutEmail = findViewById(R.id.input_layout_email);
			MyTextInputLayout MyTextInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
			MyTextInputLayout MyTextInputLayoutPassword = findViewById(R.id.input_layout_password);
			EditText editTextFullName = findViewById(R.id.input_fullName);
			EditText editTextEmail = findViewById(R.id.input_email);
			EditText editTextStudentID = findViewById(R.id.input_studentID);
			EditText editTextPassword = findViewById(R.id.input_password);
			ConstraintLayout cl = findViewById(R.id.constraintLayout);
			Button button = findViewById(R.id.btn_register);
			ImageView logInIcon = findViewById(R.id.logInIcon);

			//	If clicked widget is the login text or icon at the bottom of page
			if (view.getId() == R.id.logInText || view.getId() == R.id.logInIcon)
			{
				//	Bold the text
				logInText.setTypeface(null, Typeface.BOLD);

				// Start the Log In Activity
				startActivity(new Intent(RegisterActivity.this, LogInActivity.class));

				//	End current activity so the screen will not come back when back button pressed
				finish();
			}

			//	If clicked widget is the register button
			if (view.getId() == R.id.btn_register)
			{
				//	Remove focus from input field and remove soft input
				cl.requestFocus();

				//	Check for input data validation
				//	Full name minimum length - 5
				//	E-mail minimum length - 8
				//	Student ID minimum length - 10
				//	Password minimum length - 6

				//	Check for errors and set them
				if (editTextFullName.getText().toString().isEmpty())
				{
					MyTextInputLayoutFullName.setError("Please enter your full name");
				}
				else if (editTextFullName.getText().toString().trim().length() < 5)
				{
					MyTextInputLayoutFullName.setError("Please check your name");
				}

				if (editTextEmail.getText().toString().isEmpty())
				{
					MyTextInputLayoutEmail.setError("Please enter your email");
				}
				else if (editTextEmail.getText().toString().trim().length() < 8)
				{
					MyTextInputLayoutEmail.setError("Please check your email");
				}
				else
				{
					if (!validateEmail(editTextEmail.getText().toString().trim()))
					{
						MyTextInputLayoutEmail.setError("Please check your email");
					}
				}
				
				if (editTextStudentID.getText().toString().isEmpty())
				{
					MyTextInputLayoutStudentID.setError("Please enter your student ID");
				}
				else if (editTextStudentID.getText().toString().trim().length() < 10)
				{
					MyTextInputLayoutStudentID.setError("Please check your student ID");
				}

				if (editTextPassword.getText().toString().isEmpty())
				{
					MyTextInputLayoutPassword.setError("Please enter a password");
				}
				else if (editTextPassword.getText().toString().trim().length() < 6)
				{
					MyTextInputLayoutPassword.setError("Password length must at least 6 characters long!");
				}

				//	If all input data verified, register the user
				if (editTextFullName.getText().toString().trim().length() >= 5 && editTextEmail.getText().toString().trim().length() >= 10 && editTextStudentID.getText().toString().trim().length() >= 10 && editTextPassword.getText().toString().trim().length() >= 6 && validateEmail(editTextEmail.getText().toString().trim()))
				{
					//	Register the user
					//	Disable the widgets
					button.setEnabled(false);
					editTextFullName.setEnabled(false);
					editTextEmail.setEnabled(false);
					editTextStudentID.setEnabled(false);
					editTextPassword.setEnabled(false);
					logInText.setEnabled(false);
					logInIcon.setEnabled(false);

					//	Display progress bar
					//progressBar = new ProgressBar(RegisterActivity.this);
					progressBar = findViewById(R.id.progressBar);
					progressBar.setVisibility(View.VISIBLE);
					progressBar.animate();

					// New object for VolleyActivity class
					VolleyActivity volleyActivity = new VolleyActivity();

					Map<String, String> params = new HashMap<>();
					params.put("full_name", editTextFullName.getText().toString().trim());
					params.put("email", editTextEmail.getText().toString().trim());
					params.put("student_id", editTextStudentID.getText().toString().trim());
					params.put("password_mmuone", editTextPassword.getText().toString().trim());

					volleyActivity.setParams(params);
					volleyActivity.volleyJsonObjectRequest(RegisterActivity.this.registerURL, view.getContext(), 1);
				}
			}
		}
	}

	//	Class for OnTouchListener events
	private class registerTouchListener implements View.OnTouchListener
	{
		//	Rect or Rectangle records positions to detect the motion or position of cursor
		//	whether it is still hovering or not from a widget
		Rect rect;

		@Override
		public boolean onTouch(View view, MotionEvent event)
		{
			TextView logInText = findViewById(R.id.logInText);

			//	If touched widget is sign up text or icon
			if (view.getId() == R.id.logInText || view.getId() == R.id.logInIcon)
			{
				//	For touch event that is action down or "mouse down"
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					//	Set font weight to bold
					logInText.setTypeface(null, Typeface.BOLD);

					//	Record the rectangle position
					rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
				}

				//	If the touch event is move and the rect or "cursor" is not hovering the widget
				if (event.getAction() == MotionEvent.ACTION_MOVE && !rect.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY()))
				{
					//	Set font weight to normal
					logInText.setTypeface(null, Typeface.NORMAL);
				}

				//	If the touch event is up and inside widget region, perform a click
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					//	Set font weight to normal
					logInText.setTypeface(null, Typeface.NORMAL);
				}
			}

			return false;
		}
	}

	//	Class for OnFocusChangeListener events
	private class noInputFocusChangeListener implements View.OnFocusChangeListener
	{
		@Override
		public void onFocusChange(View view, boolean b)
		{
			MyTextInputLayout MyTextInputLayoutFullName = findViewById(R.id.input_layout_fullName);
			MyTextInputLayout MyTextInputLayoutEmail = findViewById(R.id.input_layout_email);
			MyTextInputLayout MyTextInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
			MyTextInputLayout MyTextInputLayoutPassword = findViewById(R.id.input_layout_password);
			EditText editTextEmail = findViewById(R.id.input_email);
			EditText editTextStudentID = findViewById(R.id.input_studentID);
			EditText editTextPassword = findViewById(R.id.input_password);

			//	If focused and it is the register button or the constraint layout
			if (b && view.getId() == R.id.constraintLayout)
			{
				//This will hide the keyboard or soft input
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null)
				{
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}

			//	If the focused or unfocused widget is the password input field
			if (view.getId() == R.id.input_password)
			{
				if (b)
				{
					// Enable the password visibility toggle if the input is focused
					MyTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
				}
				else
				{
					//	Remove the password visibility toggle if focus is not on password field
					MyTextInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
				}
			}

			if (!b && (view.getId() == R.id.input_fullName || view.getId() == R.id.input_email || view.getId() == R.id.input_studentID || view.getId() == R.id.input_password))
			{
				switch (view.getId())
				{
					case R.id.input_fullName:
						MyTextInputLayoutFullName.setError(null);
						break;
					case R.id.input_email:
						CharSequence inputStr = editTextEmail.getText().toString().trim();
						if (!validateEmail(inputStr) && (inputStr.length() > 0 && inputStr.length() < 8))
						{
							MyTextInputLayoutEmail.setError("Please check your email");
						}
						else
						{
							MyTextInputLayoutEmail.setError(null);
						}
						break;
					case R.id.input_studentID:
						if (editTextStudentID.getText().length() < 10 && editTextStudentID.getText().length() > 0)
						{
							MyTextInputLayoutStudentID.setError("Please check your student ID");
						}
						else
						{
							MyTextInputLayoutStudentID.setError(null);
						}
						break;
					case R.id.input_password:
						if (editTextPassword.getText().length() < 6 && editTextPassword.getText().length() > 0)
						{
							MyTextInputLayoutPassword.setError("Password length must at least 6 characters long!");
						}
						else
						{
							MyTextInputLayoutPassword.setError(null);
						}
						break;
					default:
						//	nothing =)
				}
			}
		}
	}

	private class onTextWatchChanged implements TextWatcher
	{
		@Override
		public void onTextChanged(CharSequence charSequence, int start, int before, int count)
		{
			if (!charSequence.toString().isEmpty())
			{
				//	Get widgets
				final MyTextInputLayout myTextInputLayoutEmail = findViewById(R.id.input_layout_email);
				final MyTextInputLayout myTextInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
				final MyTextInputLayout myTextInputLayoutFullName = findViewById(R.id.input_layout_fullName);
				final MyTextInputLayout myTextInputLayoutPassword = findViewById(R.id.input_layout_password);
				final EditText editTextEmail = findViewById(R.id.input_email);
				final EditText editTextStudentID = findViewById(R.id.input_studentID);
				final EditText editTextFullName = findViewById(R.id.input_fullName);
				final EditText editTextPassword = findViewById(R.id.input_password);
				final Button button = findViewById(R.id.btn_register);
				final TextView logInText = findViewById(R.id.logInText);
				final ImageView logInIcon = findViewById(R.id.logInIcon);
				final ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

				try
				{
					//	Put reply response into class variable 'reply'
					RegisterActivity.this.reply = new JSONObject(charSequence.toString());

					//	Hide Progress bar
					progressBar.setVisibility(View.GONE);

					//	Check status
					if (reply.getString("status").contains("succeed"))
					{
						//	If registration succeeded

						//	Remove widgets
						myTextInputLayoutEmail.setVisibility(View.GONE);
						myTextInputLayoutStudentID.setVisibility(View.GONE);
						myTextInputLayoutFullName.setVisibility(View.GONE);
						myTextInputLayoutPassword.setVisibility(View.GONE);
						button.setVisibility(View.GONE);
						logInText.setVisibility(View.GONE);
						logInIcon.setVisibility(View.GONE);

						//	Setup animation
						final Animation animationFadeInFast = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_fast);

						//	Set drawable XML and ImageView
						final Drawable drawableTick = ContextCompat.getDrawable(RegisterActivity.this, R.drawable.register_to_tick);
						final ImageView imageViewTick = new ImageView(RegisterActivity.this);

						//	Generate unique ID for ImageView to set constraint later
						imageViewTick.setId(View.generateViewId());

						//	Set ImageView's image to be at center
						imageViewTick.setScaleType(ImageView.ScaleType.CENTER);

						//	Set the drawable above to this ImageView
						imageViewTick.setImageDrawable(drawableTick);

						//	Set the constraint for the ImageView to our parent Constraint Layout
						ConstraintSet constraintSet = new ConstraintSet();
						constraintSet.connect(imageViewTick.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
						constraintSet.connect(imageViewTick.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
						constraintSet.connect(imageViewTick.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
						constraintSet.connect(imageViewTick.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

						//	Set the Constraint Set to the Constraint Layout
						constraintLayout.setConstraintSet(constraintSet);

						//	Add the ImageView to the Constraint Layout
						constraintLayout.addView(imageViewTick);

						//	Start the animation
						imageViewTick.startAnimation(animationFadeInFast);

						//	Animation Start, End, Repeat listener
						animationFadeInFast.setAnimationListener(new Animation.AnimationListener()
						{
							@Override
							public void onAnimationStart(Animation animation)
							{

							}

							@Override
							public void onAnimationEnd(Animation animation)
							{
								//	Animation time: 1000
								//	Display on screen time: 1000
								int animationWaitTime = 2000;

								//	On Fade In animation ends, start the register to tick animation
								((Animatable) drawableTick).start();

								//	When animation wait time ends, execute the run method
								constraintLayout.postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
										finish();
									}
								}, animationWaitTime);

								//	TODO - prevent ddos of registration, timeout or re-captcha
								//	TODO - fix drawableTick bug, if register and tab out,
								//	then come back again and register, the drawable
								//	will start with a tick before changing to register icon
							}

							@Override
							public void onAnimationRepeat(Animation animation)
							{

							}
						});
					}
					else if (reply.getString("status").contains("failed"))
					{
						//	If registration failed
						//	Error Code:
						//	10621	-	DUPLICATE ENTRY FOR EMAIL
						//	10622	-	DUPLICATE ENTRY FOR STUDENT ID
						//	10623	-	DUPLICATE ENTRY FOR EMAIL AND STUDENT ID

						//	Get error from error code
						switch (reply.getInt("code"))
						{
							case 10621:
								myTextInputLayoutEmail.setError("Email already exist. Have you registered?");
								editTextEmail.requestFocus();
								break;
							case 10622:
								myTextInputLayoutStudentID.setError("Student ID already exist. Have you registered?");
								editTextStudentID.requestFocus();
								break;
							case 10623:
								myTextInputLayoutEmail.setError("Email already exist. Have you registered?");
								myTextInputLayoutStudentID.setError("Student ID already exist. Have you registered?");
								break;
							default:
								myTextInputLayoutStudentID.setError("FATAL ERROR: NETWORK ERROR");
						}

						//	TODO timeout on brute force registration
						//	TODO Set IP and phone model

						//	Enable the widgets
						button.setEnabled(true);
						editTextFullName.setEnabled(true);
						editTextEmail.setEnabled(true);
						editTextStudentID.setEnabled(true);
						editTextPassword.setEnabled(true);
						logInText.setEnabled(true);
						logInIcon.setEnabled(true);
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

	//	Email validation with regex expression
	private boolean validateEmail(CharSequence inputStr)
	{
		//	^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$
		//	Regex for email
		String expression = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})?";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		return matcher.matches();
	}
}