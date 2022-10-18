package com.yxc.barchart.ui.hrm;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.yxc.barchart.R;
import com.yxc.barchart.tab.TopTabLayout;
import com.yxc.barchart.ui.base.BaseChartFragment;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;


/**
 * 心电图
 */
public class HrmLandScapeActivity extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout container;
    private BaseChartFragment currentFragment;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrm);
        initView();
    }

    private void initView() {
        container = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolBar);
        txtTitle = findViewById(R.id.title);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(LocalDate.now()), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_navigation_left_black_45dp);
        txtTitle.setText("心电图");
        setSupportActionBar(toolbar);

        switchTab(HrmDayFragment.class, "HrmDayFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (currentFragment != null) {
            currentFragment.resetSelectedEntry();
        }
    }
}
