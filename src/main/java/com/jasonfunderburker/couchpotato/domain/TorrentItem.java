package com.jasonfunderburker.couchpotato.domain;

import java.net.URL;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public class TorrentItem {
    private Long id;
    private String name;
    private URL link;
    private TorrentStatus status;
    private String state;
    private TorrentType type;
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

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public TorrentStatus getStatus() {
        return status;
    }

    public void setStatus(TorrentStatus status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
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
}
