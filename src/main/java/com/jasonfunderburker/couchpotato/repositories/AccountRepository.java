package com.jasonfunderburker.couchpotato.repositories;

import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 19.04.2017
 *
 * @author JasonFunderburker
 */
public interface AccountRepository extends JpaRepository<TorrentUserInfo, Long> {

    TorrentUserInfo findByType(TorrentType type);
}
