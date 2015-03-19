package is.arnastofnun.SkrambiWebTool;

import android.app.Activity;
import android.os.AsyncTask;

import is.arnastofnun.beygdu.MainActivity;

/**
 * Created by arnarjons on 10.3.2015.
 */
public class SkrambiWT extends AsyncTask<String, Void, String> {

    @Override
    protected  void onPreExecute() {
        super.onPreExecute();
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
        // Do nothing
    }


}
