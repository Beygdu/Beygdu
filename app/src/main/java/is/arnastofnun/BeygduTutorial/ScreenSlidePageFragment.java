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

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageContainer);
        imageView.setImageResource(bundle.getInt("imageSource"));

        TextView textView = (TextView) rootView.findViewById(R.id.firstTextView);
        textView.setText(bundle.getInt("firstInfoString"));

        TextView textView1 = (TextView) rootView.findViewById(R.id.secondTextView);
        textView1.setText(bundle.getInt("secondInfoString"));

        return rootView;
    }

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
