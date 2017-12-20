package com.jasonfunderburker.couchpotato.service.torrents;

import com.jasonfunderburker.couchpotato.entities.CheckInfo;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentStatus;

import java.util.List;

/**
 * Created by JasonFunderburker on 01.09.2016
 */

public interface TorrentsItemService {

    List<TorrentItem> getItemsList();

    List<TorrentItem> findByStatus(TorrentStatus status);

    void checkItem(TorrentItem item);

    TorrentItem checkItem(long id);

    void checkAllItems();

    void addItemToList(TorrentItem item);

    void deleteItemFromList(long id);

    CheckInfo getLastCheckInfo();
}
