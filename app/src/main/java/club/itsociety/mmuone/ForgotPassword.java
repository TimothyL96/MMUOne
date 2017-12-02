package club.itsociety.mmuone;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPassword extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);

		//	Create events listener
		View.OnClickListener onClickListener = new OnClickListener();
		View.OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener();

		//	Create widgets objects
		Button btn_forgotPassword = findViewById(R.id.btn_forgotPassword);
		ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
		EditText editTextFullName= findViewById(R.id.input_fullName);
		EditText editTextStudentID = findViewById(R.id.input_studentID);

		//	Set the widget object to event listener
		//	OnClickListener
		btn_forgotPassword.setOnClickListener(onClickListener);
		btn_forgotPassword.setOnFocusChangeListener(onFocusChangeListener);

		//	OnFocusChangeListener
		constraintLayout.setOnFocusChangeListener(onFocusChangeListener);
		editTextFullName.setOnFocusChangeListener(onFocusChangeListener);
		editTextStudentID.setOnFocusChangeListener(onFocusChangeListener);
	}

	//	Class for OnClickListener events
	private class OnClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			//	Create widgets objects
			EditText editTextFullName= findViewById(R.id.input_fullName);
			EditText editTextStudentID = findViewById(R.id.input_studentID);
			TextInputLayout textInputLayoutFullName = findViewById(R.id.input_layout_fullName);
			TextInputLayout textInputLayoutStudentID = findViewById(R.id.input_layout_studentID);
			ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

			if (view.getId() == R.id.btn_forgotPassword)
			{
				//	Remove all focus and remove soft input keyboard by focusing the layout
				constraintLayout.requestFocus();


				//	Check Full Name length and empty
				//	isEmpty does not check for empty spaces so no need for trim()
				//	Full name at least 3 characters
				//	Student ID at least 10 numbers
				if (editTextFullName.getText().toString().isEmpty())
				{
					textInputLayoutFullName.setError("Please enter your name");
				}
				else if (editTextFullName.getText().toString().trim().length() < 3)
				{
					textInputLayoutFullName.setError("Please check your name");
				}

				//	Check student ID length and empty
				if (editTextStudentID.getText().toString().isEmpty())
				{
					textInputLayoutStudentID.setError("Please enter your student ID");
				}
				else if (editTextStudentID.getText().toString().trim().length() < 10)
				{
					textInputLayoutStudentID.setError("Please check your student ID");
				}

				//	If all input data verified, proceed with password recovery
				if (textInputLayoutFullName.getError() == null && textInputLayoutStudentID.getError() == null)
				{
					//	TODO reset the user's password
				}
			}
		}
	}

	//	Class for OnFocusChangeListener
	private class OnFocusChangeListener implements View.OnFocusChangeListener
	{
		@Override
		public void onFocusChange(View view, boolean b)
		{
			//	Create widgets objects
			EditText editTextFullName= findViewById(R.id.input_fullName);
			EditText editTextStudentID = findViewById(R.id.input_studentID);
			TextInputLayout textInputLayoutFullName = findViewById(R.id.input_layout_fullName);
			TextInputLayout textInputLayoutStudentID = findViewById(R.id.input_layout_studentID);

			//	If focused and it is the recover password button or the constraint layout
			if (b && (view.getId() == R.id.constraintLayout))
			{
				//This will hide the keyboard or soft input
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null)
				{
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}

			//	If focused or lost focused widget is fullName
			if (view.getId() == R.id.input_fullName)
			{
				if (!b)
				{
					if (!editTextFullName.getText().toString().isEmpty() && editTextFullName.getText().toString().trim().length() >= 3)
					{
						//	Remove error
						textInputLayoutFullName.setError(null);
					}
				}
			}

			//	If focused or lost focused widget is password
			if (view.getId() == R.id.input_studentID)
			{
				if (!b)
				{
					if (!editTextStudentID.getText().toString().isEmpty() && editTextStudentID.getText().toString().trim().length() >= 10)
					{
						//	Remove error
						textInputLayoutStudentID.setError(null);
					}
				}
			}
		}
	}
}
