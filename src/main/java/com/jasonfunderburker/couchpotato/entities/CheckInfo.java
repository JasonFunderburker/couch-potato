package com.jasonfunderburker.couchpotato.entities;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created on 20.12.2017
 *
 * @author JasonFunderburker
 */
@Data
public class CheckInfo {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
