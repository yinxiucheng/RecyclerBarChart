package com.yxc.barchart.map.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.util.LocationComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordLocation;
import com.yxc.commonlib.util.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有轨迹list展示activity
 */
public class RecordActivity extends Activity implements OnItemClickListener {

    private RecordAdapter mAdapter;
    private ListView mAllRecordListView;

    private List<Record> mAllRecord = new ArrayList<>();

    public static final String RECORD_ID = "record_id";

    public int recordType = LocationConstants.SPORT_TYPE_RUNNING;

    private TextView rightTitleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordlist);
        mAllRecordListView = findViewById(R.id.recordlist);
        rightTitleBtn = findViewById(R.id.title_tv_option);

        recordType = getIntent().getIntExtra(LocationConstants.KEY_RECORD_TYPE, -1);
        searchAllRecordFromDB();

        invokeRightBtn();
        mAdapter = new RecordAdapter(this, mAllRecord);
        mAllRecordListView.setAdapter(mAdapter);
        mAllRecordListView.setOnItemClickListener(this);
    }

    private void invokeRightBtn() {
        Record record = LocationDBHelper.getLastRecord(recordType);
        final RecordLocation recordLocation = LocationDBHelper.getLastItem(recordType);
        if (record == null || recordLocation == null){
            rightBtnStartSport();
            return;
        }

        int saveId = Integer.parseInt(recordLocation.recordId);
        if (saveId >= record.id) {
            rightTitleBtn.setText("生成轨迹");
            rightTitleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveRecord(recordLocation.recordId);
                }
            });
        } else {
            rightBtnStartSport();
        }
    }

    private void rightBtnStartSport(){
        rightTitleBtn.setText("开启运动");
        rightTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSport(v);
            }
        });
    }

    protected void saveRecord(String recordId) {
        List<RecordLocation> locationList = LocationDBHelper.getLocationList(recordType, recordId);
        if (locationList != null && locationList.size() > 0) {
            RecordLocation firstLocation = locationList.get(0);
            RecordLocation lastLocation = locationList.get(locationList.size() - 1);
            double duration = getDuration(firstLocation, lastLocation)/1000;
            double distance = lastLocation.distance;
            String averageSpeed = getAverageSpeed(distance, duration);
            String pathLineStr = LocationComputeUtil.getPathLineStr(locationList);
            String startPoint = firstLocation.locationStr;
            String endPoint = lastLocation.locationStr;
            String dateStr = TimeDateUtil.getDateStrMinSecond(firstLocation.getTimestamp());
            Record record = Record.createRecord(recordType,
                    Double.toString(distance),
                    String.valueOf(duration),
                    averageSpeed, pathLineStr, startPoint, endPoint, dateStr);
            LocationDBHelper.insertRecord(record);
            mAllRecord.add(0, record);
            mAdapter.notifyDataSetChanged();
            rightBtnStartSport();
        } else {
            Toast.makeText(RecordActivity.this, "没有记录到路径", Toast.LENGTH_SHORT).show();
        }
    }

    private long getDuration(RecordLocation firstLocation, RecordLocation endLocation) {
        return (endLocation.endTime - firstLocation.timestamp);
    }

    private String getAverageSpeed(double distance, double duration) {
        return String.valueOf(distance/duration);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void searchAllRecordFromDB() {
        mAllRecord = LocationDBHelper.queryRecordAll(recordType);
    }

    public void onBackClick(View view) {
        this.finish();
    }

    public void startSport(View view) {
        startActivity(new Intent(this, LocationActivity.class));
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Record recordItem = (Record) parent.getAdapter().getItem(position);
        Intent intent = new Intent(RecordActivity.this,
                RecordShowActivity.class);
        intent.putExtra(RECORD_ID, recordItem.getId());
        intent.putExtra(LocationConstants.KEY_RECORD_TYPE, recordType);
        startActivity(intent);
    }
}
