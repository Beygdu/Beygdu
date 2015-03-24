package is.arnastofnun.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @author Arnar Jónsson
 * @since 13.3.2015
 * @version 1.0
 */
public class BinThread extends AsyncTask<String, Void, WordResult> {

    /**
     * BinThread - An AsyncTask that uses the BinParser to fetch information about a given word
     * Returns a WordResult object containing information about the search
     */
    //TODO: remove context, processdialog has been moved closer to the UI thread
    public BinThread(Context context) {

    }

    protected void onPreExecute() {

    }

    protected WordResult doInBackground(String... string) {
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

    }

}
