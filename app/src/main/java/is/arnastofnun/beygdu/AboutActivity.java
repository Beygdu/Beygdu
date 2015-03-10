package is.arnastofnun.beygdu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import is.arnastofnun.beygdu.R;

/**
 * @author Snær Seljan
 * @since 31.10.14
 * @version $Id:  0.1
 */
public class AboutActivity extends NavDrawer {

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
        getLayoutInflater().inflate(R.layout.activity_about, frameLayout);


		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_mail:
			sendEmail();
			break;
		} 
		return super.onOptionsItemSelected(item);
	}

	protected void sendEmail() {
		Log.i("Senda post", "");
		String[] TO = {"sth132@hi.is"};
		String[] CC = {"sth132@hi.is"};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Titt vidfang");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Skilabod her");
		try {
			startActivity(Intent.createChooser(emailIntent, "Sendu post....."));
			finish();
			Log.i("Buin ad senda post...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, 
					"Engin póst miðill uppsettur.", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		/**
		 * Not used
		 */
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about,
					container, false);
			return rootView;
		}
	}
}
