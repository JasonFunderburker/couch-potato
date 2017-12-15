package com.jasonfunderburker.couchpotato.entities;

import com.jasonfunderburker.couchpotato.entities.converters.TorrentStatusConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
@Entity
@Table(name = "torrents_list")
@Data
@NoArgsConstructor
public class TorrentItem implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 4000)
    private String name;
    @Column(length = 4000)
    private String link;

    @Column(name = "status_id")
    @Convert(converter = TorrentStatusConverter.class)
    private TorrentStatus status;

    @Embedded
    private TorrentState state;

    @Column(name = "error_text", length = 4000)
    private String errorText;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private TorrentUserInfo userInfo;

    @Column(name = "file_name", length = 4000)
    private String fileName;

    private LocalDateTime updateDate;

    public TorrentItem(TorrentItem item) {
        this.name = item.name;
        this.link = item.link;
        this.status = item.status;
        this.state = item.state;
        this.errorText = item.errorText;
        this.userInfo = item.userInfo;
        this.fileName = item.fileName;
        this.updateDate = item.updateDate;
    }
}
