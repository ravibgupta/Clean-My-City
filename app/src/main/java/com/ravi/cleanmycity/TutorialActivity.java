package com.ravi.cleanmycity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.ravi.cleanmycity.fragment.FragmentOne;
import com.ravi.cleanmycity.fragment.FragmentThree;
import com.ravi.cleanmycity.fragment.FragmentTwo;
import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends FragmentActivity {

    MyPageAdapter pageAdapter;
    TextView buttonFirst;
    ImageView buttonSecond;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        List<Fragment> fragments = getFragments();
        final CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        final ViewPager pager =
                (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        defaultIndicator.setViewPager(pager);

        buttonFirst = (TextView) findViewById(R.id.buttonFirst);
        buttonSecond = (ImageView) findViewById(R.id.buttonSecond);

        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1, true);
            }
        });
    }

    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(FragmentOne.newInstance());
        fList.add(FragmentTwo.newInstance());
        fList.add(FragmentThree.newInstance());
        return fList;
    }

    @Override
    public void onBackPressed() {

    }
}