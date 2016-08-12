package com.jasonfunderburker.couchpotato.domain;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public enum TorrentStatus {
    NEW(1),REFRESHED(2),DOWNLOADED(3),ERROR(4);

    private int id;

    private TorrentStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
