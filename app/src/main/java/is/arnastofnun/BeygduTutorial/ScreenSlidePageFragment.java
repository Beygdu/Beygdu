package is.arnastofnun.BeygduTutorial;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import is.arnastofnun.beygdu.R;

/**
 * @author Arnar Jonsson
 * @version 0.2
 * @since  11.4.2015.
 * Refactored since 5.4.2015, Arnar Jonsson, Daniel Pall Johannson
 * ScreenSlidePageFragment
 * A fragment design to fit TutorialActivity's Viewpager
 * Called with :
 *          ScreenSlidePageFragment.newInstance(PORTRAIT_FRAGMENT id,
 *           LANDSCAPE_FRAGMENT id (may be the same fragment as portrait),
 *           IMAGE id,
 *           STRING id,
 *           STRING id);
 *           id == R.X.X
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

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageContainer);
        imageView.setImageResource(bundle.getInt("imageSource"));

        TextView textView = (TextView) rootView.findViewById(R.id.firstTextView);
        textView.setText(bundle.getInt("firstInfoString"));

        TextView textView1 = (TextView) rootView.findViewById(R.id.secondTextView);
        textView1.setText(bundle.getInt("secondInfoString"));

        return rootView;
    }

    /**
     *
     * @param oriPortId R.layout.PORTRAITLAYOUT
     * @param oriLandId R.layout.LANDSCAPELAYOUT
     * @param imageId R.drawable.IMAGE
     * @param firstStringId R.strings.MAIN STRING
     * @param secondStringId R.strings.SECONDARY STRING
     * @return A new fragment with a portrait and a lanscape layout, containing
     *                  1 Image and 2 Textfields with content
     */
    public static ScreenSlidePageFragment newInstance(int oriPortId, int oriLandId,
                                                      int imageId, int firstStringId,
                                                      int secondStringId) {

        ScreenSlidePageFragment sSPFragment = new ScreenSlidePageFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("layoutPortId", oriPortId);
        bundle.putInt("layoutLandId", oriLandId);
        bundle.putInt("imageSource", imageId);
        bundle.putInt("firstInfoString", firstStringId);
        bundle.putInt("secondInfoString", secondStringId);

        sSPFragment.setArguments(bundle);

        return sSPFragment;
    }

}
