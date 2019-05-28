package com.yxc.barchart.ui.waterdrop;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yxc.barchart.R;
import com.yxc.barchart.view.WaterDrop;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.TimeUtil;

import org.joda.time.LocalDate;
/**
 * @author yxc
 * @since  2019/4/26
 */
public class WaterDropActivity extends AppCompatActivity {

    Toolbar toolbar;
    WaterDrop mContainer;

    Button mBtn;
    Button mResetBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_drop);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeUtil.getDateStr(TimeUtil.localDateToTimestamp(LocalDate.now()), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_45dp);
        toolbar.setTitleTextColor(ColorUtil.getResourcesColor(this, R.color.white));
        setSupportActionBar(toolbar);
        mContainer = findViewById(R.id.waterDrop);

        mBtn = findViewById(R.id.btn);
        mResetBtn = findViewById(R.id.restBtn);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainer.resetWaterDrop();
                mContainer.startLevelAnimator();
                mContainer.resetWaterScan();
                mContainer.startScanAnimator();
            }
        });

        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainer.resetWaterDrop();
            }
        });
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
