package is.arnastofnun.beygdu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;


public class StatisticsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        PieChart chart = (PieChart) findViewById(R.id.chart);

        ArrayList<String> testNames = new ArrayList<String>();
        testNames.add("NO");
        testNames.add("LO");
        testNames.add(("SO"));

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        Entry entry = new Entry(0, 10);
        Entry entry2 = new Entry(0, 20);
        Entry entry3 = new Entry(0, 87);

        yVals.add(entry);
        yVals.add(entry2);
        yVals.add(entry3);

        PieDataSet data = new PieDataSet(yVals, "label");
        chart.setData(new PieData());

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
