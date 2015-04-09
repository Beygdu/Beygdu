package is.arnastofnun.beygdu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import is.arnastofnun.beygdu.R;

public class AuthorActivity extends NavDrawer {

    //Fonts
    private Typeface LatoBold;
    private Typeface LatoSemiBold;
    private Typeface LatoLight;

    //Screen width
    private float width;
    private float height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Not setting the content view here since we are
         * inflating it in the NavDrawer (see below)
         */
        // setContentView(R.layout.activity_about);

        getLayoutInflater().inflate(R.layout.activity_author, frameLayout);


        // Get screen sizes
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = convertPixelsToDp(size.x);
        height = convertPixelsToDp(size.y);

        //Set typeface for fonts
        LatoBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        LatoSemiBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Semibold.ttf");
        LatoLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(Html.fromHtml("<b><big>Höfundar</big></b>" + "\n" + "<small>Beygðu</small>"));
        if (320 > width && width < 384) {
            title.setTextSize(35);
        }
        else if(384 > width && width < 600) {
            title.setTextSize(35);
        }
        else if(width > 600){
            title.setTextSize(35);
        }
        title.setTypeface(LatoLight);

        // Select titles
        TextView arnarTitle = (TextView)findViewById(R.id.arnarTitle);
        arnarTitle.setTypeface(LatoSemiBold);
        arnarTitle.setTextSize(18);
        TextView danielTitle = (TextView)findViewById(R.id.danielTitle);
        danielTitle.setTypeface(LatoSemiBold);
        danielTitle.setTextSize(18);
        TextView jonTitle = (TextView)findViewById(R.id.jonTitle);
        jonTitle.setTypeface(LatoSemiBold);
        jonTitle.setTextSize(18);
        TextView snaerTitle = (TextView)findViewById(R.id.snaerTitle);
        snaerTitle.setTypeface(LatoSemiBold);
        snaerTitle.setTextSize(18);


        // Select Desc
        /*TextView arnarDesc = (TextView)findViewById(R.id.arnarDesc);
        arnarDesc.setTypeface(LatoLight);
        arnarDesc.setTextSize(16);
        TextView danielDesc = (TextView)findViewById(R.id.danielDesc);
        danielDesc.setTypeface(LatoLight);
        danielDesc.setTextSize(16);
        TextView jonDesc = (TextView)findViewById(R.id.jonDesc);
        jonDesc.setTypeface(LatoLight);
        jonDesc.setTextSize(16);
        TextView snaerDesc = (TextView)findViewById(R.id.snaerDesc);
        snaerDesc.setTypeface(LatoLight);
        snaerDesc.setTextSize(16);*/


    }


    /**
     * This method converts device specific pixels to density independent pixels.
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public float convertPixelsToDp(float px){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_author, menu);
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
