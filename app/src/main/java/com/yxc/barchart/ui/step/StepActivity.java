
package com.yxc.barchart.ui.step;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.yxc.barchart.R;
import com.yxc.barchart.tab.OnTabSelectListener;
import com.yxc.barchart.tab.TopTabLayout;
import com.yxc.barchart.ui.base.BaseChartFragment;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

public class StepActivity extends AppCompatActivity {

    private String[] mTitles = {"日", "周", "月", "年"};

    TopTabLayout mTabLayout;
    Toolbar toolbar;
    FrameLayout container;
    private BaseChartFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        initView();
        initTableLayout();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.topTabLayout);
        container = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(LocalDate.now()), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_navigation_left_black_45dp);
        setSupportActionBar(toolbar);

        switchTab(StepDayFragment.class, "DayFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTableLayout() {
        mTabLayout.setCurrentTab(0);
        mTabLayout.setIndicatorColor(ColorUtil.getResourcesColor(this, R.color.red));
        mTabLayout.setTextUnselectColor(ColorUtil.getResourcesColor(this, R.color.red));
        mTabLayout.setDividerColor(ColorUtil.getResourcesColor(this, R.color.red));
        mTabLayout.setTabData(mTitles);

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {// 创建 月视图的数据
                    switchTab(StepDayFragment.class, "DayFragment");
                } else if (position == 1) {//创建Week视图的数据
                    switchTab(StepWeekFragment.class, "WeekFragment");
                } else if (position == 2) {//创建Month视图的数据
                    switchTab(StepMonthFragment.class, "MonthFragment");
                } else if (position == 3) {//创建Year视图的数据
                    switchTab(StepYearFragment.class, "YearFragment");
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
        BaseChartFragment fragment = (BaseChartFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null) {
            currentFragment.resetSelectedEntry();
            ft.hide(currentFragment);
        }
        if (fragment == null) {
            try {
                fragment = (BaseChartFragment) clz.newInstance();
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

    @Override
    protected void onStop() {
        super.onStop();
        if (currentFragment != null){
            currentFragment.resetSelectedEntry();
        }
    }
}
