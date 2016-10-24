package com.jasonfunderburker.couchpotato.domain;

/**
 * Created by JasonFunderburker on 24.10.2016
 */
public class ScheduleSettings {
    String scheduleTime;

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
