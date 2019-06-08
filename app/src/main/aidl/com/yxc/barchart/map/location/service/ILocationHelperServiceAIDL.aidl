// ILocationHelperServiceAIDL.aidl
package com.yxc.barchart.map.location.service;

// Declare any non-default types here with import statements

interface ILocationHelperServiceAIDL {

    /**
    * 定位service绑定完毕后，会通知helperservice自己绑定的notiId
    * @param notiId 定位service的notiId
    */
    void onFinishBind(int notiId);
}
