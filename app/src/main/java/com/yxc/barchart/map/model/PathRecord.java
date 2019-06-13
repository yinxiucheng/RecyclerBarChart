package com.yxc.barchart.map.model;

import com.amap.api.location.AMapLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于记录一条轨迹，包括起点、终点、轨迹中间点、距离、耗时、平均速度、时间
 * 
 * @author ligen
 * 
 */
public class PathRecord {
	private AMapLocation mStartPoint;
	private AMapLocation mEndPoint;
	private List<AMapLocation> mPathLinePoints = new ArrayList<AMapLocation>();
	private int recordType;
	private String mDistance;
	private String mDuration;
	private String mAverageSpeed;
	private String mDate;
	private int mId = 0;

	public PathRecord() {

	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public AMapLocation getStartPoint() {
		return mStartPoint;
	}

	public void setStartPoint(AMapLocation startPoint) {
		this.mStartPoint = startPoint;
	}

	public AMapLocation getEndpoint() {
		return mEndPoint;
	}

	public void setEndpoint(AMapLocation endpoint) {
		this.mEndPoint = endpoint;
	}

	public List<AMapLocation> getPathLine() {
		return mPathLinePoints;
	}

	public void setPathLine(List<AMapLocation> pathLine) {
		this.mPathLinePoints = pathLine;
	}

	public String getDistance() {
		return mDistance;
	}

	public void setDistance(String distance) {
		this.mDistance = distance;
	}

	public String getDuration() {
		return mDuration;
	}

	public void setDuration(String duration) {
		this.mDuration = duration;
	}

	public String getAverageSpeed() {
		return mAverageSpeed;
	}

	public void setAverageSpeed(String averageSpeed) {
		this.mAverageSpeed = averageSpeed;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		this.mDate = date;
	}

	public void addPoint(AMapLocation point) {
		mPathLinePoints.add(point);
	}

	public void addPointList(List<AMapLocation> pointList) {
		mPathLinePoints.addAll(pointList);
	}

	@Override
	public String toString() {
		StringBuilder record = new StringBuilder();
		record.append("recordSize:" + getPathLine().size() + ", ");
		record.append("distance:" + getDistance() + "m, ");
		record.append("duration:" + getDuration() + "s");
		return record.toString();
	}
}
