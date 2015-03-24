package is.arnastofnun.utils;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Arnar Jonsson
 * @since 22.3.2015
 * @version 0.5
 */
public class NetworkStateListener {

    private Context context;

    /**
     * A ConnectivityManager belonging to the calling application
     */
    ConnectivityManager cManager;

    /**
     * NetworkStateListener - A helper class for network calls
     * Network calling classes use it to check of a network state is active
     * @param context Context of the calling application
     */
    public NetworkStateListener(Context context) {
        this.context = context;

        cManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

    }

    /**
     * isConnectionActive - checks for network state
     * @return True if connection is establishable, false otherwise
     */
    public boolean isConnectionActive() {

        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        if(nInfo == null) {
            return false;
        }

        return true;
    }


}
