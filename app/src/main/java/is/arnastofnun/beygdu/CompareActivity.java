package is.arnastofnun.beygdu;

import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import is.arnastofnun.DB.DBController;
import is.arnastofnun.beygdu.R;
import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.Tables;
import is.arnastofnun.utils.BeygduUtilities;
import is.arnastofnun.utils.TableFragment;

/**
 * @author Jón Friðrik
 * @since 13.04.15
 * @version 1.0
 *
 * An activity the shows tables which the user has selected to compare. If the user has
 * selected less than 2 tables it shows a textview telling the user to select more tables
 * to see them in the activty. The Activity has one button for clearing the compered tables
 * and also a TableLayout to put the tableFragments in.
 *
 */
public class CompareActivity extends NavDrawer {

    private TableLayout tableLayout;

    //Fonts
    private Typeface LatoBold;
    private Typeface LatoSemiBold;
    private Typeface LatoLight;

    //Screen width
    private float width;
    private float height;

    private ArrayList<Tables> compareTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Not setting the content view here since we are
         * inflating it in the NavDrawer (see below)
         */
        // setContentView(R.layout.activity_about);

        /**
         * Inflate the layout into the NavDrawer layout
         * where `frameLayout` is a FrameLayout in the layout for the
         * NavDrawer (see file nav_base_layout)
         */
        getLayoutInflater().inflate(R.layout.activity_compare, frameLayout);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get screen sizes
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = BeygduUtilities.convertPixelsToDp(size.x, this);
        height = BeygduUtilities.convertPixelsToDp(size.y, this);

        //Set typeface for fonts
        LatoBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        LatoSemiBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Semibold.ttf");
        LatoLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        tableLayout = (TableLayout) findViewById(R.id.data_table);
        drawTables();
    }

    private void drawTables() {
        DBController controller = new DBController(this);
        compareTables = controller.fetchAllComparableWords();

        if(compareTables.size() >= 2) {
            for (Tables table : compareTables) {
                TextView tableTitle = new TextView(this);
                if (320 > width && width < 384) {
                    tableTitle.setTextSize(20);
                } else if (384 > width && width < 600) {
                    tableTitle.setTextSize(28);
                } else if (width > 600) {
                    tableTitle.setTextSize(42);
                }
                tableTitle.setMinHeight(100);
                tableTitle.setText(table.getHeader());
                tableTitle.setTypeface(LatoLight);
                tableTitle.setTextColor(getResources().getColor(R.color.white));
                tableTitle.setPadding(0, 10, 0, 10);

                TableFragment tFragment = new TableFragment(CompareActivity.this, tableLayout, table, table.getTitle());
                getFragmentManager().beginTransaction().add(tableLayout.getId(), tFragment).commit();
            }
        }
        else {
            TableRow tr = new TableRow(this);
            final TextView cell = new TextView(this);
            cell.setText("Ekkert til að bera saman");
            tr.addView(cell);
            tableLayout.addView(tr);
        }
    }

    /**
     * cleares the compareTables list and calls removeAllFromCompareTable method in DBController.
     *
     * @param view
     */
    public void clearBtnOnClick(@SuppressWarnings("unused") View view){
        DBController controller = new DBController(this);
        controller.removeAllFromCompareTable();
        compareTables.clear();
        tableLayout.removeAllViews();
        drawTables();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compare, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
