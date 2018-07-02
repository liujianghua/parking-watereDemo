package com.kwantler.model;

import java.io.Serializable;
import java.util.Date;

public class SewerCover implements Serializable {
    private Integer id;

    private String bygId;

    private String deviceId;

    private Date monitorTime;

    private Integer alertAngleSlant;

    private Integer angleCurrent;

    private Integer deviceBatteryLevel;

    private Integer waterLevel;

    private Integer angleAlert;

    private Integer alertLowbattery;

    private Integer alertOutWaterline;

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

    public Integer getAlertAngleSlant() {
        return alertAngleSlant;
    }

    public void setAlertAngleSlant(Integer alertAngleSlant) {
        this.alertAngleSlant = alertAngleSlant;
    }

    public Integer getAngleCurrent() {
        return angleCurrent;
    }

    public void setAngleCurrent(Integer angleCurrent) {
        this.angleCurrent = angleCurrent;
    }

    public Integer getDeviceBatteryLevel() {
        return deviceBatteryLevel;
    }

    public void setDeviceBatteryLevel(Integer deviceBatteryLevel) {
        this.deviceBatteryLevel = deviceBatteryLevel;
    }

    public Integer getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(Integer waterLevel) {
        this.waterLevel = waterLevel;
    }

    public Integer getAngleAlert() {
        return angleAlert;
    }

    public void setAngleAlert(Integer angleAlert) {
        this.angleAlert = angleAlert;
    }

    public Integer getAlertLowbattery() {
        return alertLowbattery;
    }

    public void setAlertLowbattery(Integer alertLowbattery) {
        this.alertLowbattery = alertLowbattery;
    }

    public Integer getAlertOutWaterline() {
        return alertOutWaterline;
    }

    public void setAlertOutWaterline(Integer alertOutWaterline) {
        this.alertOutWaterline = alertOutWaterline;
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