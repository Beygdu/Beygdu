package is.arnastofnun.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

import is.arnastofnun.beygdu.R;

/**
 * Created by arnarjons on 13.4.2015.
 */
public class NotificationDialog extends DialogFragment {

    private final String notificationTitle = "Beygdu";
    private final String errorTitle = "Villa!";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();
        boolean isError = bundle.getBoolean("isError");
        if(isError) builder.setTitle(errorTitle);
        else builder.setTitle(notificationTitle);

        builder.setMessage(bundle.getString("message"));
        //builder.setIcon(getResources().getDrawable(R.drawable.bicon));
        builder.setNegativeButton(getResources().getString(R.string.NegativeButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag);
        }
    }
}
