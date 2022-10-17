
package com.yxc.barchart.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.yxc.barchart.R;
import com.yxc.barchart.map.location.LocationActivity;
import com.yxc.barchart.map.location.RecordListActivity;
import com.yxc.barchart.map.location.database.RealmDbHelper;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.ui.bezier.BezierActivity;
import com.yxc.barchart.ui.hrm.HrmActivity;
import com.yxc.barchart.ui.line.LineActivity;
import com.yxc.barchart.ui.rainbow.RainbowActivity;
import com.yxc.barchart.ui.sleep.SleepActivity;
import com.yxc.barchart.ui.step.StepActivity;
import com.yxc.barchart.ui.waterdrop.WaterDropActivity;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        permissionApply();
        Log.d("MainActivity", "SDCard:" + Environment.getExternalStorageDirectory());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }

    private void permissionApply() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        } else {
            RealmDbHelper.initSDCard("location", 1);
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(LocalDate.now()), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_navigation_left_black_45dp);
        setSupportActionBar(toolbar);
        clickHrm();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void clickRainbow(View view){
        startActivity(new Intent(this, RainbowActivity.class));
    }

    public void clickGaoDeMap(View view) {
        startActivity(new Intent(this, LocationActivity.class));
    }

    public void clickAMapRecord(View view) {
        intentToRecord(true);
    }

    public void clickMapboxRecord(View view) {intentToRecord(false); }

    public void clickHrm(){
        findViewById(R.id.hrm_chart_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HrmActivity.class));
            }
        });
    }

    private void intentToRecord(boolean useGaoDe) {
        Intent intent = new Intent(this, RecordListActivity.class);
        intent.putExtra(LocationConstants.KEY_RECORD_TYPE, LocationConstants.SPORT_TYPE_RUNNING);
        intent.putExtra("useGaoDe", useGaoDe);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RealmDbHelper.initSDCard("location", 1);
    }

}
