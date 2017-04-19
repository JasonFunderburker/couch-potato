package com.jasonfunderburker.couchpotato.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
@Embeddable
public class TorrentState {
    @Column(name = "state")
    private String state;
    private String info;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public TorrentState() {
        super();
    }

    public TorrentState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TorrentState)) return false;

        TorrentState that = (TorrentState) o;

        if (!state.equals(that.state)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return "TorrentState{" +
                "state='" + state + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
