package com.yxc.barchart.map.location.event;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.yxc.barchart.map.model.RecordLocation;

/**
 * @author yxc
 * @date 2019-06-14
 */
public class LocationEvent {

   public  AMapLocation mapLocation;

   public final RecordLocation recordLocation;

    public  Location location;

   public LocationEvent(AMapLocation mapLocation, RecordLocation recordLocation){
        this.mapLocation = mapLocation;
        this.recordLocation = recordLocation;
   }

    public LocationEvent(Location mapLocation, RecordLocation recordLocation){
        this.location = mapLocation;
        this.recordLocation = recordLocation;
    }
}
