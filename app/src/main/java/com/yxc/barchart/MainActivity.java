
package com.yxc.barchart;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxc.barchart.tab.OnTabSelectListener;
import com.yxc.barchart.tab.TopTabLayout;
import com.yxc.barchart.ui.DayFragment;
import com.yxc.barchart.ui.MonthFragment;
import com.yxc.barchart.ui.WeekFragment;
import com.yxc.barchart.ui.YearFragment;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.util.ColorUtil;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.TextUtil;
import com.yxc.barchartlib.util.TimeUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DayFragment.OnDaySelectListener,
        WeekFragment.OnWeekSelectListener, MonthFragment.OnMonthSelectListener, YearFragment.OnYearSelectListener {

    public static final int VIEW_DAY = 0;
    public static final int VIEW_WEEK = 1;
    public static final int VIEW_MONTH = 2;
    public static final int VIEW_YEAR = 3;
    public int mType;
    private String[] mTitles = {"日", "周", "月", "年"};

    TopTabLayout mTabLayout;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    TextView textTitle;
    TextView txtCountStep;
    ImageView imgLast;
    ImageView imgNext;
    FrameLayout container;
    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTableLayout();
        setListener();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.topTabLayout);
        txtLeftLocalDate = findViewById(R.id.txt_left_local_date);
        txtRightLocalDate = findViewById(R.id.txt_right_local_date);
        textTitle = findViewById(R.id.txt_layout);
        txtCountStep = findViewById(R.id.txt_count_Step);
        imgLast = findViewById(R.id.img_left);
        imgNext = findViewById(R.id.img_right);
        container = findViewById(R.id.container);

        switchTab(DayFragment.class, "DayFragment");
        showDisplayEntries("DayFragment");
    }

    //滑动监听
    private void setListener() {
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imgLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                if (position == VIEW_DAY) {// 创建 月视图的数据
                    switchTab(DayFragment.class, "DayFragment");
//                    showDisplayEntries("DayFragment");
                } else if (position == VIEW_WEEK) {//创建Week视图的数据
                    switchTab(WeekFragment.class, "WeekFragment");
//                    showDisplayEntries("WeekFragment");
                } else if (position == VIEW_MONTH) {//创建Month视图的数据
                    switchTab(MonthFragment.class, "MonthFragment");
//                    showDisplayEntries("MonthFragment");
                } else if (position == VIEW_YEAR) {//创建Year视图的数据
                    switchTab(YearFragment.class, "YearFragment");
//                    showDisplayEntries("YearFragment");
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
                addListener(fragment);
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
        showDisplayEntries(tag);
    }

    public void showDisplayEntries(String tag){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment instanceof DayFragment) {
            ((DayFragment) fragment).showDisplayEntries();
        }

        if (fragment instanceof WeekFragment) {
            ((WeekFragment) fragment).showDisplayEntries();
        }

        if (fragment instanceof MonthFragment) {
            ((MonthFragment) fragment).showDisplayEntries();
        }

        if (fragment instanceof YearFragment) {
            ((YearFragment) fragment).showDisplayEntries();
        }
    }

    private void addListener(BaseFragment fragment) {
        if (fragment instanceof DayFragment) {
            ((DayFragment) fragment).setOnDaySelectListener(this);
        }

        if (fragment instanceof WeekFragment) {
            ((WeekFragment) fragment).setOnWeekSelectListener(this);
        }

        if (fragment instanceof MonthFragment) {
            ((MonthFragment) fragment).setOnMonthSelectListener(this);
        }

        if (fragment instanceof YearFragment) {
            ((YearFragment) fragment).setOnYearSelectListener(this);
        }
    }

    @Override
    public void onDaySelect(List<BarEntry> displayEntries) {
        displayDateAndStep(displayEntries, VIEW_DAY);

    }

    @Override
    public void onWeekSelect(List<BarEntry> displayEntries) {
        displayDateAndStep(displayEntries, VIEW_WEEK);
    }

    @Override
    public void onSelectMonth(List<BarEntry> displayEntries) {
        displayDateAndStep(displayEntries, VIEW_MONTH);
    }

    @Override
    public void onYearSelect(List<BarEntry> displayEntries) {
        displayDateAndStep(displayEntries, VIEW_YEAR);
    }


    private void displayDateAndStep(List<BarEntry> displayEntries, int mType) {
        //todo 调试显示用的
        BarEntry leftBarEntry = displayEntries.get(0);
        BarEntry rightBarEntry = displayEntries.get(displayEntries.size() - 1);
        txtLeftLocalDate.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));
        txtRightLocalDate.setText(TimeUtil.getDateStr(rightBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));

        if (mType == VIEW_MONTH) {
            String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日");
            String patternStr = "yyyy年MM月dd日";
            if (TimeUtil.isSameMonth(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月"));
            } else if (TimeUtil.isSameYear(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                patternStr = "MM月dd日";
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
                String connectStr = "至";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            } else {
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
                String connectStr = "至";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            }
        } else if (mType == VIEW_WEEK) {
            String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日");
            String patternStr = "yyyy年MM月dd日";
            if (TimeUtil.isSameMonth(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                patternStr = "dd日";
            } else if (TimeUtil.isSameYear(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                patternStr = "MM月dd日";
            }
            String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
            String connectStr = "至";
            textTitle.setText(beginDateStr + connectStr + endDateStr);
        } else if (mType == VIEW_DAY) {
            String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日 HH:mm");
            String patternStr = "yyyy年MM月dd日 HH:mm";
            if (TimeUtil.isTheSameDay(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日"));
            } else {
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
                String connectStr = " - ";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            }
        } else if (mType == VIEW_YEAR) {
            if (TimeUtil.isSameYear(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年"));
            } else {
                String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy/MM/dd");
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, "yyyy/MM/dd");
                String connectStr = " -- ";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            }
        }

        long count = 0;
        for (int i = 0; i < displayEntries.size(); i++) {
            BarEntry entry = displayEntries.get(i);
            count += entry.getY();
        }
        int averageStep = (int) (count / displayEntries.size());
        String childStr = DecimalUtil.addComma(Integer.toString(averageStep));
        String parentStr = String.format(getString(R.string.str_count_step), childStr);
        SpannableStringBuilder spannable = TextUtil.getSpannableStr(this, parentStr, childStr, 24);
        txtCountStep.setText(spannable);
    }
}
