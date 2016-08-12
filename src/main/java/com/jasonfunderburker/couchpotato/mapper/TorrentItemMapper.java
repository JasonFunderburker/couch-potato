package com.jasonfunderburker.couchpotato.mapper;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;

import java.util.List;

/**
 * Created by Ekaterina.Bashkankova on 19.08.2016
 */
public interface TorrentItemMapper {

    TorrentItem findById(Long id);

    List<TorrentItem> getItemsList();
}
