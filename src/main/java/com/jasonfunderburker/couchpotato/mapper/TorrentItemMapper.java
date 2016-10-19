package com.jasonfunderburker.couchpotato.mapper;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;

import java.util.List;

/**
 * Created by JasonFunderburker on 19.08.2016
 */
public interface TorrentItemMapper {

    TorrentItem findById(Long id);

    List<TorrentItem> findByStatus(TorrentStatus status);

    List<TorrentItem> getItemsList();

    void updateItem(TorrentItem item);

    void addItemToList(TorrentItem item);

    void deleteItemFromList(Long id);
}
