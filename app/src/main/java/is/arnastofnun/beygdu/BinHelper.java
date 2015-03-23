package is.arnastofnun.beygdu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import is.arnastofnun.parser.BinThread;
import is.arnastofnun.parser.WordResult;

/**
 * Created by arnarjons on 13.3.2015.
 */
public class BinHelper {

    private Context context;

    public BinHelper(Context context) {

        this.context = context;

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
