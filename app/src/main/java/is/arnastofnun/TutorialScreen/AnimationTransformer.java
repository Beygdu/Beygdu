package is.arnastofnun.TutorialScreen;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by arnarjons on 5.4.2015.
 */
public class AnimationTransformer implements ViewPager.PageTransformer {

    private static final float MINIMUM_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pWidth = view.getWidth();

        if(position < -1) {
            view.setAlpha(0);
        }
        else if(position <= 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        }
        else if(position <= 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(pWidth * -position);
            float sFactor =
                    MINIMUM_SCALE + (1 - MINIMUM_SCALE) * (1 - Math.abs(position));
            view.setScaleX(sFactor);
            view.setScaleY(sFactor);
        }
        else {
            view.setAlpha(0);
        }


    }

}
