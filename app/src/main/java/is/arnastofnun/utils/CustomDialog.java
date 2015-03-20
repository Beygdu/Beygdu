package is.arnastofnun.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by arnarjons on 19.3.2015.
 */
public class CustomDialog extends DialogFragment {

    DialogListener dListener;
    private String selectedItem = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] descriptionActions = getArguments().getStringArray("descriptionActions");

        builder.setTitle(getArguments().getString("title"));

        builder.setSingleChoiceItems(getArguments().getStringArray("descriptions"),
                -1 , new DialogInterface.OnClickListener() {
                    /**
                     * a listener which listens to which result the user chooses from the dialog
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItem = descriptionActions[which];
                    }
                });

        builder.setNegativeButton(getArguments().getString("negativeButtonText"), new DialogInterface.OnClickListener() {
            /**
             * a listener for the cancel button, nothing happens except the dialog closes
             */
            public void onClick(DialogInterface dialog, int id) {
                // Do nothing
            }
        });

        builder.setPositiveButton(getArguments().getString("positiveButtonText"), new DialogInterface.OnClickListener() {
            /**
             * a listener if the user presses the continue button
             * If nothing is chosem then the dialog closes
             */
            public void onClick(DialogInterface dialog, int id) {
                if( selectedItem != null) {
                    dListener.onPositiveButtonClick(selectedItem, getArguments().getInt("id"));
                }
            }
        });

        return builder.create();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            dListener = (DialogListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " needs to implement a DialogListener");
        }
    }

    public interface DialogListener {
        public void onPositiveButtonClick(String selectedItem, int id);
        public void onNegativeButtonClick(String errorString);
    }

}
