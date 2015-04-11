package is.arnastofnun.BeygduTutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import is.arnastofnun.beygdu.R;

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSliderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_one,
                            R.drawable.smiley,
                            R.string.TF01,
                            R.string.TF02);
                case 1:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_two,
                            R.drawable.smiley,
                            R.string.TF11,
                            R.string.TF12);
                case 2:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_three,
                            R.drawable.smiley,
                            R.string.TF21,
                            R.string.TF22);
                case 3:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_four,
                            R.drawable.smiley,
                            R.string.TF31,
                            R.string.TF32);
                case 4:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_one,
                            R.drawable.smiley,
                            R.string.TF41,
                            R.string.TF42);
                case 5:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_two,
                            R.drawable.smiley,
                            R.string.TF51,
                            R.string.TF52);
                case 6:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_three,
                            R.drawable.smiley,
                            R.string.TF61,
                            R.string.TF62);
                case 7:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_four,
                            R.drawable.smiley,
                            R.string.TF71,
                            R.string.TF72);
                default:
                    return ScreenSlidePageFragment.newInstance(R.layout.fragment_slide_portrait,
                            R.layout.fragment_slide_landscape_one,
                            R.drawable.smiley,
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
