
package com.yxc.barchart;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.yxc.barchart.tab.OnTabSelectListener;
import com.yxc.barchart.tab.TopTabLayout;
import com.yxc.barchart.ui.DayFragment;
import com.yxc.barchart.ui.MonthFragment;
import com.yxc.barchart.ui.WeekFragment;
import com.yxc.barchart.ui.YearFragment;
import com.yxc.barchartlib.util.ColorUtil;

public class MainActivity extends AppCompatActivity {

    private String[] mTitles = {"日", "周", "月", "年"};

    TopTabLayout mTabLayout;
    FrameLayout container;
    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTableLayout();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.topTabLayout);
        container = findViewById(R.id.container);
        switchTab(DayFragment.class, "DayFragment");
    }

    private void initTableLayout() {
        mTabLayout.setCurrentTab(0);
        mTabLayout.setIndicatorColor(ColorUtil.getResourcesColor(this, R.color.pink));
        mTabLayout.setTextUnselectColor(ColorUtil.getResourcesColor(this, R.color.tab_checked));
        mTabLayout.setDividerColor(ColorUtil.getResourcesColor(this, R.color.pink));
        mTabLayout.setTabData(mTitles);

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {// 创建 月视图的数据
                    switchTab(DayFragment.class, "DayFragment");
                } else if (position == 1) {//创建Week视图的数据
                    switchTab(WeekFragment.class, "WeekFragment");
                } else if (position == 2) {//创建Month视图的数据
                    switchTab(MonthFragment.class, "MonthFragment");
                } else if (position == 3) {//创建Year视图的数据
                    switchTab(YearFragment.class, "YearFragment");
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mTabLayout.setCurrentTab(0);
    }

    public void switchTab(Class clz, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        if (fragment == null) {
            try {
                fragment = (BaseFragment) clz.newInstance();
                ft.add(R.id.container, fragment, tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
        currentFragment = fragment;
    }

}
