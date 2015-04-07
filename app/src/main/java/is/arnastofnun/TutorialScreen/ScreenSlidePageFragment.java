package is.arnastofnun.TutorialScreen;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int layoutId = bundle.getInt("layoutId");

        View rootView = inflater.inflate(
                layoutId, container, false);


        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageContainer);
        imageView.setImageResource(bundle.getInt("imageSource"));

        TextView textView = (TextView) rootView.findViewById(R.id.firstTextView);
        textView.setText(bundle.getInt("firstInfoString"));

        TextView textView1 = (TextView) rootView.findViewById(R.id.secondTextView);
        textView1.setText(bundle.getInt("secondInfoString"));


        return rootView;
    }

    public static ScreenSlidePageFragment newInstance(int id, int imageId, int firstStringId,
                                                      int secondStringId) {

        ScreenSlidePageFragment sSPFragment = new ScreenSlidePageFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", id);
        bundle.putInt("imageSource", imageId);
        bundle.putInt("firstInfoString", firstStringId);
        bundle.putInt("secondInfoString", secondStringId);

        sSPFragment.setArguments(bundle);

        return sSPFragment;
    }






}
