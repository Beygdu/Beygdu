package is.arnastofnun.beygdu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import is.arnastofnun.beygdu.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import is.arnastofnun.parser.HTMLParser;
import is.arnastofnun.parser.ParserResult;
import is.arnastofnun.parser.WordResult;

/**
 * @author Jón Friðrik, Arnar, Snær, Máni
 * @since 05.11.14
 * @version 1.0
 * 
 * The initial activity of the program. Has a Text input og a button for initializing the search.
 * Also has an actionbar where the user can open other activies such as AboutActivity
 * and send an email to the creator of the database
 * 
 */
public class MainActivity extends NavDrawer {

	/**
	 * The result from the parser search.
	 */
	public ParserResult pR = new ParserResult();

	/**
	 * @param pR the parser results.
	 */
	public void setParserResult(ParserResult pR) {
		this.pR = pR;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		checkNetworkState();
	}
	
	/**
	 * Checks if the user is connected to a network.
	 * TODO - Should be implemented so that it shows a dialog if 
	 * the user is not connected
	 */
	private void checkNetworkState() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			Toast.makeText(MainActivity.this,
					"Þú ert ekki nettengdur.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * @return item 
	 *  a switch for all the possibilies in the ActionBar. 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			Intent intent1 = new Intent(this, AboutActivity.class);
			startActivity(intent1);
			break;
		case R.id.action_mail:
			sendEmail();
			break;
		} 
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Send a post to sth132@hi.is
	 */
	protected void sendEmail() {
		Log.i("Senda post", "");
		String[] TO = {"sth132@hi.is"};
		String[] CC = {"sth132@hi.is"};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Þitt vidfang");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Skilabod her");
		try {
			startActivity(Intent.createChooser(emailIntent, "Sendu post....."));
			finish();
			Log.i("Buin ad senda post...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivity.this,
					"Engin póst miðill uppsettur í þessu tæki.", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * @param view the view points to this activity. 
	 * Runs when the user pushes the search button
	 * Constructs a new Async thread which fetches the results for the searchString which was inputted by the user.
	 * If nothing or more than one word was inputted the user gets a Toast notification.
	 * The input can have 1 or two space characters infront and behind it.
	 */
	public void btnOnClick(@SuppressWarnings("unused") View view){
		EditText editText = (EditText) findViewById(R.id.mainSearch);
		String word = editText.getText().toString();
		
		if(word.isEmpty()){
			Toast.makeText(this, "Vinsamlegasta sláið inn orð í reitinn hér að ofan", Toast.LENGTH_SHORT).show();
		} else if(word.contains(" ")){
			if (islegalInput(word)) {
				word = replaceSpaces(word);
				word = convertToUTF8(word);
				new ParseThread(word).execute();
			} else {
				Toast.makeText(this, "Einingis hægt að leita að einu orði í einu", Toast.LENGTH_SHORT).show();
			}
		} else {
			//New Thread to get word
			word = convertToUTF8(word);
			new ParseThread(word).execute();
		}
	}
    public void cacheClick(@SuppressWarnings("unused") View view){
        Intent intent = new Intent(MainActivity.this, Cache.class);
        startActivity(intent);
    }

	
	/**
	 * @param word the searchword
	 * @return word converted to UTF-8
	 */
	private String convertToUTF8(String word) {
		try {
			word = URLEncoder.encode(word, "UTF-8");
			return word;
		}
		catch( UnsupportedEncodingException e ) {
			return "";
		}
	}
	
	/**
	 * 
	 * @param a the string
	 * @return the string without spacecharactes (" ")
	 */
	private boolean islegalInput(String a) { 
	    if (a.equals("")) {
	    	return false;
	    } else {
			ArrayList<Integer> starts = new ArrayList<Integer>();  
		    Pattern pattern = Pattern.compile("\\s");
		    Matcher matcher = pattern.matcher(a);
		    
		    while( matcher.find() ) {
		      starts.add(matcher.start());
		    }
		    if( starts.get(0) == a.length()-1 || (starts.get(0) == 0 && starts.size() == 1) ||
		      (starts.get(0) == 0 && starts.get(1) == a.length()-1 && starts.size() == 2) ) {
		      return true;
		    } 
		    return false;
	    }
	  }
	
	private String replaceSpaces(String a) {
	    return a.replaceAll("\\s+","");
	  }

	/**
	 * sees if the results are:
	 * <strong>Partial hit: </strong> many words with the different meaning spelled the same way.
	 * <strong>Critical hit: </strong> the word is found and the results have been set in WordResults
	 * Or no result.
	 */
	private void checkWordCount() {
		String pr = pR.getType();
		if (pr.equals("Multiple results")) {
			FragmentManager fM = getSupportFragmentManager();
			DialogFragment newFragment = new WordChooserDialogFragment();
			newFragment.show(fM, "wordChooserFragment");
		} else if (pr.equals("Single result")) {
			WordResult word = pR.getWordResult();
			createNewActivity(word);
		} else if (pr.equals("Word not found")) {
			Toast.makeText(this, "Engin leitarniðurstaða", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * @param word the WordResult
	 * Constructs a new activity with the result.
	 */
	private void createNewActivity(WordResult word) {
		Intent intent = new Intent(this, BeygingarActivity.class);
		intent.putExtra("word", word);
		startActivity(intent);
	}

	/**
	 * @author Jón Friðrik
	 * @since 23.10.14
	 * @version 0.1
	 * 
	 * Construct a Dialog where the user can choose <strong>one</strong> of the words in results ArrayList
	 * or go back
	 */
	public class WordChooserDialogFragment extends DialogFragment {

		/**
		 * selectedItem - the object which was last chosen in the dialog, or the first object in the list if nothing was chosen
		 * charArr - Are the words which the ParserResult contains if it return a PartialHit
		 */
		private String selectedItem = null;
		private CharSequence[] charArr;

		/**
		 *  The constructor for WordChooserDialogFragment
		 *  A dialog where the user can choose between search results
		 *  Only one word can be chosen.
		 */
		public WordChooserDialogFragment() {
			toCharArr();
		}

		/**
		 * cast the ArrayList to a CharSequence.
		 */
		private void toCharArr() {
			charArr = new CharSequence[pR.getDesc().length];
			for (int i = 0; i < pR.getDesc().length; i++){
				charArr[i] = pR.getDesc()[i];
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstance) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.choosedialog);

			builder.setSingleChoiceItems(charArr, -1 , 
					new DialogInterface.OnClickListener() {
				/**
				 * a listener which listens to which result the user chooses from the dialog
				 */
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedItem = pR.getIds()[which]+"";
				}
			});

			builder.setPositiveButton(R.string.afram, new DialogInterface.OnClickListener() {
				/**
				 * a listener if the user presses the continue button
				 * If nothing is chosem then the dialog closes
				 */
				public void onClick(DialogInterface dialog, int id) {
					if( selectedItem != null) {
						int wordId = Integer.parseInt(selectedItem);
						new ParseThread(wordId).execute();
					}
				}
			});
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				/**
				 * a listener for the cancel button, nothing happens except the dialog closes
				 */
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});
			return builder.create();
		}	
	}

	/**
	 * 
	 * @author Arnar, Jón Friðrik
	 * @since 23.10.14
	 * @version 1.0
	 * 
	 */
	private class ParseThread extends AsyncTask<Void, Void, Void> {
		/**
		 * parser - the parser which is constructed to retrieve the results
		 * url - the url which the parser uses. 
		 */
		private HTMLParser parser;
		private String url;

		/**
		 * 
		 * @param searchWord -the string which is concated into the url
		 * the searchWord string has been converted to UTF-8
		 * (Má leita af hvaða orðmynd.)
		 */
		public ParseThread(String searchWord) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?q=";
			url = baseURL + searchWord + "&ordmyndir=on";
		}

		/**
		 * @param searchId - the id (int) which will be concated into the url
		 */
		public ParseThread(int searchId) {
			String baseURL = "http://dev.phpbin.ja.is/ajax_leit.php/?id=";
			url = baseURL + searchId + "&ordmyndir=on";
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * the function which is after the thread has been constructed
		 */
		@Override
		protected Void doInBackground(Void... args) {
			Document doc;
			try {
				doc = Jsoup.connect(url).get();
				parser = new HTMLParser(doc);
			} catch (IOException e) {
				Toast.makeText(MainActivity.this,
						"Tenging rofnaði, vinsamlega reynið aftur.", Toast.LENGTH_LONG).show();
			}
			return null;
		}
		/**
		 * the function which is called when the diInBackground function is finished
		 * ParserResults are set in MainActivity.
		 */
		@Override
		protected void onPostExecute(Void args) {
			setParserResult(parser.getParserResult());
			checkWordCount();
		}
	}
}