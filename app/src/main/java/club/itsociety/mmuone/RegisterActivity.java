package club.itsociety.mmuone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		View.OnClickListener registerClickListener = new registerClickListener();
		View.OnTouchListener registerTouchListener = new registerTouchListener();
		View.OnFocusChangeListener noInputFocusListener = new noInputFocusChangeListener();

		TextView logInText = (TextView) findViewById(R.id.logInText);
		logInText.setOnClickListener(registerClickListener);
		logInText.setOnTouchListener(registerTouchListener);

		ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constraintlayout);
		cl.setOnFocusChangeListener(noInputFocusListener);

		Button register = (Button) findViewById(R.id.btn_register);
		register.setOnFocusChangeListener(noInputFocusListener);
		register.setOnClickListener(registerClickListener);

		ImageView logInIcon = (ImageView) findViewById(R.id.logInIcon);
		logInIcon.setOnClickListener(registerClickListener);
		logInIcon.setOnTouchListener(registerTouchListener);
	}

	private class registerClickListener implements View.OnClickListener {

		public void onClick(View view)
		{
			if (view.getId() != R.id.btn_register)
			{
				TextView logInText = (TextView) findViewById(R.id.logInText);
				logInText.setTypeface(null, Typeface.BOLD);
				startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
				finish();
			}

			if (view.getId() == R.id.btn_register)
			{

			}
		}
	}

	private class registerTouchListener implements View.OnTouchListener {

		Rect rect;
		public boolean onTouch(View view, MotionEvent event) {
			TextView logInText = (TextView) findViewById(R.id.logInText);

			if (view.getId() != R.id.btn_register)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					logInText.setTypeface(null, Typeface.BOLD);
					rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
				}

				if (event.getAction() == MotionEvent.ACTION_MOVE && !rect.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY())) {
					logInText.setTypeface(null, Typeface.NORMAL);
				}
			}

			if (view.getId() == R.id.btn_register)
			{
				//Register
			}

			return false;
		}
	}

	private class noInputFocusChangeListener implements View.OnFocusChangeListener {

		public void onFocusChange(View view, boolean b)
		{
			if (b && !(view instanceof TextInputLayout))
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
	}
}
