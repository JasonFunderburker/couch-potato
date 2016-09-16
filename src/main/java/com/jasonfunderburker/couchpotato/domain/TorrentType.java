package com.jasonfunderburker.couchpotato.domain;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public class TorrentType {
    private Long id;
    private String name;

    public TorrentType() {
        super();
    }

    public TorrentType(String name) {
        this.name = name;
    }

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
