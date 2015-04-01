package is.arnastofnun.TutorialScreen;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.arnastofnun.beygdu.R;


public class TutorialThree extends Fragment {

    public static TutorialThree newInstance(int param1, String param2) {

        TutorialThree tThree = new TutorialThree();

        Bundle args = new Bundle();
        args.putInt("layoutId", param1);
        args.putString("contentFlag", param2);
        tThree.setArguments(args);

        return tThree;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int layoutId = bundle.getInt("layoutId");
        String contentFlag = bundle.getString("contentFlag");

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                layoutId, container, false);

        return rootView;

    }



}
