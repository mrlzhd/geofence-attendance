package com.example.geofencing.ViewHolder;

public class DataGetFire {

    String name, latitude, longitude, className, date, time;

    public DataGetFire(String name, String latitude, String longitude, String className, String date, String time) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.className = className;
        this.date = date;
        this.time = time;
    }

    public DataGetFire() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
