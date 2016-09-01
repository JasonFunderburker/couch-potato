package com.jasonfunderburker.couchpotato.domain;

/**
 * Created by Ekaterina.Bashkankova on 01.09.2016
 */
public class TorrentState {
    String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public TorrentState(String state) {
        this.state = state;
    }
}
