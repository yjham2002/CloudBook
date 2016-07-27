package com.CBook.CB.cloudbook;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class tutorialActivity extends FragmentActivity implements View.OnClickListener{

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private final static int NUM_PAGES = 4;

    private List<ImageView> dots;

    private static final int dotSize = 20;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mSectionsPagerAdapter = new tutorialActivity.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        addDots();
        selectDot(0);

        Util.setGlobalFont(this, getWindow().getDecorView());
    }

    public void addDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout)findViewById(R.id.dots);

        for(int i = 0; i < NUM_PAGES; i++) {
            ImageView dot = new ImageView(this);
            Drawable drawable = getResources().getDrawable(R.drawable.pager_dot_not_selected);
            drawable = resize(drawable);
            dot.setImageDrawable(drawable);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10,10,10,10);
            dotsLayout.addView(dot, params);

            dots.add(dot);
        }


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, dotSize, dotSize, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for(int i = 0; i < NUM_PAGES; i++) {
            int drawableId = (i == idx) ? (R.drawable.pager_dot_selected):(R.drawable.pager_dot_not_selected);
            Drawable drawable = res.getDrawable(drawableId);
            drawable = resize(drawable);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment temp = null;
            Bundle args = new Bundle();
            args.putInt("key", position);
            switch(position){
                case 0: temp = new fragment_tutorial_01(); break;
                case 1: temp = new fragment_tutorial_01(); break;
                case 2: temp = new fragment_tutorial_01(); break;
                case 3: temp = new fragment_tutorial_01(); break;
                default: temp = null; break;
            }
            temp.setArguments(args);
            return temp;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (position+1)+"/"+getCount();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) finish();
        return super.onKeyDown(keyCode, event);
    }

}