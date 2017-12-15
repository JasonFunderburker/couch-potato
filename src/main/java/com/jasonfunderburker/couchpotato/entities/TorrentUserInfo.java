package com.jasonfunderburker.couchpotato.entities;

import com.jasonfunderburker.couchpotato.entities.converters.TorrentTypeConverter;
import com.jasonfunderburker.couchpotato.entities.util.CryptMaster;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;

@Entity
@Table(name = "torrents_accounts")
@Data
@NoArgsConstructor
@ToString(exclude="hash")
public class TorrentUserInfo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type_id")
    @Convert(converter = TorrentTypeConverter.class)
    private TorrentType type;

    private String username;
    private String hash;

    public TorrentUserInfo(TorrentType type) {
        this.type = type;
    }

    public TorrentUserInfo(String userName, String password) {
        this.username = userName;
        generateHash(password);
    }

    @Transient
    public String getPassword() {
        return hash != null ? CryptMaster.decrypt(hash) : null;
    }

    public void generateHash(String password) {
        this.hash = CryptMaster.encrypt(password);
    }
}