package com.kwantler.model;

import java.io.Serializable;
import java.util.Date;

public class WaterDetect implements Serializable {
    private Integer id;

    private String bygId;

    private String deviceId;

    private Date monitorTime;

    private Float freeChlorine;

    private Float ph;

    private Float temperature;

    private Float turbidity;

    private Integer waterQuality;

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

    public Float getFreeChlorine() {
        return freeChlorine;
    }

    public void setFreeChlorine(Float freeChlorine) {
        this.freeChlorine = freeChlorine;
    }

    public Float getPh() {
        return ph;
    }

    public void setPh(Float ph) {
        this.ph = ph;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getTurbidity() {
        return turbidity;
    }

    public void setTurbidity(Float turbidity) {
        this.turbidity = turbidity;
    }

    public Integer getWaterQuality() {
        return waterQuality;
    }

    public void setWaterQuality(Integer waterQuality) {
        this.waterQuality = waterQuality;
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