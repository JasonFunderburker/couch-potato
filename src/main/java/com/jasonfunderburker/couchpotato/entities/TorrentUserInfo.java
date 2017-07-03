package com.jasonfunderburker.couchpotato.entities;

import com.jasonfunderburker.couchpotato.entities.converters.TorrentTypeConverter;
import com.jasonfunderburker.couchpotato.entities.util.CryptMaster;

import javax.persistence.*;

@Entity
@Table(name = "torrents_accounts")
public class TorrentUserInfo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type_id")
    @Convert(converter = TorrentTypeConverter.class)
    private TorrentType type;

    private String username;
    private String hash;

    @Transient
    private String clearHash;

    public TorrentUserInfo() {
        super();
    }

    public TorrentUserInfo(TorrentType type) {
        this.type = type;
    }

    public TorrentUserInfo(String userName, String password) {
        this.username = userName;
        setHash(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TorrentType getType() {
        return type;
    }

    public void setType(TorrentType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public String getPassword() {
        return CryptMaster.decrypt(hash);
    }

    public void setHash(String password) {
        this.hash = CryptMaster.encrypt(password);
    }

    public void setClearHash(String clearHash) {
        this.hash = clearHash;
    }

    @Override
    public String toString() {
        return "TorrentUserInfo{" +
                "type="+ type + ',' +
                "username='" + username + '\'' +
                '}';
    }
}