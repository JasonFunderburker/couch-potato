package com.jasonfunderburker.couchpotato.domain;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
public class TorrentState {
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
                '}';
    }
}
