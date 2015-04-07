package is.arnastofnun.TutorialScreen;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import is.arnastofnun.beygdu.R;


public class ScreenSlidePageFragment extends Fragment {

    //private int layoutPortId;
    //private int layoutLandId;
    private View rootView;
    private ViewGroup containter;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.containter = container;
        int layoutId;
        Bundle bundle = getArguments();

        if(getResources().getConfiguration().orientation == 1) {
            layoutId = bundle.getInt("layoutPortId");
        }
        else {
            layoutId = bundle.getInt("layoutLandId");
        }


        rootView = inflater.inflate(
                layoutId, container, false);


        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageContainer);
        imageView.setImageResource(bundle.getInt("imageSource"));

        TextView textView = (TextView) rootView.findViewById(R.id.firstTextView);
        textView.setText(bundle.getInt("firstInfoString"));

        TextView textView1 = (TextView) rootView.findViewById(R.id.secondTextView);
        textView1.setText(bundle.getInt("secondInfoString"));


        return rootView;
    }
    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        containter.removeAllViews();
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                break;
        }

    }
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
