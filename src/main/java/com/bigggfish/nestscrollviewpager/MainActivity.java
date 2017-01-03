package com.bigggfish.nestscrollviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {

    private ViewPager vp_main;
    private Button btn_main;
    private SimpleViewPagerIndicator indicator;
    private NestedScrollTopView id_main_topview;
    private TabFragment[] tabFragments = new TabFragment[2];

    private int mTopHeight;
    private int mScaledTouchSlop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabFragments[0] = new TabFragment();
        tabFragments[1] = new TabFragment();
        vp_main = (ViewPager) findViewById(R.id.id_main_viewpager);
        vp_main.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        btn_main = (Button) findViewById(R.id.btn_main);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "CLick Button", Toast.LENGTH_SHORT).show();
            }
        });

        //指示器
        indicator = (SimpleViewPagerIndicator) findViewById(R.id.id_main_indicator);
        indicator.setTitles(new String[]{"详情", "目录"});
        indicator.setOnItemClickListener(new SimpleViewPagerIndicator.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        vp_main.setCurrentItem(0);
                        break;
                    case 1:
                        vp_main.setCurrentItem(1);
                        break;
                }
            }
        });
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //TopView
        id_main_topview = (NestedScrollTopView) findViewById(R.id.id_main_topview);
        mTopHeight = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 240, getResources()
                        .getDisplayMetrics()));
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

    }

    private void topViewScroll(int dx, int dy) {
        int scrollHeight = id_main_topview.getScrollY() + dy;
        if (scrollHeight < 0)
            scrollHeight = 0;
        if (scrollHeight > mTopHeight)
            scrollHeight = mTopHeight;
        id_main_topview.scrollTo(dx, scrollHeight);
    }

    class OnRecyclerViewScrollListener implements TabFragment.OnRecyclerViewScrollListener {

        @Override
        public void onScroll(int firstPosition, int dx, int dy) {

            if (dy > 0 && id_main_topview.getScrollY() < mTopHeight) {
                topViewScroll(dx, dy);
                tabFragments[0].setRecyclerTopPadding(mTopHeight - id_main_topview.getScrollY());
                tabFragments[1].setRecyclerTopPadding(mTopHeight - id_main_topview.getScrollY());
                return;
            }
            if (dy < 0 && id_main_topview.getScrollY() >= 0 && firstPosition <= 0) {
                topViewScroll(dx, dy);
                tabFragments[0].setRecyclerTopPadding(mTopHeight - id_main_topview.getScrollY());
                tabFragments[1].setRecyclerTopPadding(mTopHeight - id_main_topview.getScrollY());
                return;
            }
        }

        @Override
        public void onMoveScroll(int firstPosition, int dx, int dy) {
            if (firstPosition <= 0 && dy > 0 && Math.abs(dy) > mScaledTouchSlop && id_main_topview.getScrollY() >= 0) {
                topViewScroll(0, -dy);
                tabFragments[0].setRecyclerTopPadding(mTopHeight - id_main_topview.getScrollY());
                tabFragments[1].setRecyclerTopPadding(mTopHeight - id_main_topview.getScrollY());
            }
        }

        @Override
        public void onFling(int velocityY) {
            id_main_topview.fling(velocityY);
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                tabFragments[0].setmListener(new OnRecyclerViewScrollListener());
                return tabFragments[0];
            }
            if (position == 1) {
                tabFragments[1].setmListener(new OnRecyclerViewScrollListener());
                return tabFragments[1];
            }
            return tabFragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }

}

