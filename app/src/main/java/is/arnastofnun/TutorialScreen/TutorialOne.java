package is.arnastofnun.TutorialScreen;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.arnastofnun.beygdu.R;


public class TutorialOne extends Fragment {

    public static TutorialOne newInstance(int param1, String param2) {

        TutorialOne tOne = new TutorialOne();

        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", param1);
        bundle.putString("contentFlag", param2);
        tOne.setArguments(bundle);

        return tOne;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int layoutId = bundle.getInt("layoutId");
        String contenFlag = bundle.getString("contentFlag");

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                layoutId, container, false);

        return rootView;

    }



}
