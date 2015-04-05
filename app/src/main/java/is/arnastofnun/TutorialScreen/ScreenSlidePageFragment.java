package is.arnastofnun.TutorialScreen;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import is.arnastofnun.beygdu.R;


public class ScreenSlidePageFragment extends Fragment {


    Animation anim;

    @Override
    public void onStart() {
        super.onStart();
        TextView tView = (TextView) getView().findViewById(R.id.animTV);
    
        AnimatorSet firstSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                getActivity().getApplicationContext(), R.anim.slideup);
        firstSet.setTarget(tView);
        firstSet.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int layoutId = bundle.getInt("layoutId");

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                layoutId, container, false);


        return rootView;
    }

    public static ScreenSlidePageFragment newInstance(int  id) {

        ScreenSlidePageFragment sSPFragment = new ScreenSlidePageFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", id);
        sSPFragment.setArguments(bundle);

        return sSPFragment;
    }





}
