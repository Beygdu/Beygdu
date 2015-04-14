package is.arnastofnun.utils;

import android.util.Log;

import java.io.StringReader;
import java.util.Properties;

/**
 * @author Arnar Jonsson
 * @version 0.1
 * @since  14.4.2015.
 */
public class SkrambiValidator {

    ///
    // Error handling
    ///
    private boolean isError = false;
    private String errorString = null;
    public static final String VALIDATOR_ERROR_1 = "Input Wrapper does not meet the desired description";
    public static final String VALIDATOR_ERROR_2 = "Input keywords do not match the desired description";
    public static final String VALIDATOR_ERROR_3 = "Failed to morph input into desired output";

    ///
    // Validators variables
    ///
    private String input;
    private String[] output;

    /**
     * SkrambiValidator
     * An OutputValidator for string responses from
     * the Skrambi server
     * @param input
     */
    public SkrambiValidator(String input) {
        this.input = input;
        this.isError = false;
    }

    ///
    // Get/Set Methods
    ///

    /**
      * @param inputString Input (String)
     */
    public void setInput(String inputString) {
        this.input = inputString;
    }

    /**
     * @return input
     */
    public String getInput() {
        return this.input;
    }

    /**
     * @return Validity of the input
     */
    public boolean isError() {
        return this.isError;
    }

    /**
     * @return Latest error message of the validator, if any
     */
    public String getErrorMessage() {
        if(isError) return this.errorString;
        return null;
    }

    ///
    // Validators inner logic
    // [{"charStart": X, "charEnd": Y, "targetWord": "EXAMPLE", "suggestions": ["EXAMPLE1", "EXAMPLE2", ... "EXAMPLEN"], "errorClass": "CLASS"}]
    // X == 0 (d)
    // Y == d
    // EXAMPLE? == .+
    // CLASS == nwe (no understanding, only using class nwe)
    ///
    private boolean validateSkrambiOutPut() {

        this.isError = checkInputWrapping();

        this.isError = checkMissingKeyWords();

        this.isError = manageOutput();

        if(this.isError) return false;

        return true;
    }

    private boolean validateInput() {
        return validateSkrambiOutPut();
    }

    /**
     * Validates the legitimacy of the input
     * @return TRUE if input is legal
     * @return FALSE otherwise
     */
    public boolean validate() {
        return validateInput();
    }

    ///
    // Response handling
    ///

    /**
     * @return Validated output, if any
     */
    public String[] getOutput() {
        if(!isError) return this.output;
        return null;
    }

    ///
    // Utilz
    // [{"charStart": X, "charEnd": Y, "targetWord": "EXAMPLE", "suggestions": ["EXAMPLE1", "EXAMPLE2", ... "EXAMPLEN"], "errorClass": "CLASS"}]
    ///

    /**
     * Encodes all strings on array args to handle the icelandic alphabet
     */
    private String[] forceEncoding(String[] args) {
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
     * Removes wrapping of each result in the array
     * Front wrapper is "\""
     * Back wrapper is "\","
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
     * Tries to clean the input string
     */
    private boolean manageOutput() {
        String rawOutPut = this.input;
        try {
            // Removing square brackets from wrapper
            rawOutPut = rawOutPut.substring(1, rawOutPut.length()-1);

            // Extractiong ["EXAMPLE1", "EXAMPLE2", ... "EXAMPLEN"]
            rawOutPut = rawOutPut.substring(
                    rawOutPut.indexOf("[")+1,
                    rawOutPut.indexOf("]")
            );

            // Split rawOupPut into array based on whitespaces
            String[] rawArray = rawOutPut.split(" ");

            rawArray = prepareResults(rawArray);

            rawArray = forceEncoding(rawArray);

            this.output = rawArray;

            return false;
        }
        catch (Exception e) {
            this.errorString = SkrambiValidator.VALIDATOR_ERROR_3;
            return true;
        }
    }

    private boolean checkMissingKeyWords() {
        boolean containsKeyWords = this.input.contains("charStart")
                && this.input.contains("charEnd")
                && this.input.contains("targetWord")
                && this.input.contains("suggestions")
                && this.input.contains("errorClass");

        if(containsKeyWords) {
            return false;
        }

        this.errorString = SkrambiValidator.VALIDATOR_ERROR_2;
        return true;
    }

    /**
     * Front wrapper is "[{"
     * Back wrapper is "]}"
     */
    private boolean checkInputWrapping() {
        if(this.input.contains("[{") && this.input.contains("]}")) {
            if(this.input.indexOf("[{") == 0) {
                if(this.input.indexOf("]}") == this.input.length()-2) {
                    return false;
                }
            }
        }

        this.errorString = SkrambiValidator.VALIDATOR_ERROR_1;
        return true;
    }

    public String asString() {
        // TODO : Create method for debugging if necessary (Current status : not)
        return null;
    }

}
