package com.jasonfunderburker.couchpotato.entities;

import com.jasonfunderburker.couchpotato.entities.converters.TorrentStatusConverter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
@Entity
@Table(name = "torrents_list")
public class TorrentItem implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String link;

    @Column(name = "status_id")
    @Convert(converter = TorrentStatusConverter.class)
    private TorrentStatus status;

    @Embedded
    private TorrentState state;
/*
    @Column(name = "type_id")
    @Convert(converter = TorrentTypeConverter.class)
    private TorrentType type;
*/
    @Column(name = "error_text")
    private String errorText;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private TorrentUserInfo userInfo;

    @Column(name = "file_name")
    private String fileName;

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
/*
    public TorrentType getType() {
        return type;
    }

    public void setType(TorrentType type) {
        this.type = type;
    }
*/
    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public TorrentUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(TorrentUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "TorrentItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", status=" + status +
                ", state=" + state +
//                ", type=" + type +
                ", errorText='" + errorText + '\'' +
                ", userInfo=" + userInfo +
                ", fileName='" + fileName + '\'' +
                '}';
    }


}
