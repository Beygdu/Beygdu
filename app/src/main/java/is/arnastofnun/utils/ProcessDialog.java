package is.arnastofnun.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import is.arnastofnun.beygdu.R;

/**
 * @author Arnar Jonsson
 * @since 21.3.2015
 * @version 0.5
 */
public class ProcessDialog {

    private Context context;
    private int theme;

    ProgressDialog pDialog;

    public ProcessDialog(Context context) {
        this.context = context;

        this.pDialog = new ProgressDialog(context);
    }

    /**
     * ProcessDialog - A visual helper class for network calls
     * Constructs a ProgressDialog that network related classes can use for a visual
     * representation of processing
     * @param context Context of the calling application
     * @param theme Integer representation of the theme used by the application, voluntary
     */
    public ProcessDialog( Context context, int theme) {
        this.context = context;
        this.theme = theme;

        this.pDialog = new ProgressDialog(context);
    }

    /**
     * initiateProcessDialog
     * Tries to show the processDialog belonging to the helper class
     */
    public void initiateProcessDialog() {
        try {
            this.pDialog.setMessage(this.context.getString(R.string.progressdialog));
            this.pDialog.setCancelable(false);
            this.pDialog.show();
        }
        catch (Exception e) {
            Log.w("Exception", e);
            this.pDialog = null;

        }
    }

    /**
     * closeProcessDialog
     * Tries to close the existing processDialog, if any
     */
    public void closeProcessDialog() {
        if(this.pDialog == null) {
            return;
        }

        try {
            this.pDialog.dismiss();
        }
        catch (Exception e) {
            Log.w("Exception", e);
        }
    }
}
