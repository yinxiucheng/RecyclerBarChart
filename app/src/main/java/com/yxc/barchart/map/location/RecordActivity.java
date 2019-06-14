package com.yxc.barchart.map.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.model.PathRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有轨迹list展示activity
 *
 */
public class RecordActivity extends Activity implements OnItemClickListener {

	private RecordAdapter mAdapter;
	private ListView mAllRecordListView;

	private List<PathRecord> mAllRecord = new ArrayList<PathRecord>();

	public static final String RECORD_ID = "record_id";

	public int recordType = LocationConstants.SPORT_TYPE_RUNNING;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordlist);

		mAllRecordListView = findViewById(R.id.recordlist);
		recordType = getIntent().getIntExtra(LocationConstants.KEY_RECORD_TYPE, -1);
		searchAllRecordFromDB();
		mAdapter = new RecordAdapter(this, mAllRecord);
		mAllRecordListView.setAdapter(mAdapter);
		mAllRecordListView.setOnItemClickListener(this);
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

	public void startSport(View view){
		startActivity(new Intent(this, LocationActivity.class));
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PathRecord recordItem = (PathRecord) parent.getAdapter().getItem(
				position);
		Intent intent = new Intent(RecordActivity.this,
				RecordShowActivity.class);
		intent.putExtra(RECORD_ID, recordItem.getId());
		intent.putExtra(LocationConstants.KEY_RECORD_TYPE, recordType );
		startActivity(intent);
	}
}
