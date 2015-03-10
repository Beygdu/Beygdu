package is.arnastofnun.SkrambiWebTool;

import android.app.Activity;
import android.os.AsyncTask;

import is.arnastofnun.beygdu.MainActivity;

/**
 * Created by arnarjons on 10.3.2015.
 */
public class SkrambiWT extends AsyncTask<Void, Void, Void> {

    public Activity activity;

    private String word;
    private String URL;

    private String responseString;


    public SkrambiWT(Activity activity, String word) {

        this.activity = activity;

        this.URL = "http://skrambi.arnastofnun.is/checkDocument";
        this.word = word;

    }

    @Override
    protected  void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... args) {
        PostRequestHandler pHandler = new PostRequestHandler(this.URL, this.word,
                "text/plain", "en-US", false, true, true);
        this.responseString = pHandler.sendRequest();
        return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        String returnString = this.responseString;
        //activity.(returnString);
    }


}
