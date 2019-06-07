package com.yxc.barchart.map.basic;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.yxc.barchart.R;

public class TwoMapActivity extends Activity {

    MapView mapView;
    MapView textureMapView;

    AMap aMap1;
    AMap aMap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_map);

        mapView = (MapView) findViewById(R.id.mapview);
        textureMapView = (MapView) findViewById(R.id.texturemapview);


        mapView.onCreate(savedInstanceState);
        textureMapView.onCreate(savedInstanceState);

        aMap1 = mapView.getMap();

        aMap2 = textureMapView.getMap();

        init();

    }

    private void init() {
        aMap1.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                setUp(aMap1);
            }
        });

        aMap2.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                setUp(aMap2);
            }
        });
    }

    private void setUp(AMap amap) {
        UiSettings uiSettings = amap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        textureMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        textureMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        textureMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        textureMapView.onDestroy();
    }
}
