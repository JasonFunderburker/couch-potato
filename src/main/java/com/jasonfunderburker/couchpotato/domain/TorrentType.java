package com.jasonfunderburker.couchpotato.domain;

import com.jasonfunderburker.couchpotato.dao.mybatis.handler.HasIdValue;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public enum TorrentType implements HasIdValue {
    LOST_FILM(1L, "lostfilm"),
    NNM_CLUB(2L, "nnmclub"),
    RUTRACKER(3L, "rutracker");

    private Long id;
    private String name;

    private TorrentType(Long id, String name) {
        this.id = id;
        this.name = name;

    }
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
