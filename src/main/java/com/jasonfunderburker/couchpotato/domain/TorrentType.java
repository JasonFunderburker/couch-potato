package com.jasonfunderburker.couchpotato.domain;

import com.jasonfunderburker.couchpotato.dao.mybatis.handler.HasIdValue;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public enum TorrentType implements HasIdValue {
    LOST_FILM(1, "lostfilm");

    private int id;
    private String name;

    private TorrentType(int id, String name) {
        this.id = id;
        this.name = name;

    }
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TorrentType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
