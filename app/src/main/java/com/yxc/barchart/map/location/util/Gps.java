package com.yxc.barchart.map.location.util;

/**
 * @author yxc
 * @since  2019-08-30
 */
public class Gps {

    private double latitude;
    private double longitude;
    private double altitude;

    public Gps(double latitude, double longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public Gps(double latitude, double longitude, double altitude) {
        this(latitude, longitude);
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }


    @Override
    public String toString() {
        return latitude + "," + longitude + "," + altitude;
    }
}

