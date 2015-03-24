package is.arnastofnun.beygdu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import is.arnastofnun.parser.BinThread;
import is.arnastofnun.parser.WordResult;

/**
 * Created by Arnar Jónsson on 13.3.2015.
 * Class BinHelper
 * @version 0.2
 */
public class BinHelper {

    private Context context;

    /**
     * BinHelper, a helper class for the BinParser
     * Allows an application to call the BinThread
     *
     * @param context The context of the calling application
     */
    public BinHelper(Context context) {

        this.context = context;

    }

    /**
     * @param word
     * @return UTF-8 representation of word
     * @throws java.io.UnsupportedEncodingException, returns word non-encoded
     */
    private String convertToUTF8(String word) {
        try {
            word = URLEncoder.encode(word, "UTF-8");
            return word;
        }
        catch( UnsupportedEncodingException e ) {
            return word;
        }
    }

    /**
     * sendThread - a function that starts the BinThread
     * @param string A string representing an icelandic word
     * @param flag The degree of the search, 0 is quicksearch, 1 is advanced search
     * @return WordResult containing details and content of the searched string
     */
    public WordResult sendThread(String string, int flag) {

        try {
            return new BinThread(this.context).execute(convertToUTF8(string), Integer.toString(flag)).get();
        }
        catch( Exception e ) {
            Log.w("Exception", e);
            return null;
        }


    }

}
