package is.arnastofnun.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import is.arnastofnun.beygdu.R;

/**
 * @author Daniel Pall
 */
public class CustomErrorDialog extends DialogFragment{

    public static CustomErrorDialog newInstance(int title){
        CustomErrorDialog customErrorDialog = new CustomErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("title",title);
        customErrorDialog.setArguments(bundle);
        return customErrorDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(getActivity());

        diagBuilder.setTitle(title);

        diagBuilder.setNegativeButton(R.string.alert_dialog_ok,
                new DialogInterface.OnClickListener() {
                    /**
                     * a listener for the Ok button, nothing happens except the dialog closes
                     * This is for notifying the user of the error that created the dialog
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });

        return diagBuilder.create();
//        return super.onCreateDialog(savedInstanceState);
    }
}
