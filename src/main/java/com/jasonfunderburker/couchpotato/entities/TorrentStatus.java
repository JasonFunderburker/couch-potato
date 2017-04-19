package com.jasonfunderburker.couchpotato.entities;

import com.jasonfunderburker.couchpotato.entities.converters.HasIdValue;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public enum TorrentStatus implements HasIdValue {
    NEW(1L),REFRESHED(2L),UNCHANGED(3L),DOWNLOADED(4L),ERROR(5L),RELOADED(6L),DOWNLOAD_ERROR(7L);

    private Long id;

    private TorrentStatus(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
