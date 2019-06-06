
package com.yxc.barchart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yxc.barchart.R;
import com.yxc.barchart.map.MapsGoogleActivity;
import com.yxc.barchart.map.location.LocationMarkerActivity;
import com.yxc.barchart.ui.bezier.BezierActivity;
import com.yxc.barchart.ui.line.LineActivity;
import com.yxc.barchart.ui.sleep.SleepActivity;
import com.yxc.barchart.ui.step.StepActivity;
import com.yxc.barchart.ui.waterdrop.WaterDropActivity;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(LocalDate.now()), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_navigation_left_black_45dp);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void clickStep(View view) {
        startActivity(new Intent(this, StepActivity.class));
    }

    public void clickSleep(View view) {
        startActivity(new Intent(this, SleepActivity.class));
    }

    public void clickBezier(View view) {
        startActivity(new Intent(this, BezierActivity.class));
    }

    public void clickLine(View view) {
        startActivity(new Intent(this, LineActivity.class));
    }

    public void clickWaterDrop(View view) {
        startActivity(new Intent(this, WaterDropActivity.class));
    }

    public void clickGaoDeMap(View view){
        startActivity(new Intent(this, LocationMarkerActivity.class));
    }

    public void clickGoogleMap(View view) {
        startActivity(new Intent(this, MapsGoogleActivity.class));
    }

}
