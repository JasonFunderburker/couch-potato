package com.jasonfunderburker.couchpotato.domain;

import java.io.Serializable;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public class TorrentItem implements Serializable {
    private Long id;
    private String name;
    private String link;
    private TorrentStatus status;
    private TorrentState state;
    private TorrentType type;
    private String torrentType;
    private String errorText;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public TorrentStatus getStatus() {
        return status;
    }

    public void setStatus(TorrentStatus status) {
        this.status = status;
    }

    public TorrentState getState() {
        return state;
    }

    public void setState(TorrentState state) {
        this.state = state;
    }

    public TorrentType getType() {
        return type;
    }

    public void setType(TorrentType type) {
        this.type = type;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getTorrentType() {
        return torrentType;
    }

    public void setTorrentType(String torrentType) {
        this.torrentType = torrentType;
    }

    @Override
    public String toString() {
        return "TorrentItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", status=" + status +
                ", state=" + state +
                ", type=" + type +
                ", errorText='" + errorText + '\'' +
                '}';
    }


}
