package com.jasonfunderburker.couchpotato.domain;

import com.jasonfunderburker.couchpotato.dao.mybatis.handler.HasIdValue;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public enum TorrentStatus implements HasIdValue {
    NEW(1),REFRESHED(2),UNCHANGED(3),DOWNLOADED(4),ERROR(5);

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
