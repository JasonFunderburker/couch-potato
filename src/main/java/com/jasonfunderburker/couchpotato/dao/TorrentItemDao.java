package com.jasonfunderburker.couchpotato.dao;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;

import java.util.List;

/**
 * Created by Ekaterina.Bashkankova on 19.08.2016
 */
public interface TorrentItemDao {

    TorrentItem findById(Long id);

    List<TorrentItem> getItemsList();
}
