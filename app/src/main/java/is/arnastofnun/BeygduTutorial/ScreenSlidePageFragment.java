package is.arnastofnun.BeygduTutorial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.arnastofnun.beygdu.R;

/**
 * Created by arnarjons on 11.4.2015.
 */
public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int layoutId;
        Bundle bundle = getArguments();

        if(getResources().getConfiguration().orientation == 1) {
            layoutId = bundle.getInt("layoutPortId");
        }
        else {
            layoutId = bundle.getInt("layoutLandId");
        }

        View rootView = inflater.inflate(layoutId, container, false);

        return rootView;
    }

}
