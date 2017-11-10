package com.jasonfunderburker.couchpotato.entities;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * Created by JasonFunderburker on 24.10.2016
 */
@Data
@Embeddable
public class ScheduleSettings {
    private String scheduleTime;
}
