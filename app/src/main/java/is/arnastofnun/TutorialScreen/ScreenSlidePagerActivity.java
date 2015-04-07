package is.arnastofnun.TutorialScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import is.arnastofnun.beygdu.R;

public class ScreenSlidePagerActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new AnimationTransformer());
    }

    @Override
    public void onBackPressed() {
        if(mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return ScreenSlidePageFragment.newInstance(R.layout.fragment_tutorial_one,
                        R.drawable.placeholderimage,
                        R.string.placeHolderFragmentText,
                        R.string.placeHolderFragmentText);
                case 1: return ScreenSlidePageFragment.newInstance(R.layout.fragment_tutorial_two,
                        R.drawable.placeholderimage,
                        R.string.placeHolderFragmentText,
                        R.string.placeHolderFragmentText);
                case 2: return ScreenSlidePageFragment.newInstance(R.layout.fragment_tutorial_three,
                        R.drawable.placeholderimage,
                        R.string.placeHolderFragmentText,
                        R.string.placeHolderFragmentText);
                //TODO : Add instance for every page for custome layouts
                default : return ScreenSlidePageFragment.newInstance(R.layout.fragment_tutorial_one,
                        R.drawable.placeholderimage,
                        R.string.placeHolderFragmentText,
                        R.string.placeHolderFragmentText);
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }



    }


}
