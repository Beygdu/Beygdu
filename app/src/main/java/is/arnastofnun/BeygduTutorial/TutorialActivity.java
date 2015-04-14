package is.arnastofnun.BeygduTutorial;

import android.support.v7.app.ActionBar;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import is.arnastofnun.beygdu.R;

/**
 * @author Arnar Jonsson
 * @version 0.2
 * @since 11.4.2015
 * Refactored since 5.4.2015, Arnar Jonsson, Daniel Pall Johannson
 * TutorialActivity
 * A standalone activity that contains a ViewPager and a custom made Actionbar
 * (because the default ones are shit)
 * The ViewPager holds fragments that show users a tutorial on how to use
 * the application.
 * The fragment design is ancient, a new instance of it is created every time you swipe,
 * so the parameters need to change for every swipe position switch, so the newinstance takes all
 * information it needs as parameters>
 *          R.layout ids for portrait and landscape layouts
 *          R.drawable id for the image displayed
 *          R.string ids for textfield information displaying
 */
public class TutorialActivity extends ActionBarActivity {

    private static final int NUM_PAGES = 8;

    private ViewPager vPager;

    private PagerAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial);


        vPager = (ViewPager) findViewById(R.id.pager);
        pAdapter = new ScreenSliderPagerAdapter(getSupportFragmentManager());
        vPager.setAdapter(pAdapter);
        vPager.setPageTransformer(true, new AnimationTransformer());

        /**
         * Implementation of the custom action bar, and its actions
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View actionBarView = layoutInflater.inflate(R.layout.actionbar_tutorial, null);

        ImageView forwardImage = (ImageView) actionBarView.findViewById(R.id.forwardImage);
        forwardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vPager.getCurrentItem() < NUM_PAGES) vPager.setCurrentItem(vPager.getCurrentItem() + 1);
            }
        });

        ImageView backwardImage = (ImageView) actionBarView.findViewById(R.id.backwardImage);
        backwardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vPager.getCurrentItem() > 0) vPager.setCurrentItem(vPager.getCurrentItem() - 1);
            }
        });

        TextView quitView = (TextView) actionBarView.findViewById(R.id.quitTextView);
        quitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);


    }

    /**
     * A PagerAdapter thad handles the ViewPagers logic
     * New instances of the ViewPagers fragments should only be created here
     */
    private class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSliderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            //TODO : Create content
            switch (pos) {
                case 0:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_one,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                case 1:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_two,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                case 2:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_three,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                case 3:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_one,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                case 4:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_two,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                case 5:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_three,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                case 6:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_one,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                case 7:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_two,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
                default:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_one,
                            R.drawable.tut_placement,
                            R.string.TF01,
                            R.string.TF02);
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
