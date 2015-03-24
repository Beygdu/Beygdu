package is.arnastofnun.beygdu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import is.arnastofnun.SkrambiWebTool.SkrambiWT;

/**
 * @author Arnar Jonsson
 * @since 19.3.2015
 * @version 1.0
 */
public class SkrambiHelper {

    /**
     * SkrambiHelper - A helper class for SkrambiWT
     * Allows the application to call SkrambiWT, which returns potential spelling
     * correnctions of the searchword
     * @param context Context of the calling application
     */
    public SkrambiHelper(Context context) {
        // Empty Constructor
    }

    /**
     * Regex statements to fetch the current result from the server callback
     */
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

    /**
     * @param args
     * @return Special character reconstruction of the strings within
     * the array
     */
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

    /**
     * @param spellCheck A word that needs to be spell checked
     * @return An array of suggestion by the Skrambi Web Tool
     */
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
