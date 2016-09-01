package com.jasonfunderburker.couchpotato.service.torrents;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import java.util.List;

/**
 * Created by Ekaterina.Bashkankova on 01.09.2016
 */

public interface TorrentsItemService {

    List<TorrentItem> getItemsList();

    void checkItem(TorrentItem item);

    void checkAllItems();
}
