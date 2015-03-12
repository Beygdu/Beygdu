package is.arnastofnun.beygdu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import is.arnastofnun.parser.BinParser;
import is.arnastofnun.parser.WordResult;

/**
 * Created by arnarjons on 12.3.2015.
 */
public class BinThread extends AsyncTask<String, Void, WordResult> {

    private ProgressDialog processDialog;

    public BinThread(Context context) {
        this.processDialog = new ProgressDialog(context);
    }

    protected void onPreExecute() {
        this.processDialog.setMessage("Saekji gogn");
        this.processDialog.setCancelable(false);
        this.processDialog.show();
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
        if(this.processDialog.isShowing()){
            this.processDialog.dismiss();
        }
    }


}
