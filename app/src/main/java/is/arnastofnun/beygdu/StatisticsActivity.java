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
import java.util.HashMap;
import java.util.Map;

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

        PieDataSet data = new PieDataSet(entries, "");

        data.setColors(new int[] {Color.parseColor("#428bca"), Color.parseColor("#5cb85c"), Color.parseColor("#d9534f"), Color.parseColor("#5bc0de"), Color.parseColor("#f0ad4e")});
        chart.setData(new PieData(labels, data));
        chart.setCenterText("Orðflokkar");
        chart.setDescription("Fjöldi leita: " + sum);
        chart.invalidate();
        chart.animateY(1500);
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
