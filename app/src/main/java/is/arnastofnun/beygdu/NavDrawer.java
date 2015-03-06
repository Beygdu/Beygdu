package is.arnastofnun.beygdu;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ListView;

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
public class NavDrawer {

    private String[] drawerListViewItems;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;



}
