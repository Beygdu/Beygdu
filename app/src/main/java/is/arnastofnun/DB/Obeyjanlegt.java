package is.arnastofnun.DB;

import android.content.Context;
import android.util.Log;

/**
 * Created by arnarjons on 23.8.2015.
 * Tilgangurinn med thessu er ad komast framhja try - catch i mainactivity
 */
public class Obeyjanlegt {

    private Context context;

    public Obeyjanlegt(Context context) {
        this.context = context;
    }

    public boolean isObeygjanlegt(String word) {

        try {
            DBController controller = new DBController(context);
            if( controller.fetchObeygjanlegt(word) == null ) return false;
            else return true;
        }
        catch (Exception e) {
            Log.w("Obeygjanleg ordaleit", e);
        }
        return false;
    }
}
