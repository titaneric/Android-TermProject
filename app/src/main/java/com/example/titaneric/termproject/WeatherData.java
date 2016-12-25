package com.example.titaneric.termproject;

/**
 * Created by TitanEric on 12/25/2016.
 */

public class WeatherData {

    private String location = "";
    private String timeRange = "";
    private String weather = "";
    private String maxT = "";
    private String minT = "";
    private String comfortIndex = "";
    private String dropPercent = "";

    public WeatherData(){
        this.location = "";
        this.weather = "";
        this.timeRange = "";
        this.maxT = "";
        this.minT = "";
        this.comfortIndex = "";
        this.dropPercent = "";
    }

    public WeatherData(String loc, String weather, String time, String maxT, String minT, String comfort, String drop){
        this.location = loc;
        this.weather = weather;
        this.timeRange = time;
        this.maxT = maxT;
        this.minT = minT;
        this.comfortIndex = comfort;
        this.dropPercent = drop;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMaxT() {
        return maxT;
    }

    public void setMaxT(String maxT) {
        this.maxT = maxT;
    }

    public String getMinT() {
        return minT;
    }

    public void setMinT(String minT) {
        this.minT = minT;
    }

    public String getComfortIndex() {
        return comfortIndex;
    }

    public void setComfortIndex(String comfortIndex) {
        this.comfortIndex = comfortIndex;
    }

    public String getDropPercent() {
        return dropPercent;
    }

    public void setDropPercent(String dropPercent) {
        this.dropPercent = dropPercent;
    }



}
