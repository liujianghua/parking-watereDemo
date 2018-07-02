package com.kwantler.model;

import java.io.Serializable;
import java.util.Date;

public class ParkingWaterDetect implements Serializable {
    private Integer id;

    private String bygId;

    private String deviceId;

    private Date monitorTime;

    private Integer currentWaterLevel;

    private Integer outWaterLevel;

    private Integer alertOutWater;

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

    public Integer getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(Integer currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
    }

    public Integer getOutWaterLevel() {
        return outWaterLevel;
    }

    public void setOutWaterLevel(Integer outWaterLevel) {
        this.outWaterLevel = outWaterLevel;
    }

    public Integer getAlertOutWater() {
        return alertOutWater;
    }

    public void setAlertOutWater(Integer alertOutWater) {
        this.alertOutWater = alertOutWater;
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