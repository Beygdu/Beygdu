package is.arnastofnun.BeygduTutorial;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author Arnar Jonsson
 * @version 0.2
 * @since  11.4.2015.
 * Refactored since 5.4.2015, Arnar Jonsson, Daniel Pall Johannson
 * AnimationTransformer
 * Handles the animation of TutorialActivity's ViewPager fragment switching
 * Owns one animation, which is played backwards on a 'back'-swipe
 */
public class AnimationTransformer implements ViewPager.PageTransformer {

    private static final float MINIMUM_SCALE = 0.75f;

    public void transformPage(View view, float pos) {
        int pWidth = view.getWidth();

        if(pos < -1) {
            view.setAlpha(0);
        }
        else if(pos <= 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        }
        else if(pos <= 1) {
            view.setAlpha(1 - pos);
            view.setTranslationX(pWidth * -pos);
            float sFactor =
                    MINIMUM_SCALE + (1 - MINIMUM_SCALE) * (1 - Math.abs(pos));
            view.setScaleX(sFactor);
            view.setScaleY(sFactor);
        }
        else {
            view.setAlpha(0);
        }
    }

}
