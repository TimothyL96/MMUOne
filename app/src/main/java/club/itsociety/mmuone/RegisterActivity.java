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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity
{
	VolleyActivity volleyActivity = new VolleyActivity();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		//	Create events listener
		View.OnClickListener registerClickListener = new registerClickListener();
		View.OnTouchListener registerTouchListener = new registerTouchListener();
		View.OnFocusChangeListener noInputFocusListener = new noInputFocusChangeListener();

		//	Create widgets objects
		TextView logInText = findViewById(R.id.logInText);
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

		//	Gradient Background Animation
		//	Enter Fade Time: 500
		//	Exit Fade Time: 1200
		AnimationDrawable animationDrawable = (AnimationDrawable) cl.getBackground();
		animationDrawable.setEnterFadeDuration(500);
		animationDrawable.setExitFadeDuration(1200);
		animationDrawable.start();
	}

	//	Class for OnClickListener events
	private class registerClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			TextView logInText = findViewById(R.id.logInText);
			TextInputLayout textInputLayoutFullName = findViewById(R.id.input_layout_fullName);
			TextInputLayout textInputLayoutEmail = findViewById(R.id.input_layout_email);
			TextInputLayout textInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
			TextInputLayout textInputLayoutPassword = findViewById(R.id.input_layout_password);
			EditText editTextFullName = findViewById(R.id.input_fullName);
			EditText editTextEmail = findViewById(R.id.input_email);
			EditText editTextStudentID = findViewById(R.id.input_studentID);
			EditText editTextPassword = findViewById(R.id.input_password);
			ConstraintLayout cl = findViewById(R.id.constraintLayout);
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
				//	Full name minimum length - 3
				//	E-mail minimum length - 3
				if (editTextFullName.getText().toString().isEmpty())
				{
					textInputLayoutFullName.setError("Please enter your full name");
				}
				else if (editTextFullName.getText().toString().trim().length() < 3)
				{
					textInputLayoutFullName.setError("Please check your name");
				}

				if (editTextEmail.getText().toString().isEmpty())
				{
					textInputLayoutEmail.setError("Please enter your email");
				}
				else if (editTextEmail.getText().toString().trim().length() < 10)
				{
					textInputLayoutEmail.setError("Please check your email");
				}
				else
				{
					if (!validateEmail(editTextEmail.getText().toString().trim()))
					{
						textInputLayoutEmail.setError("Please check your email");
					}
				}

				if (editTextStudentID.getText().toString().isEmpty())
				{
					textInputLayoutStudentID.setError("Please enter your student ID");
				}
				else if (editTextStudentID.getText().toString().trim().length() < 10)
				{
					textInputLayoutStudentID.setError("Please check your student ID");
				}

				if (editTextPassword.getText().toString().isEmpty())
				{
					textInputLayoutPassword.setError("Please enter a password");
				}
				else if (editTextPassword.getText().toString().trim().length() < 6)
				{
					textInputLayoutPassword.setError("Password length must at least 6 characters long!");
				}

				//	If all input data verified, register the user
				if (editTextFullName.getText().toString().trim().length() >= 3 && editTextEmail.getText().toString().trim().length() >= 10 && editTextStudentID.getText().toString().trim().length() >= 10 && editTextPassword.getText().toString().trim().length() >= 6 && validateEmail(editTextEmail.getText().toString().trim()))
				{
					//	TODO register the user
					Map<String, String> params = new HashMap<>();
					params.put("full_name", editTextFullName.getText().toString().trim());
					params.put("email", editTextEmail.getText().toString().trim());
					params.put("student_id", editTextStudentID.getText().toString().trim());
					params.put("password_mmuone", editTextPassword.getText().toString().trim());
					volleyActivity.setParams(params);
					volleyActivity.volleyJsonObjectRequest("https://mmuone.com/api/users/create.php", view.getContext());
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
			TextInputLayout textInputLayoutFullName = findViewById(R.id.input_layout_fullName);
			TextInputLayout textInputLayoutEmail = findViewById(R.id.input_layout_email);
			TextInputLayout textInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
			TextInputLayout textInputLayoutPassword = findViewById(R.id.input_layout_password);
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
					textInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
				}
				else
				{
					//	Remove the password visibility toggle if focus is not on password field
					textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
				}
			}

			if (!b && (view.getId() == R.id.input_fullName || view.getId() == R.id.input_email || view.getId() == R.id.input_studentID || view.getId() == R.id.input_password))
			{
				switch (view.getId())
				{
					case R.id.input_fullName:
						textInputLayoutFullName.setError(null);
						break;
					case R.id.input_email:
						CharSequence inputStr = editTextEmail.getText().toString().trim();
						if (!validateEmail(inputStr) || (inputStr.length() > 0 && inputStr.length() < 10))
						{
							textInputLayoutEmail.setError("Please check your email");
						}
						else
						{
							textInputLayoutEmail.setError(null);
						}
						break;
					case R.id.input_studentID:
						if (editTextStudentID.getText().length() < 10 && editTextStudentID.getText().length() > 0)
						{
							textInputLayoutStudentID.setError("Please check your student ID");
						}
						else
						{
							textInputLayoutStudentID.setError(null);
						}
						break;
					case R.id.input_password:
						if (editTextPassword.getText().length() < 6 && editTextPassword.getText().length() > 0)
						{
							textInputLayoutPassword.setError("Password length must at least 6 characters long!");
						}
						else
						{
							textInputLayoutPassword.setError(null);
						}
						break;
					default:
						//	nothing =)
				}
			}
		}
	}

	//	Email validation with regex expression
	private boolean validateEmail(CharSequence inputStr)
	{
		//	^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$
		String expression = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})?";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		return matcher.matches();
	}
}