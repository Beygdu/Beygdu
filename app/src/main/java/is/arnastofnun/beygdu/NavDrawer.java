package is.arnastofnun.beygdu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Daniel Pall Johannsson
 * @since 06.03.2015
 * @version 1.0
 *
 * NavDrawer handles the navigation drawer and its functions.
 *
 * All other activities in the application will extend this class, thus enabling
 * the navigation drawer on all activities without repeating code for each activity.
 *
 * This activity has a layout that contains a frame layout, which we will inflate the
 * child activity layout into.
 */
public class NavDrawer extends FragmentActivity{

    /**
     * Frame layout: Used as a parent layout for the child activity
     */
    protected FrameLayout frameLayout;

    /**
     * Listview to add navigation drawer items
     */
    protected ListView mDrawerList;

    /**
     * List item array for navigation drawer items
     * TODO: Get Activity names from strings.xml. This is just for a test now
     */
    protected String[] listArray = {"Item 1","Item 2","Item 3"};

    /**
     * Static variable for selected item position in the navigation drawer.
     * Used to know which item is selected from the navigation drawer.
     */
    protected static int position;

    /**
     * Check if the launcher is being called for the first time so that we can start
     * the appropriate activity
     */
    private static boolean isLaunch = true;

    /**
     * Base layout of this activity
     */
    private DrawerLayout mDrawerLayout;

    /**
     * Drawer listener class for drawer open, close, etc.
     */
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout
        setContentView(R.layout.nav_base_layout);

        // "Connect" to the layout
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_drawer_item,R.id.navDrawerItem,listArray));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        /**
         * ActionbarDrawerToggle ties together the proper interactions
         * between the nav drawer and the action bar
         */
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.open_drawer,
                R.string.close_drawer)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        /**
         * Since NavDrawer is the Launcher Activity, if it is launching for the first time in the apps
         * lifecycle we want it to start the correct activity.
         * This is why whe check the isLaunch boolean variable
         */
        if(isLaunch){
            isLaunch = false;
            openActivity(0);
        }
    }

    /**
     * Launching respective activity when selected list item is clicked
     * @param position
     */
    protected void openActivity(int position){
        mDrawerLayout.closeDrawer(mDrawerList);
        // Set the position so we can access it from child activities
        NavDrawer.position = position;

        switch (position) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        Toast.makeText(this, "You selected item: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer
        // ActionBarDrawerToggle will take care of this
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
//        super.onBackPressed();
    }
}
