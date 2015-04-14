package is.arnastofnun.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import is.arnastofnun.beygdu.R;
import is.arnastofnun.parser.WordResult;
import is.arnastofnun.parser.BinParser;
import is.arnastofnun.utils.NetworkStateListener;

/**
 * @author Arnar Jonsson
 * @version 0.1
 * @since  14.4.2015.
 * BinAsyncTask, handles results from the bin database callback
 * called with
 *          BinAsyncTask(CONTEXT).execute("SEARCHWORD","0/1").get();
 *          CONTEXT == application context (XActicity.this)
 *          SEARCHWORD == self explanatory
 *          0/1 == control flow, 0 for id searches and simple searches
 *                  1 for advanced searches (rep. as string)
 */
public class BinAsyncTask extends AsyncTask<String, Void, WordResult> {

    ///
    // Control Flow
    ///
    private String controlFlow;
    private String isInt = "0";
    private String isString = "1";

    ///
    // Task variables
    ///
    private int id = -1;
    private Context context;
    public ProgressDialog pDialog;
    private String pDialogString;

    public BinAsyncTask(Context context) {
        this.pDialog = new ProgressDialog(context);
        this.pDialogString = context.getResources().getString(R.string.progressdialog);

        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.pDialog.setMessage(this.pDialogString);
        this.pDialog.setCancelable(false);
        this.pDialog.show();
    }

    @Override
    protected WordResult doInBackground(String... args) {

        if(!new NetworkStateListener(this.context).isConnectionActive()) {
            return null;
        }

        try {

            if (args[1].equals("0")) {
                setControlFlow(args[0]);
                switch (this.controlFlow) {
                    case "0":
                        return new BinParser(this.id,
                                new String[]{"h2", "h3", "h4", "th", "tr"})
                                .getWordResult();
                    case "1":
                        return new BinParser(convertToUTF8(args[0]), 0,
                                new String[]{"h2", "h3", "h4", "th", "tr"})
                                .getWordResult();
                    default:
                        return null;
                }

            } else {
                return new BinParser(convertToUTF8(args[0]), 1,
                        new String[]{"h2", "h3", "h4", "th", "tr"})
                        .getWordResult();
            }
        }
        catch (Exception e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(WordResult arg) {
        this.pDialog.dismiss();
    }

    ///
    // Utilz
    ///
    private void setControlFlow(String arg) {
        try {
            int newArg = Integer.parseInt(arg);
            newArg = this.id;
            this.controlFlow = this.isInt;
        }
        catch (NumberFormatException e) {
            this.controlFlow = this.isString;
        }
    }

    private String convertToUTF8(String word) {
        try {
            word = URLEncoder.encode(word, "UTF-8");
            return word;
        }
        catch( UnsupportedEncodingException e ) {
            return word;
        }
    }
}
