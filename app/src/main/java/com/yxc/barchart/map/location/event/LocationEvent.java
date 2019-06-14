package com.yxc.barchart.map.location.event;

import com.amap.api.location.AMapLocation;

/**
 * @author yxc
 * @date 2019-06-14
 */
public class LocationEvent {

   public final AMapLocation mapLocation;

   public LocationEvent(AMapLocation mapLocation){
        this.mapLocation = mapLocation;
   }
}
