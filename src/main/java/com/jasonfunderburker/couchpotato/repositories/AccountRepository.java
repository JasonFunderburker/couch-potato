package com.jasonfunderburker.couchpotato.repositories;

import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 19.04.2017
 *
 * @author JasonFunderburker
 */
public interface AccountRepository extends JpaRepository<TorrentUserInfo, Long> {

    TorrentUserInfo findByType(TorrentType type);

    default void saveIfNotExists(List<TorrentUserInfo> accounts) {
        accounts.forEach(this::saveIfNotExists);
    }

    default void saveIfNotExists(TorrentUserInfo userInfo) {
        if (!existsByType(userInfo.getType())) {
            save(userInfo);
        }
    }

    boolean existsByType(TorrentType type);
}
