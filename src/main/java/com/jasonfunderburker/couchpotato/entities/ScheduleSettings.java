package com.jasonfunderburker.couchpotato.entities;

/**
 * Created by JasonFunderburker on 24.10.2016
 */
public class ScheduleSettings {
    private String scheduleTime;

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Override
    public String toString() {
        return "ScheduleSettings{" +
                "scheduleTime='" + scheduleTime + '\'' +
                '}';
    }
}
