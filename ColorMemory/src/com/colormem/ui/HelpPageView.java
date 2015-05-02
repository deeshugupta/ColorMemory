package com.colormem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class HelpPageView extends FragmentActivity {

	private final int NUM_PAGES=2;
	
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	
	public HelpPageView() {
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.help_view_pager);

	        // Instantiate a ViewPager and a PagerAdapter.
	        mPager = (ViewPager) findViewById(R.id.pager);
	        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
	        mPager.setAdapter(mPagerAdapter);
	    }

	    @Override
	    public void onBackPressed() {
	        if (mPager.getCurrentItem() == 0) {
	            Intent intent = new Intent(getApplicationContext(), Launch.class);
	            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				startActivity(intent);
	        } else {
	            // Otherwise, select the previous step.
	            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
	        }
	    }
	
	 /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        

		public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new PageViewer(HelpPageView.this,position,getAssets());
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
