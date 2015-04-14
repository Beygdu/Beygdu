package is.arnastofnun.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import is.arnastofnun.SkrambiWebTool.PostRequestHandler;
import is.arnastofnun.beygdu.R;
import is.arnastofnun.utils.SkrambiValidator;

/**
 * Created by arnarjons on 14.4.2015.
 */
public class SkrambiAsyncTask extends AsyncTask<String, Void, String[]> {

    ///
    // Task variables
    ///
    private String url = "http://skrambi.arnastofnun.is/checkDocument";

    public ProgressDialog pDialog;
    private String pDialogString;

    public SkrambiAsyncTask(Context context) {
        pDialog = new ProgressDialog(context);
        pDialogString = context.getResources().getString(R.string.progressdialog);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.pDialog.setMessage(this.pDialogString);
        this.pDialog.setCancelable(false);
        this.pDialog.show();
    }

    @Override
    protected String[] doInBackground(String... args) {

        PostRequestHandler pHandler = new PostRequestHandler(this.url, args[0],
                "text/plain", "en-US", false, true, true);
        String rawResponse = pHandler.sendRequest();

        SkrambiValidator sValidator = new SkrambiValidator(rawResponse);

        if(sValidator.validate()) {
            return sValidator.getOutput();
        }

        //TODO : Edit method asString for debugging
        return new String[] { sValidator.asString() };
        //return null;
    }

    @Override
    protected void onPostExecute(String[] arg) {
        this.pDialog.dismiss();
    }

}
