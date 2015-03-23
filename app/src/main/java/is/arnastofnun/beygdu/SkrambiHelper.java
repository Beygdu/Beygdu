package is.arnastofnun.beygdu;

import android.util.Log;
import android.widget.Toast;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import is.arnastofnun.SkrambiWebTool.SkrambiWT;

/**
 * Created by arnarjons on 19.3.2015.
 */
public class SkrambiHelper {

    public SkrambiHelper() {
        // Empty Constructor
    }

    private String[] prepareResults(String[] rawResults) {
        String[] correctStrings = new String[rawResults.length];
        int count = 0;
        for(String str : rawResults) {
            str = str.replaceAll("\"", "");
            str = str.replaceAll(",", "");
            correctStrings[count++] = str;
        }

        return correctStrings;
    }

    private String[] checkEncodingStage(String[] args) {
        String[] decodingArray = new String[args.length];
        for(int i = 0; i < args.length; i++) {
            try {
                Properties p = new Properties();
                p.load(new StringReader("key="+args[i]));
                decodingArray[i] = p.getProperty("key");
            }
            catch ( Exception e ) {
                Log.w("Exception", e);
                decodingArray[i] = args[i];
            }
        }
        return decodingArray;
    }

    public String[] getSpellingCorrection(String spellCheck) {
        try {
            String str = new SkrambiWT().execute(spellCheck).get();
            //Destroying duplicates of [ and ]
            str = str.substring(1, str.length()-1);
            str = str.substring(str.indexOf("[")+1,
                    str.indexOf("]"));
            String[] results = str.split(" ");
            results = prepareResults(results);
            results = checkEncodingStage(results);
            return results;
        }
        catch( Exception e ) {
            Log.w("Exception", e);
            return null;
        }
    }
}
