package is.arnastofnun.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import is.arnastofnun.beygdu.MainActivity;
import is.arnastofnun.beygdu.R;
import is.arnastofnun.utils.NetworkStateListener;

/**
 * @author Arnar Jónsson
 * @since 13.3.2015
 * @version 1.0
 */
public class BinThread extends AsyncTask<String, Void, WordResult> {

    private Context context;
    ProgressDialog pDialog;
    /**
     * BinThread - An AsyncTask that uses the BinParser to fetch information about a given word
     * Returns a WordResult object containing information about the search
     */

    public BinThread(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        //this.pDialog = new ProgressDialog(context);
        //this.pDialog.setMessage(this.context.getString(R.string.progressdialog));
        //this.pDialog.setCancelable(false);
        //this.pDialog.show();
    }

    protected WordResult doInBackground(String... string) {

        if(!new NetworkStateListener(this.context).isConnectionActive()) {
            return null;
        }

        String word = string[0];

        WordResult wR = new WordResult();

        try {
            int id = Integer.parseInt(word);
            BinParser bParser = new BinParser(id, new String[]{"h2", "h3", "h4", "th", "tr"} );
            wR = bParser.getWordResult();
        }
        catch( NumberFormatException e) {
            if(string[1].equals("1")) {
                BinParser bParser = new BinParser(word, 1, new String[]{"h2", "h3", "h4", "th", "tr"});
                wR = bParser.getWordResult();
            }
            else {
                BinParser bParser = new BinParser(word, 0, new String[]{"h2", "h3", "h4", "th", "tr"});
                wR = bParser.getWordResult();
            }
        }

        return wR;
    }

    protected void onPostExecute(WordResult wordResult) {
        //this.pDialog.dismiss();
    }

}
