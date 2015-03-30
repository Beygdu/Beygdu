package is.arnastofnun.utils;

import android.content.res.Resources;

import is.arnastofnun.beygdu.R;

/**
 * @author Arnar Jonsson
 * @version 0.1
 * @since 29.3.15
 */
public class InputValidator {

    private String errString;

    private String input;

    private boolean isLegal = true;

    /**
     * Legal input snipping codes
     */
    private String INPUTVALIDATOR_SNIPPING_CODE = "";
    private String SNIPPING_CODE_1 = "Smella ad framan";
    private String SNIPPING_CODE_2 = "Smella ad aftan";

    /**
     * Illegal input error codes
     */
    private String INPUTVALIDATOR_ERRORCODE = "";
    private String ERROR_CODE_1 = "IllegalMetaCharacters";
    private String ERROR_CODE_2 = "IllegalWhiteSpaces";

    /**
     * InputValidator
     * Validator and snipping tool for input strings destined for a BIN database search
     */
    public InputValidator(String input, String errString) {
        this.input = input;
        this.errString = errString;

        validate();
    }

    /////
    // Get/Set
    /////

    /**
     * Resets the input string of the validator
     * @param str
     */
    public void setInput(String str) {
        this.input = str;
        validate();
    }

    public String getInput() {
        if(this.isLegal) return this.input;
        else return null;
    }

    public void setErrString(String str) {
        this.errString = str;
        validate();
    }

    public String getErrString() {
        return this.errString;
    }

    private void setErrorCode(String str) {
        this.INPUTVALIDATOR_ERRORCODE = str;
    }

    public String getErrorCode() {
        return this.INPUTVALIDATOR_ERRORCODE;
    }

    private void setSnippingCode(String str) {
        this.INPUTVALIDATOR_SNIPPING_CODE = str;
    }

    public String getSnippingCode() {
        return this.INPUTVALIDATOR_SNIPPING_CODE;
    }

    /////
    // Collection of public methods
    /////

    public boolean isLegal() {
        return this.isLegal;
    }

    public String removeFrontWhiteSpace(String str) {
        return str.substring(1, str.length()-1);
    }

    public String removeBackWhiteSpace(String str) {
        return str.substring(0, str.length()-2);
    }

    /////
    // Collection of private methods
    /////

    private boolean containsIllegalUriCharacters() {

        for(char i : this.input.toCharArray()) {
            for(char j : this.errString.toCharArray()) {
                if( i == j ) {
                    return true;
                }
            }
        }

        return false;
    }


    private void handleWhitespaceException() {
        //TODO: use replace to delete the single whitespace try/catch
        int whitespaceLocation = this.input.indexOf(" ");
        if( whitespaceLocation == 0 ) {
            this.INPUTVALIDATOR_SNIPPING_CODE = SNIPPING_CODE_1;
            this.input = removeFrontWhiteSpace(this.input);
        }
        else if( whitespaceLocation == this.input.length()-1) {
            this.INPUTVALIDATOR_SNIPPING_CODE = SNIPPING_CODE_2;
            this.input = removeBackWhiteSpace(this.input);
        }
        else {
            this.INPUTVALIDATOR_ERRORCODE = ERROR_CODE_2;
            this.isLegal = false;
        }
    }

    private void manageWhitespaceCharacters() {
        int whitespaceCount = 0;
        for(char i : this.input.toCharArray()) {
            if( Character.isWhitespace(i) ) {
                whitespaceCount++;
            }
        }

        if(whitespaceCount == 1) {
            handleWhitespaceException();
        }
        else {
            this.INPUTVALIDATOR_ERRORCODE = ERROR_CODE_2;
            this.isLegal = false;
        }

    }

    private void validate() {

        if(containsIllegalUriCharacters()) {
            this.INPUTVALIDATOR_ERRORCODE = ERROR_CODE_1;
            this.isLegal = false;
            return;
        }

        if(this.input.contains(" ")) {
            manageWhitespaceCharacters();
        }
    }
}
