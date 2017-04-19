package com.jasonfunderburker.couchpotato.repositories;

import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 19.04.2017
 *
 * @author JasonFunderburker
 */
public interface TorrentRepository extends JpaRepository<TorrentItem, Long> {

    List<TorrentItem> findByStatus(TorrentStatus status);
}
