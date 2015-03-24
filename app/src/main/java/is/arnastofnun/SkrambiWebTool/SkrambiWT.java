package is.arnastofnun.SkrambiWebTool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import is.arnastofnun.beygdu.MainActivity;
import is.arnastofnun.beygdu.R;

/**
 * @author Arnar Jonsson
 * @since 10.3.2015
 * @version 1.0
 */
public class SkrambiWT extends AsyncTask<String, Void, String> {

    private Context context;
    ProgressDialog pDialog;
    /**
     * SkrambiWT - An AsyncTask that fetches potential result from the
     * Skrambi web services
     */

    public SkrambiWT(Context context) {
        this.context = context;
    }

    @Override
    protected  void onPreExecute() {
        super.onPreExecute();

        this.pDialog = new ProgressDialog(context);
        this.pDialog.setMessage(this.context.getString(R.string.progressdialog));
        this.pDialog.setCancelable(false);
        this.pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        String url = "http://skrambi.arnastofnun.is/checkDocument";
        PostRequestHandler pHandler = new PostRequestHandler(url, args[0],
                "text/plain", "en-US", false, true, true);
        String responseString = pHandler.sendRequest();
        return responseString;
    }

    @Override
    protected void onPostExecute(String args) {
        this.pDialog.dismiss();
    }


}
