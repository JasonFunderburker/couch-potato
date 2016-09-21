package com.jasonfunderburker.couchpotato.domain;

/**
 * Created by Ekaterina.Bashkankova on 21.09.2016
 */
public class TorrentUserInfo {
    String userName;
    String hash;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "TorrentUserInfo{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
