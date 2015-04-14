package is.arnastofnun.beygdu;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.HashMap;

import is.arnastofnun.DB.DBController;

/**
 * @author Jón Friðrik
 * @since 05.03.15
 * @version 1.0
 *
 * StatisticsActivity contains (will contain) various methods to show the user
 * statistics of his former searches.
 *
 */

public class StatisticsActivity extends NavDrawer {

    private Typeface LatoLight;

    /**
     * Creates the activity and fetches statistics from the database. Then constructs a pieChart
     * with MPAndroid library (see link below).
     *
     * @see <a href="https://github.com/PhilJay/MPAndroidChart"</a>
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Not setting the content view here since we are
         * inflating it in the NavDrawer (see below)
         */
//        setContentView(R.layout.activity_statistics);

        /**
         * Inflate the layout into the NavDrawer layout
         * where `frameLayout` is a FrameLayout in the layout for the
         * NavDrawer (see file nav_base_layout)
         */
        getLayoutInflater().inflate(R.layout.activity_statistics, frameLayout);

        /**
         * Setting what item is checked
         */
        mDrawerList.setItemChecked(position,true);

        PieChart chart = (PieChart) findViewById(R.id.chart);

        DBController controller = new DBController(this);
        HashMap<String, Integer> stats = controller.fetchAllStats();

        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        int i = 0;
        int sum = 0;
        for(HashMap.Entry<String, Integer> entry : stats.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if(value != 0) {
                entries.add(new Entry(value, i++));
                labels.add(key);
                sum = sum + value;
            }
        }

        LatoLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        PieDataSet data = new PieDataSet(entries, "");

        data.setColors(new int[]{Color.parseColor("#669900"), Color.parseColor("#FF5722"), Color.parseColor("#2196F3"), Color.parseColor("#5bc0de"), Color.parseColor("#f0ad4e")});
        data.setValueTextSize(15);

        data.setValueTypeface(LatoLight);
        chart.setData(new PieData(labels, data));
        chart.setCenterText("Orðflokkar");
        chart.setDescription("Fjöldi leita: " + sum);
        chart.setCenterTextSize(30);
        chart.setCenterTextTypeface(LatoLight);
        chart.invalidate();
        chart.animateY(1500);

        setTitle(R.string.title_activity_statistics);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
