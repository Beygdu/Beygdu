package is.arnastofnun.TutorialScreen;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.arnastofnun.beygdu.R;


public class TutorialTwo extends Fragment {

    public static TutorialTwo newInstance(int param1, String param2) {

        TutorialTwo tTwo = new TutorialTwo();

        Bundle args = new Bundle();
        args.putInt("layoutId", param1);
        args.putString("contentFlag", param2);
        tTwo.setArguments(args);

        return tTwo;
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
