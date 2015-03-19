package is.arnastofnun.beygdu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import is.arnastofnun.DB.DBController;

/**
 * @author Jón Friðrik
 * @since 05.03.15
 * @version 0.1
 *
 */

public class StatisticsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setDescription(getResources().getString(R.string.pieChart_Desc));

        ArrayList<String> testNames = new ArrayList<String>();
        testNames.add("NO");
        testNames.add("LO");
        testNames.add(("SO"));

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        Entry entry = new Entry(10, 0);
        Entry entry2 = new Entry(34, 1);
        Entry entry3 = new Entry(89, 2);

        yVals.add(entry);
        yVals.add(entry2);
        yVals.add(entry3);

        PieDataSet data = new PieDataSet(yVals, "label");

        data.setColors(new int[] {Color.RED, Color.BLUE, Color.GREEN});

        createDataSet();


        chart.setData(new PieData(testNames, data));
        chart.invalidate();

    }

    private PieDataSet createDataSet() {
        PieDataSet data = null;// = new PieDataSet(yVals, "label");

        //TODO: Connect to DB and fetch Statistics
        DBController controller = new DBController(this);
        ArrayList<Integer> stats = controller.fetchAllStats();

        for(Integer i : stats) {
            Log.v("", ""+i);
        }

        return data;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
