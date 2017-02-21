package com.jasonfunderburker.couchpotato.service.torrents;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;

import java.util.List;

/**
 * Created by JasonFunderburker on 01.09.2016
 */

public interface TorrentsItemService {

    List<TorrentItem> getItemsList();

    List<TorrentItem> findByStatus(TorrentStatus status);

    void checkItem(TorrentItem item);

    void checkAllItems();

    void addItemToList(TorrentItem item) throws IllegalArgumentException;

    void deleteItemFromList(long id);
}
