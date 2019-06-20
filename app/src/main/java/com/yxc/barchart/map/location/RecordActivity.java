package com.yxc.barchart.map.location;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.util.LocationComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordCorrect;
import com.yxc.barchart.map.model.RecordLocation;
import com.yxc.commonlib.util.TimeDateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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


    private List<RecordCorrect> mListPoint = new ArrayList<>();
    private Boolean isFirst = true;// 是否是第一次定位点
    private RecordCorrect weight1 = new RecordCorrect();// 权重点1
    private RecordCorrect weight2;// 权重点2
    private List<RecordCorrect> w1TempList = new ArrayList<>();// w1的临时定位点集合
    private List<RecordCorrect> w2TempList = new ArrayList<>();// w2的临时定位点集合
    private int w1Count = 0; // 统计w1Count所统计过的点数
    private String filterString;
    private int posCount = 0;

    private Boolean filterPos(RecordLocation recordLocation) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(recordLocation.getTimestamp());
        String time = df.format(date);//定位时间
        filterString = time + "开始虑点" + "\r\n";
        try {
            // 获取的第一个定位点不进行过滤
            if (isFirst) {
                isFirst = false;
                weight1 = RecordLocation.createRecordCorrect(recordLocation);
                /****************存储数据到文件中，后面好分析******************/

                filterString += "第一次定位" + "\r\n";
                /**********************************/
                // 将得到的第一个点存储入w1的缓存集合
                final RecordCorrect traceLocation = RecordLocation.createRecordCorrect(recordLocation);
                w1TempList.add(traceLocation);
                w1Count++;
                return true;

            } else {
                filterString += "非第一次定位" + "\r\n";
//                // todo 过来 locationType == 6的，以及Accuracy > 500 的点， 过滤静止时的偏点，在静止时速度小于1米就算做静止状态
//                if (recordLocation.getSpeed() < 1) {
//                    return false;
//                }
                if (weight2 == null) {
                    filterString += "weight2 == null" + "\r\n";

                    // 计算w1与当前定位点p1的时间差并得到最大偏移距离D
                    long offsetTimeMils = recordLocation.getTimestamp() - weight1.getTimestamp();
                    long offsetTimes = (offsetTimeMils / 1000);
                    long MaxDistance = offsetTimes * LocationComputeUtil.getMaxSpeedByType(recordType);
                    float distance = AMapUtils.calculateLineDistance(
                            new LatLng(weight1.getLatitude(), weight1.getLongitude()),
                            new LatLng(recordLocation.getLatitude(), recordLocation.getLongitude()));

                    filterString += "distance = " + distance + ",MaxDistance = " + MaxDistance + "\r\n";

                    if (distance > MaxDistance) {
                        filterString += "distance > MaxDistance" + "\r\n";
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 = RecordLocation.createRecordCorrect(recordLocation);
                        w2TempList.add(weight2);
                        return false;

                    } else {
                        filterString += "distance < MaxDistance" + "\r\n";
                        // 将p1加入到做坐标集合w1TempList
                        RecordCorrect traceLocation = RecordLocation.createRecordCorrect(recordLocation);
                        w1TempList.add(traceLocation);
                        w1Count++;
                        // 更新w1权值点, todo 需要修改 locationStr
                        weight1.setLatitude(weight1.getLatitude() * 0.2 + recordLocation.getLatitude() * 0.8);
                        weight1.setLongitude(weight1.getLongitude() * 0.2 + recordLocation.getLongitude() * 0.8);
                        weight1.setTimestamp(recordLocation.getTimestamp());
                        weight1.setSpeed(recordLocation.getSpeed());

                        if (w1TempList.size() > 3) {
                            filterString += "d1TempList.size() > 3" + "\r\n";
                            //将w1TempList中的数据放入finalList，并将w1TempList清空
                            mListPoint.addAll(w1TempList);
                            w1TempList.clear();
                            return true;
                        } else {
                            filterString += "d1TempList.size() < 3" + "\r\n";
                            return false;
                        }
                    }
                } else {
                    filterString += "weight2 != null" + "\r\n";
                    // 计算w2与当前定位点p1的时间差并得到最大偏移距离D
                    long offsetTimeMils = recordLocation.getTimestamp() - weight2.getTimestamp();
                    long offsetTimes = (offsetTimeMils / 1000);
                    long MaxDistance = offsetTimes * 16;
                    float distance = AMapUtils.calculateLineDistance(
                            new LatLng(weight2.getLatitude(), weight2.getLongitude()),
                            new LatLng(recordLocation.getLatitude(), recordLocation.getLongitude()));


                    filterString += "distance = " + distance + ",MaxDistance = " + MaxDistance + "\r\n";

                    if (distance > MaxDistance) {
                        filterString += "distance > MaxDistance" + "\r\n";
                        w2TempList.clear();
                        // 将设置w2位新的点，并存储入w2临时缓存
                        weight2 = RecordLocation.createRecordCorrect(recordLocation);
                        w2TempList.add(weight2);

                        return false;
                    } else {
                        filterString += "distance < MaxDistance" + "\r\n";

                        // 将p1加入到做坐标集合w2TempList
                        RecordCorrect traceLocation = RecordLocation.createRecordCorrect(recordLocation);
                        w2TempList.add(traceLocation);

                        // 更新w2权值点 todo 这里需要修改 locationStr
                        weight2.setLatitude(weight2.getLatitude() * 0.2 + recordLocation.getLatitude() * 0.8);
                        weight2.setLongitude(weight2.getLongitude() * 0.2 + recordLocation.getLongitude() * 0.8);
                        weight2.setTimestamp(recordLocation.getTimestamp());
                        weight2.setSpeed(recordLocation.getSpeed());

                        if (w2TempList.size() > 4) {
                            filterString += "w2TempList.size() > 4" + "\r\n";
                            // 判断w1所代表的定位点数是否>4,小于说明w1之前的点为从一开始就有偏移的点
                            if (w1Count > 4) {
                                filterString += "w1Count > 4" + "\r\n";
                                mListPoint.addAll(w1TempList);
                            } else {
                                filterString += "w1Count < 4" + "\r\n";
                                w1TempList.clear();
                            }

                            //将w2TempList集合中数据放入finalList中
                            mListPoint.addAll(w2TempList);
                            //1、清空w2TempList集合 2、更新w1的权值点为w2的值 3、将w2置为null
                            w2TempList.clear();
                            weight1 = weight2;
                            weight2 = null;
                            return true;
                        } else {
                            filterString += "w2TempList.size() < 4" + "\r\n";
                            return false;
                        }
                    }
                }
            }
        } finally {
            //FileWriteUtil.getInstance().save("tutu_driver_filter.txt", filterString);
            //Log.d("hhh","finnaly");
            Log.d("RecordActivity", filterString);
        }
    }
}
