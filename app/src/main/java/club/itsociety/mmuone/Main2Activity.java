package club.itsociety.mmuone;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class Main2Activity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("MMU One");
		// Create the AccountHeader
		AccountHeader headerResult = new AccountHeaderBuilder()
				.withActivity(this)
				.withHeaderBackground(R.drawable.header)
				.addProfiles(
						new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(ContextCompat.getDrawable(Main2Activity.this, R.drawable.header))
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
		PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Timetable");
		SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("Settings");

		//create the drawer and remember the `Drawer` result object
		final Drawer result = new DrawerBuilder()
				.withActivity(this)
				.withAccountHeader(headerResult)
				.withToolbar(toolbar)
				.addDrawerItems(
						item1,
						item2,
						new DividerDrawerItem(),
						item3,
						new SecondaryDrawerItem().withName("Setting")
				)
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						// do something with the clicked item :D
						switch (position)
						{
							case 1:
								break;
							case 2:
								break;
							default:
						}
						return true;
					}
				})
				.build();

		result.closeDrawer();
	}
}
