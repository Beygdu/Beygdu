package is.arnastofnun.utils;

/**
 * @author Arnar Jonsson
 * @version 0.1
 * @since  14.4.2015.
 */
public class OutputValidator {

    ///
    // Error Handling
    ///
    private boolean isError = false;
    private String errorString = null;

    ///
    // Validators variables
    ///
    private Object input;
    private Object output;

    /**
     * OutputValidator
     * A zero logic outputvalidator with errorhandling
     * @param input
     */
    
    public OutputValidator(Object input) {
        this.input = input;


    }

    ///
    // Get/Set Methods
    ///

    /**
     * Resets the validators input
     * @param input
     */
    public void setInput(Object input) {
        this.input = input;
    }

    /**
     * @return The validators inputString
     */
    public Object getInput() {
        return this.input;
    }

    /**
     * @return The validators latest error message, if any
     */
    public String getErrorMessage() {
        return this.errorString;
    }

    /**
     * @return Validity of the input
     */
    public boolean isLegal() {
        return this.isError;
    }

    ///
    // Validators inner logic
    ///

    ///
    // Result Handling
    ///

    /**
     * @return The validators output, if any
     */
    public Object getOutput() {
        if(!isError) return this.output;
        return null;
    }
}
