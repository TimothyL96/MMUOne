package club.itsociety.mmuone;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

public class MainActivity extends AppCompatActivity
{
	private Drawer result = null;

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
						item1,
						new DividerDrawerItem(),
						item2,
						new SecondaryDrawerItem().withName("Setting")
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
						switch (position)
						{
							case 1:

								break;
							case 2:
								startActivity(new Intent(MainActivity.this, LogInActivity.class));
								finish();
								break;
							default:
						}
						return false;
					}
				})
				.withSavedInstance(savedInstanceState)
				.build();

		//	Just to use the drawer once to remove warning
		result.closeDrawer();

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
}
