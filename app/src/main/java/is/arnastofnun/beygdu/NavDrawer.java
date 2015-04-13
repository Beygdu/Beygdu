package is.arnastofnun.beygdu;


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

import java.util.ArrayList;
import java.util.Collections;

import is.arnastofnun.BeygduTutorial.TutorialActivity;


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
     * List item array for navigation drawer items. This array will be populated
     * from a String Array in strings.xml
     */
    protected ArrayList<String> navArray;

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
    protected DrawerLayout mDrawerLayout;

    /**
     * Drawer listener class for drawer open, close, etc.
     */
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Set the layout
         */
        setContentView(R.layout.nav_base_layout);

        /**
         * attach variables to items in the loaded layout
         */
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        /**
         * retrieve the array of strings from strings.xml and load it into
         * an arraylist because we want to be able to add and remove from this
         * list dynamically
         */
        navArray = new ArrayList<>();
        Collections.addAll(navArray, getResources().getStringArray(R.array.navdrawer_items));


        /**
         * populate the list for the navigation drawer and attach a
         * click listener for when an item is selected
         */
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.nav_drawer_item,R.id.navDrawerItem,navArray));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(position);
            }
        });

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
                getActionBar().setTitle(navArray.get(position));
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
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        /**
         * TODO: Use .syncState() to properly display the 'hamburger' icon for the navDrawer
         * actionBarDrawerToggle.syncState();
         */
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        /**
         * enable ActionBar app icon to behave as a button to toggle navigation drawer
         */

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


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
     * @param position position in the list of items in the navigation drawer
     */
    protected void openActivity(int position){
        mDrawerLayout.closeDrawer(mDrawerList);
        // Set the position so we can access it from child activities
        NavDrawer.position = position;

        switch (position) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                activityAnimationTransition();
                break;
            case 1:
                startActivity(new Intent(this, Cache.class));
                activityAnimationTransition();
                break;
            case 2:
                startActivity(new Intent(this, AboutActivity.class));
                activityAnimationTransition();
                break;
            case 3:
                startActivity(new Intent(this, StatisticsActivity.class));
                activityAnimationTransition();
                break;
            case 4:
                startActivity(new Intent(this, AuthorActivity.class));
                activityAnimationTransition();
                break;
            case 5:
                startActivity(new Intent(this, MapsActivity.class));
                activityAnimationTransition();
                break;
            case 6:
                startActivity(new Intent(this, TutorialActivity.class));
                activityAnimationTransition();
                break;
        }
    }

    /**
     * Handles the animation when the app is transitioning between activities
     */
    public void activityAnimationTransition(){
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer
        // ActionBarDrawerToggle will take care of this

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){

            switch (item.getItemId()){
                case R.id.home:
                    onBackPressed();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /*
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
        */

        super.onBackPressed();
    }
}
