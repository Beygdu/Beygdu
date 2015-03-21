package is.arnastofnun.beygdu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import is.arnastofnun.DB.DBController;
import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.WordResult;
import is.arnastofnun.utils.TableFragment;

/**
 * @author Jón Friðrik Jónatansson, Daniel Pall
 * @since 25.10.14
 * @version 1.0
 *
 *BeygingarActivity contains a LinearLayout with a ScrollView which contains a TableLayout.
 *The tables are then plaved in the TableLayout.
 */
public class BeygingarActivity extends NavDrawer {

	/**
	 * tableLayout is the TableLayout in the activity, the TextViews and TableFragments are added to it
	 * tables is a ArrayLists which contains the tables which are shown in the TableLayout.
	 * blockNames is a ArrayList which contains the title of each block , visible and not visible.
	 * mSelectedItems is an ArrayList which contains the indexes of the tables the user has chosein in the TableChooserDialogFragment.
	 * words - is the result from the Parser
	 */
	private TableLayout tableLayout;
	private ArrayList<TableFragment> tables = new ArrayList<TableFragment>();
	private ArrayList<String> blockNames = new ArrayList<String>();
	private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
	private WordResult words;

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
        // setContentView(R.layout.activity_beygingar);

        /**
         * Inflate the layout into the NavDrawer layout
         * where `frameLayout` is a FrameLayout in the layout for the
         * NavDrawer (see file nav_base_layout)
         */
        getLayoutInflater().inflate(R.layout.activity_beygingar, frameLayout);

        /**
         * Setting what item is checked
         */
        mDrawerList.setItemChecked(position,true);

        getActionBar().setDisplayHomeAsUpEnabled(true);

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

		//get WordResult from MainActivity.
		Intent intent = getIntent();
		words = (WordResult) intent.getSerializableExtra("word");
		tableLayout = (TableLayout) findViewById(R.id.data_table);

		//fill mSelectedItems with all possible blocks
		for (int i = 0; i < words.getBlocks().size(); i++) {
			mSelectedItems.add(i);
			blockNames.add(words.getBlocks().get(i).getTitle());
		}
		initTables();

        //Save to DB -- if it does not exist before
        DBController dbController = new DBController(this);
        dbController.insert(words);
        dbController.insertStats(words.getTitle());

        // Set the title in the actionbar
        setTitle(firstWordInString(words.getTitle()));

        // If it is possible to filter the word result, add a Navigation
        // Drawer Item at the second last position in the list
        navDrawerFilterableListItem();
}

    /**
     * If the word is filterable, set the option to filter in the navigation drawer
     */
    public void navDrawerFilterableListItem(){
        if(checkWordFilterable()){
            navArray.add(navArray.size()-1,getString(R.string.nav_drawer_sia));
        }
    }

    /**
     * Checks if it is possible to filter the world result and returns true
     * if it is possible, false if it is not possible
     */
    public boolean checkWordFilterable(){
        if (words.getBlocks().size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Override the behavior of the navigation drawer because we want to have
     * added functionality just for this Activity, the Sía functionality
     */
    @Override
    protected void openActivity(int position) {
        if(checkWordFilterable()){
            mDrawerLayout.closeDrawer(mDrawerList);
            // Set the position so we can access it from child activities
            NavDrawer.position = position;

            switch (position) {
                case 0:
                    startActivity(new Intent(this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    break;
                case 1:
                    startActivity(new Intent(this, Cache.class));
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    break;
                case 2:
                    filterAction();
                    break;
                case 3:
                    startActivity(new Intent(this, AboutActivity.class));
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    break;
            }
        } else {
            super.openActivity(position);
        }
    }

    /**
     * Accepts a String of word(s) and returns the first word in that string.
     * Looks for an empty space (" ") to see where the word ends.
     * @param title String of words
     * @return String that is the first word in a String
     */
    private String firstWordInString(String title){
        String firstWord = null;

        // Get the first word
        if(title.contains(" ")){
            firstWord = title.substring(0, title.indexOf(" "));
        }

        // Capitalize the first letter of the word
        if(firstWord != null){
            firstWord = firstWord.substring(0,1).toUpperCase() + firstWord.substring(1);
        }
        return firstWord;
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

	
	/**
	 * Constructs a TextView with the title of the word and possibly a TextView with a note about the word, if it exits, 
	 * and puts them into the tableLayout. Then a TableFragment is constructed for each block in the word.
	 */
	private void initTables(){
		//SetTitle
		TextView titleDesc = new TextView(this);
        titleDesc.setText(words.getTitle());
        if (320 > width && width < 384) {
            titleDesc.setTextSize(20);
        }
        else if(384 > width && width < 600) {
            titleDesc.setTextSize(24);
        }
        else if(width > 600){
            titleDesc.setTextSize(30);
        }

		titleDesc.setMinHeight(130);
		titleDesc.setTypeface(LatoBold);
        titleDesc.setTextColor(R.color.white);
        tableLayout.addView(titleDesc);
		
		//SetNote
		if(!words.getWarning().equals("")) {
			TextView note = new TextView(this);
			note.setText(words.getWarning());
            note.setTypeface(LatoLight);
            note.setMaxWidth(getScreenWidth());
			note.setBackgroundResource(R.drawable.noteborder);
			tableLayout.addView(note);
		}

		//Iterate through blocks and set title
		tables.clear();
		for (int i = 0; i < words.getBlocks().size(); i++){
			if (mSelectedItems.contains(i)) {
				Block block = words.getBlocks().get(i);
				TextView blockTitle = new TextView(this);
                if (320 > width && width < 384) {
                    blockTitle.setTextSize(18);
                }
                else if(384 > width && width < 600) {
                    blockTitle.setTextSize(22);
                }
                else if(width > 600){
                    blockTitle.setTextSize(28);
                }
				blockTitle.setMinHeight(100);
				blockTitle.setText(block.getTitle());
                blockTitle.setTypeface(LatoSemiBold);
                blockTitle.setTextColor(getResources().getColor(R.color.white));
                blockTitle.setPadding(0, 10, 0, 10);
                TableFragment tFragment = new TableFragment(BeygingarActivity.this, tableLayout, block, blockTitle);
				getFragmentManager().beginTransaction().add(tableLayout.getId(), tFragment).commit();
				tables.add(tFragment);				
			}
		}
	}
	
	/**
	 * If word contains more than one block a TableChooserDialogFragment is constructed.
	 */
	public void filterAction(){
		if (words.getBlocks().size() > 1) {
			FragmentManager fM = getSupportFragmentManager();
			DialogFragment newFragment = new TableChooserDialogFragment();
			newFragment.show(fM, "tableChooserFragment");
		} else {
			Toast.makeText(BeygingarActivity.this,
					"Ekkert til að sía.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beygingar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_filter:
			filterAction();
			break;
//		case R.id.action_about:
//			Intent intent1 = new Intent(this, AboutActivity.class);
//			startActivity(intent1);
//			break;
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
			Toast.makeText(BeygingarActivity.this,
					"Engin póst miðill uppsettur.", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * tableLayout is cleared and new Table is constructed.
	 */
	public void updateFragments() {
		tableLayout.removeAllViews();
		initTables();
	}

	/**
	 * @author Jón Friðrik
	 * @since 23.10.14
	 * @version 1.0
	 * 
	 * Construct a Dialog where the chooser can choose multiple blocks which are drawn in the tableLayout.
	 */
	public class TableChooserDialogFragment extends DialogFragment {

		/**
		 * charArr - Charsequence array containing all the titles of the clocks
		 */

		private CharSequence[] charArr;

		/**
		 *  The constructor for TableChooserDialogFragment
		 *  A dialog where the user can choose blocks from the WordResults
		 */
		public TableChooserDialogFragment() {
			makeCharArr();
		}
		
		/**
		 * fills the charArr with block titles
		 */
		private void makeCharArr() {
			charArr = new CharSequence[blockNames.size()];
			for (int i = 0; i < charArr.length; i++){
				charArr[i] = blockNames.get(i);
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstance) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.choosedialog);			
			boolean[] prevChoices = new boolean[blockNames.size()];
			for (int i = 0; i < blockNames.size(); i++) {
				if (mSelectedItems.contains(i)){
					prevChoices[i] = true; 
				} else {
					prevChoices[i] = false;
				}
			}
			
			builder.setMultiChoiceItems(charArr, prevChoices,
					new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which,
						boolean isChecked) {
					if (isChecked) {
						// If the user checked the item, add it to the selected items
						mSelectedItems.add(which);
					} else if (mSelectedItems.contains(which)) {
						// Else, if the item is already in the array, remove it 
						mSelectedItems.remove(Integer.valueOf(which));
					}
				}
			})
			// Set the action buttons
			.setPositiveButton(R.string.PositiveButton, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					updateFragments();
				}
			})
			.setNegativeButton(R.string.NegativeButton, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					//Ekkert gerist
				}
			});
			return builder.create();
		}	
	}
}
