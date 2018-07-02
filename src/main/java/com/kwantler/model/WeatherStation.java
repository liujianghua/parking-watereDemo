package com.kwantler.model;

import java.io.Serializable;
import java.util.Date;

public class WeatherStation implements Serializable {
    private Integer id;

    private String bygId;

    private String deviceId;

    private Date monitorTime;

    private Float currentTemp;

    private Float topTemp;

    private Date topTempTime;

    private Float lowestTemp;

    private Date lowestTempTime;

    private Float currentHumidity;

    private Float topHumidity;

    private Date topHumidityTime;

    private Float lowestHumidity;

    private Date lowestHumidityTime;

    private Float currentAirPressure;

    private Float instantaneousWindSpeed;

    private Integer instantaneousWindDirection;

    private Float currentRainfallMin;

    private Float currentRainfallHour;

    private Float batteryLevel;

    private Integer status;

    private Integer sendTo;

    private Date insertTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBygId() {
        return bygId;
    }

    public void setBygId(String bygId) {
        this.bygId = bygId == null ? null : bygId.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Date getMonitorTime() {
        return monitorTime;
    }

    public void setMonitorTime(Date monitorTime) {
        this.monitorTime = monitorTime;
    }

    public Float getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(Float currentTemp) {
        this.currentTemp = currentTemp;
    }

    public Float getTopTemp() {
        return topTemp;
    }

    public void setTopTemp(Float topTemp) {
        this.topTemp = topTemp;
    }

    public Date getTopTempTime() {
        return topTempTime;
    }

    public void setTopTempTime(Date topTempTime) {
        this.topTempTime = topTempTime;
    }

    public Float getLowestTemp() {
        return lowestTemp;
    }

    public void setLowestTemp(Float lowestTemp) {
        this.lowestTemp = lowestTemp;
    }

    public Date getLowestTempTime() {
        return lowestTempTime;
    }

    public void setLowestTempTime(Date lowestTempTime) {
        this.lowestTempTime = lowestTempTime;
    }

    public Float getCurrentHumidity() {
        return currentHumidity;
    }

    public void setCurrentHumidity(Float currentHumidity) {
        this.currentHumidity = currentHumidity;
    }

    public Float getTopHumidity() {
        return topHumidity;
    }

    public void setTopHumidity(Float topHumidity) {
        this.topHumidity = topHumidity;
    }

    public Date getTopHumidityTime() {
        return topHumidityTime;
    }

    public void setTopHumidityTime(Date topHumidityTime) {
        this.topHumidityTime = topHumidityTime;
    }

    public Float getLowestHumidity() {
        return lowestHumidity;
    }

    public void setLowestHumidity(Float lowestHumidity) {
        this.lowestHumidity = lowestHumidity;
    }

    public Date getLowestHumidityTime() {
        return lowestHumidityTime;
    }

    public void setLowestHumidityTime(Date lowestHumidityTime) {
        this.lowestHumidityTime = lowestHumidityTime;
    }

    public Float getCurrentAirPressure() {
        return currentAirPressure;
    }

    public void setCurrentAirPressure(Float currentAirPressure) {
        this.currentAirPressure = currentAirPressure;
    }

    public Float getInstantaneousWindSpeed() {
        return instantaneousWindSpeed;
    }

    public void setInstantaneousWindSpeed(Float instantaneousWindSpeed) {
        this.instantaneousWindSpeed = instantaneousWindSpeed;
    }

    public Integer getInstantaneousWindDirection() {
        return instantaneousWindDirection;
    }

    public void setInstantaneousWindDirection(Integer instantaneousWindDirection) {
        this.instantaneousWindDirection = instantaneousWindDirection;
    }

    public Float getCurrentRainfallMin() {
        return currentRainfallMin;
    }

    public void setCurrentRainfallMin(Float currentRainfallMin) {
        this.currentRainfallMin = currentRainfallMin;
    }

    public Float getCurrentRainfallHour() {
        return currentRainfallHour;
    }

    public void setCurrentRainfallHour(Float currentRainfallHour) {
        this.currentRainfallHour = currentRainfallHour;
    }

    public Float getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSendTo() {
        return sendTo;
    }

    public void setSendTo(Integer sendTo) {
        this.sendTo = sendTo;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}