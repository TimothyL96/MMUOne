package club.itsociety.mmuone;

import android.graphics.drawable.Drawable;
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

		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("MMU One");

		// Create the AccountHeader
		AccountHeader headerResult = new AccountHeaderBuilder()
				.withActivity(this)
				.withHeaderBackground(R.drawable.header)
				.addProfiles(
						new ProfileDrawerItem().withName("Timothy Lam").withEmail("timothylam96@gmail.com").withIcon(ContextCompat.getDrawable(this, R.drawable.header))
				)
				.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
					@Override
					public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
						return false;
					}
				})
				.build();

		//if you want to update the items at a later time it is recommended to keep it in a variable
		PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
		PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("MMU Portal");

		//create the drawer and remember the `Drawer` result object
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
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						// do something with the clicked item :D
						return false;
					}
				})
				.withSavedInstance(savedInstanceState)
				.build();

		result.closeDrawer();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		//	add the values which need to be saved from the drawer to the bundle
		outState = this.result.saveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

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
