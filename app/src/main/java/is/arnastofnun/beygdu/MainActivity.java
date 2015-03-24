package is.arnastofnun.beygdu;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import is.arnastofnun.DB.DBController;
import is.arnastofnun.parser.WordResult;
import is.arnastofnun.utils.CustomDialog;


/**
 * @author Jón Friðrik, Arnar, Snær, Máni
 * @since 05.11.14
 * @version 1.0
 * 
 * The initial activity of the program. Has a Text input og a button for initializing the search.
 * Also has an actionbar where the user can open other activities such as AboutActivity
 * and send an email to the creator of the database
 * 
 */
public class MainActivity extends NavDrawer implements CustomDialog.DialogListener {

    /**
     * Progress dialog to be used in Async Task
     */
    ProgressDialog progressDialog;

	/**
	 * The result from the parser search.
     * ---Depricated
	 */

	//public ParserResult pR = new ParserResult();


    /**
     * The WordResult Document, containing all data on searched word
     */
    public WordResult wR;

	public void setWordResult(WordResult wordResult) {
		this.wR = wordResult;
	}


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
        // setContentView(R.layout.activity_main);

		/**
        * Inflate the layout into the NavDrawer layout
        * where `frameLayout` is a FrameLayout in the layout for the
        * NavDrawer (see file nav_base_layout)
        */
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        /**
         * Setting the title and what item is checked
         */
        mDrawerList.setItemChecked(position,true);
        setTitle(R.string.app_name);

        getActionBar().setDisplayHomeAsUpEnabled(false);

		checkNetworkState();
        headerText();


	}

    /**
     * This method changes text size depending on screen sizes
     * Snær Seljan
     */
    public void headerText() {
        //Set typeface for fonts
        LatoBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");
        LatoSemiBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Semibold.ttf");
        LatoLight = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        TextView header = (TextView)findViewById(R.id.title);
        TableRow rowSearch = (TableRow) findViewById(R.id.search_row);

        header.setTypeface(LatoLight);
        if (320 > width && width < 384) {
            header.setTextSize(30);
        }
        else if(384 > width && width < 600) {
            header.setTextSize(50);
        }
        else if(width > 600){
            header.setTextSize(50);
        }

        Animation animateFromTop = AnimationUtils.loadAnimation(this, R.animator.animator);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.animator.fadein);
        rowSearch.startAnimation(fadeIn);
        header.startAnimation(animateFromTop);


    }


    /*public void animationTest(TextView name) {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animator);
        set.setTarget(header);
        set.start();
    }*/

    /**
     * This method converts device specific pixels to density independent pixels.
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     * Snær Seljan
     */
    public float convertPixelsToDp(float px){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
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
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * @return item 
	 *  a switch for all the possibilies in the ActionBar. 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        /**
         *
        switch (item.getItemId()) {
		case R.id.action_about:
			Intent intent1 = new Intent(this, AboutActivity.class);
			startActivity(intent1);
			break;
		case R.id.action_mail:
			sendEmail();
			break;
		}
         */
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
		
		if(word.isEmpty())  {
			Toast.makeText(this, "Vinsamlegasta sláið inn orð í reitinn hér að ofan", Toast.LENGTH_SHORT).show();
		}
        else if(islegalInput(word)) {
            if(word.contains(" ")) {
                word = replaceSpaces(word);
            }
            word = convertToUTF8(word);
            BinHelper bHelper = new BinHelper(getApplicationContext());
            try {
                setWordResult(bHelper.sendThread(word, 1));
            }
            catch( Exception e ) {
                this.wR = null;
            }
            if(this.wR != null) {
                checkWordCount();
            }
            else {
                Toast.makeText(getApplicationContext(), "Villa i btnclicked", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Einingis hægt að leita að einu orði í einu", Toast.LENGTH_SHORT).show();
        }


	}
    public void cacheClick(@SuppressWarnings("unused") View view){
        Intent intent = new Intent(MainActivity.this, Cache.class);
        startActivity(intent);
        overridePendingTransition(R.animator.activity_open_scale,R.animator.activity_close_translate);
    }


    public void statisticsClick(@SuppressWarnings("unused") View view){
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.activity_open_scale,R.animator.activity_close_translate);
    }

    public void googleClick(@SuppressWarnings("unused")  View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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
        /* TODO: FIX THIS
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
	    */
        return true;
        
	  }
	
	private String replaceSpaces(String a) {
	    return a.replaceAll("\\s+","");
	  }

    private String[] intArrayToStringArray(int[] id) {
        String[] returnArray = new String[id.length];

        for(int i = 0; i < id.length; i++) {
            returnArray[i] = Integer.toString(id[i]);
        }

        return returnArray;
    }

	/**
	 * sees if the results are:
	 * <strong>Partial hit: </strong> many words with the different meaning spelled the same way.
	 * <strong>Critical hit: </strong> the word is found and the results have been set in WordResults
	 * Or no result.
	 */
	private void checkWordCount() {

		String pr = wR.getDescription();

		if (pr.equals("MultiHit")) {

            Bundle bundle = new Bundle();
            bundle.putInt("id", 0);
            bundle.putString("title", getString(R.string.MultiHitDialog));
            bundle.putString("positiveButtonText", getString(R.string.PositiveButton));
            bundle.putString("negativeButtonText", getString(R.string.NegativeButton));
            bundle.putStringArray("descriptions", wR.getMultiHitDescriptions());
            bundle.putStringArray("descriptionActions", intArrayToStringArray(wR.getMultiHitIds()));
            android.app.DialogFragment multiDialog = new CustomDialog();
            multiDialog.setArguments(bundle);
            multiDialog.show(getFragmentManager(), "0");

		} else if (pr.equals("SingleHit")) {
			WordResult word = this.wR;
			createNewActivity(word);
		} else if (pr.equals("Miss")) {
            SkrambiHelper sHelper = new SkrambiHelper(getApplicationContext());
            String[] correctedWords = sHelper.getSpellingCorrection(wR.getSearchWord());
            if( correctedWords == null || correctedWords[0].equals("")) {
                DBController controller = new DBController(this);
                if(controller.fetchObeygjanlegt(wR.getSearchWord()) != null){
                    Toast.makeText(this, wR.getSearchWord() + " er óbeygjanlegt orð", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Engin leitarniðurstaða", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putInt("id", 1);
                bundle.putString("title", getString(R.string.SkrambiDialog));
                bundle.putString("positiveButtonText", getString(R.string.PositiveButton));
                bundle.putString("negativeButtonText", getString(R.string.NegativeButton));
                bundle.putStringArray("descriptions", correctedWords);
                bundle.putStringArray("descriptionActions", correctedWords);
                android.app.DialogFragment multiDialog = new CustomDialog();
                multiDialog.setArguments(bundle);
                multiDialog.show(getFragmentManager(), "0");
            }
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
        overridePendingTransition(R.animator.activity_open_scale,R.animator.activity_close_translate);
	}

    private void manageDialogFragmentOutput(String word) {
        if(islegalInput(word)) {
            if(word.contains(" ")) {
                word = replaceSpaces(word);
            }
            word = convertToUTF8(word);
            BinHelper bHelper = new BinHelper(getApplicationContext());
            this.wR = bHelper.sendThread(word, 0);
            if(this.wR != null) {
                checkWordCount();
            }
            else {
                Log.w("app", "wR is null");
                Toast.makeText(getApplicationContext(), "Villa i manageDialogFragmentOutput", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Engin leit fannst", Toast.LENGTH_SHORT).show();
        }
    }

    // INTERFACE HANDLER FOR CUSTOMDIALOG RESULTS
    @Override
    public void onPositiveButtonClick(String selectedItem, int id) {
        BinHelper binHelper = new BinHelper(getApplicationContext());
        try {
            setWordResult(binHelper.sendThread(selectedItem, 1));
            checkWordCount();
        }
        catch (Exception e) {
            Log.w("Exception", e);
            Toast.makeText(getApplicationContext(), "Engin leit fannst", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNegativeButtonClick(String errorString) {

    }
}