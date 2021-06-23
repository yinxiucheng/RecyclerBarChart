package com.yxc.barchart.ui.rainbow;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yxc.barchart.R;
import com.yxc.barchart.view.Rainbow;
import com.yxc.barchart.view.WaterDrop;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @since  2019/4/26
 */
public class RainbowActivity extends AppCompatActivity {

    Toolbar toolbar;
    Rainbow mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(LocalDate.now()), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_45dp);
        toolbar.setTitleTextColor(ColorUtil.getResourcesColor(this, R.color.white));
        setSupportActionBar(toolbar);
        mContainer = findViewById(R.id.rainbow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
