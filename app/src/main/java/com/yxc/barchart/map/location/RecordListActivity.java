package com.yxc.barchart.map.location;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.database.RealmDbHelper;
import com.yxc.barchart.map.location.recycler.RecordAdapter;
import com.yxc.barchart.map.location.recycler.RecordItemDecoration;
import com.yxc.barchart.map.location.util.LocationComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordCorrect;
import com.yxc.barchart.map.model.RecordLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 所有轨迹list展示activity
 */
public class RecordListActivity extends AppCompatActivity implements RecordAdapter.OnRecordItemClickListener {

    private RecordAdapter mAdapter;
    private RecyclerView mRecordRcyclerView;

    private List<Record> mAllRecord = new ArrayList<>();

    public static final String RECORD_ID = "record_id";

    public int recordType = LocationConstants.SPORT_TYPE_RUNNING;

    private TextView rightTitleBtn;
    private TextView titleCenter;
    private boolean useGaoDe = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordlist);

        Intent intent = getIntent();
        if (intent != null) {
            useGaoDe = intent.getBooleanExtra("useGaoDe", true);
            recordType = intent.getIntExtra(LocationConstants.KEY_RECORD_TYPE, -1);
        }
        mRecordRcyclerView = findViewById(R.id.record_recycler);
        titleCenter = findViewById(R.id.title_center);
        if (useGaoDe) {
            titleCenter.setText("高德运动记录");
        } else {
            titleCenter.setText("Mapbox运动记录");
        }
        rightTitleBtn = findViewById(R.id.title_tv_option);
        searchAllRecordFromDB();
        invokeRightBtn();
        initRecycler();
    }

    private void initRecycler() {
        mAdapter = new RecordAdapter(this, mAllRecord);
        mAdapter.setUseGaoDe(useGaoDe);
        mAdapter.setOnRecordItemClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecordRcyclerView.setLayoutManager(linearLayoutManager);

        RecordItemDecoration itemDecoration = new RecordItemDecoration();
        mRecordRcyclerView.addItemDecoration(itemDecoration);

        mRecordRcyclerView.setAdapter(mAdapter);
    }

    private void invokeRightBtn() {
        Record record = LocationDBHelper.getLastRecord(recordType);
        final RecordLocation recordLocation = LocationDBHelper.getLastItem(recordType);
        if (record == null || recordLocation == null) {
            rightBtnStartSport();
            return;
        }

        int saveId = Integer.parseInt(recordLocation.recordId);
        if (saveId >= record.id) {
            rightTitleBtn.setText("生成轨迹");
            rightTitleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<RecordLocation> locationList = LocationDBHelper.getLocationList(recordType, recordLocation.recordId);
                    LocationDBHelper.saveRecord(RecordListActivity.this, locationList);
                }
            });
        } else {
            rightBtnStartSport();
        }
    }

    private void rightBtnStartSport() {
        rightTitleBtn.setText("开启运动");
        rightTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSport(v);
            }
        });
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
                w1TempList.clear();
                w2TempList.clear();
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
                        AMapLocation aMapLocation = LocationComputeUtil.parseLocation(weight1.locationStr);
                        aMapLocation.setLatitude(weight1.getLatitude());
                        aMapLocation.setLongitude(weight1.getLongitude());
                        weight1.setLocationStr(LocationComputeUtil.amapLocationToString(aMapLocation));

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
                    long MaxDistance = offsetTimes * LocationComputeUtil.getMaxSpeedByType(recordType);
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
                        AMapLocation aMapLocation = LocationComputeUtil.parseLocation(weight2.locationStr);
                        aMapLocation.setLatitude(weight2.getLatitude());
                        aMapLocation.setLongitude(weight2.getLongitude());
                        weight2.setLocationStr(LocationComputeUtil.amapLocationToString(aMapLocation));
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

    @Override
    public void onItemClick(int position, Record recordItem) {
        Class cls = null;
        if (useGaoDe) {
            cls = RecordShowActivity.class;
            if (recordItem.isCorrect) {
                cls = RecordCorrectShowActivity.class;
            }
        } else {
            cls = MapboxShowActivity.class;
        }

        Intent intent = new Intent(this, cls);
        intent.putExtra(RECORD_ID, recordItem.getId());
        intent.putExtra(LocationConstants.KEY_RECORD_TYPE, recordType);
        startActivity(intent);
    }

    @Override
    public void onRecordCorrect(int position, final Record record) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != mListPoint) {
                    mListPoint.clear();
                }
                int recordType = record.recordType;
                int recordId = record.getId();
                List<RecordLocation> originalList = LocationDBHelper.queryRecordLocationAll(recordType, String.valueOf(recordId));
                for (int i = 0; i < originalList.size(); i++) {
                    RecordLocation recordLocation = originalList.get(i);
                    if (i == 0) {
                        isFirst = true;
                    } else {
                        isFirst = false;
                    }
                    filterPos(recordLocation);
                }
                if (null != mListPoint && mListPoint.size() > 0) {
                    Collections.sort(mListPoint);
                    RealmDbHelper.insertRealmObjects(mListPoint);
                    LocationDBHelper.saveRecordCorrect(RecordListActivity.this, mListPoint);
                }
                Toast.makeText(RecordListActivity.this, "转化完毕", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
