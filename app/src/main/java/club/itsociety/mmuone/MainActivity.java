package club.itsociety.mmuone;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
	private Drawer result = null;
	String token = "";
	String studentID = String.valueOf(UserData.studentID);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//	Get the toolbar from the XML file
		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);

		//	Set the action bar
		setSupportActionBar(toolbar);

		//	Get the toolbar and set the title
		getSupportActionBar().setTitle("MMU One");

		//	Create the AccountHeader
		AccountHeader headerResult = new AccountHeaderBuilder()
				.withActivity(this)
				.withHeaderBackground(R.drawable.header)
				.addProfiles(
						//	Profile details
						new ProfileDrawerItem().withName("Timothy Lam").withEmail("timothylam96@gmail.com").withIcon(ContextCompat.getDrawable(this, R.drawable.header))
				)
				.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
					@Override
					public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
						return false;
					}
				})
				.build();

		//	Items to be displayed at the drawer
		//	If you want to update the items at a later time it is recommended to keep it in a variable
		PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
		PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("MMU Portal");

		//	Create the drawer and remember the `Drawer` result object
		this.result = new DrawerBuilder(this)
				.withRootView(R.id.drawer_container)
				.withToolbar(toolbar)
				.withDisplayBelowStatusBar(true)
				.withActionBarDrawerToggleAnimated(true)
				.withAccountHeader(headerResult)
				.addDrawerItems(
						item1, // case 1
						new DividerDrawerItem(), // case 2
						item2, // case 3
						new SecondaryDrawerItem().withName("Setting"), // case 4
						new PrimaryDrawerItem().withName("Check Update"), // case 5
						new PrimaryDrawerItem().withName("Get Update") // case 6
				)
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
				{
					//	When the drawer buttons are clicked
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
					{
						//	Do something with the clicked item :D
						//	Use a switch to control it
						//	Position integer is the identifier set previously at above with items
						//	For each cases it should be calling other activity with intend
						//	But don't use finish() after starting the activity
						//	So when the user presses back button, it will come back to this screen

						VolleyActivity volleyActivity = new VolleyActivity();

						switch (position)
						{
							case 3:
								//	Get the data into a Hash Map
								Map<String, String> params = new HashMap<>();
								//params.put("student_id", "1144400444");

								// New object for VolleyActivity class for network request

								// Reset token
								MainActivity.this.token = Integer.toString(0);
								//volleyActivity.setParams(params);
								String loginURL = "https://www.mmuone.com/api/portal/login.php?student_id=" + studentID;
								volleyActivity.volleyJsonObjectRequest(loginURL, view.getContext(), 0);
								break;
							case 4:
								//	Get full name
								loginURL = "https://www.mmuone.com/api/portal/getFullName.php?student_id=" + studentID;
								volleyActivity.volleyJsonObjectRequest(loginURL, view.getContext(), 0);
								break;
							case 5:
								//	Check update
								loginURL = "https://www.mmuone.com/api/portal/checkUpdate.php?student_id=" + studentID + "&tab=1";
								volleyActivity.volleyJsonObjectRequest(loginURL, view.getContext(), 0);
								break;
							case 6:
								//	Get update: GET: tab, student_id, cookie, token
								loginURL = "https://www.mmuone.com/api/portal/getUpdate.php?tab=1&student_id=" + studentID + "&force_update=1&token=" + MainActivity.this.token + "&hash=";
								volleyActivity.volleyJsonObjectRequest(loginURL, view.getContext(), 0);
								break;
							default:
								//Log.i("int position", Integer.toString(position));
								//startActivity(new Intent(MainActivity.this, LogInActivity.class));

						}
						return false;
					}
				})
				.withSavedInstance(savedInstanceState)
				.build();

		//	Just to use the drawer once to remove warning
		result.closeDrawer();


		TextWatcher textWatcher = new onTextWatchChanged();
		TextView textViewVolley = findViewById(R.id.textViewVolley);
		textViewVolley.addTextChangedListener(textWatcher);

		//	TODO: This is the main window. Have a social place for students to post
		//	TODO: From this page they can navigate to other places using the drawer
		//	TODO: Record login session after user successfully logged in
	}

	//	Overriding onSaveInstanceState
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		//	Add the values which need to be saved from the drawer to the bundle
		outState = this.result.saveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	//	Overriding pressing back button method
	//	If the drawer is not close, close it or else perform a normal back button action
	@Override
	public void onBackPressed()
	{
		//	Handle the back press :D close the drawer first and if the drawer is closed close the activity
		if (this.result != null && this.result.isDrawerOpen())
		{
			result.closeDrawer();
		}
		else
		{
			super.onBackPressed();
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
				final TextView firstText = findViewById(R.id.firstText);
				boolean messageArray = true;
				try
				{
					//	Put reply response into class variable 'reply'
					JSONObject reply = new JSONObject(charSequence.toString());

					//	Check if message is json object array
					if (reply.optJSONObject("message") != null)
					{
						messageArray = true;

					}
					else
					{
						messageArray = false;
					}

					//	Check status
					if (reply.getString("status").contains("1"))
					{
						if (messageArray)
						{
							JSONObject messageObject = reply.optJSONObject("message");
							firstText.setText("login succeeded" + "token: " + (messageObject.optString("token")));

							if (messageObject.optString("token") != null)
							{
								MainActivity.this.token = messageObject.optString("token");
							}

							if (messageObject.optInt("hasPage") == 0)
							{
								firstText.setText(firstText.getText() + "\nNo more pages!");
							}
						}
						else
						{
							firstText.setText(reply.getString("message"));
						}
					}
					else if (reply.getString("status").contains("0"))
					{
						firstText.setText("faillllled");
					}
					else
					{
						firstText.setText("unknown");
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
