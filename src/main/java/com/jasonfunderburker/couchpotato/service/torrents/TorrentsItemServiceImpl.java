package com.jasonfunderburker.couchpotato.service.torrents;

import com.jasonfunderburker.couchpotato.dao.TorrentItemDao;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.service.check.TorrentCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ekaterina.Bashkankova on 01.09.2016
 */
@Service
public class TorrentsItemServiceImpl implements TorrentsItemService {
    @Autowired
    TorrentItemDao torrentItemDao;
    @Autowired
    TorrentCheckService checkService;

    @Override
    public List<TorrentItem> getItemsList() {
        return torrentItemDao.getItemsList();
    }

    @Override
    public void checkItem(TorrentItem item) {
        checkService.check(item);
        torrentItemDao.updateItem(item);
    }

    @Override
    public void checkAllItems() {
        List<TorrentItem> allItems = getItemsList();
        for (TorrentItem item : allItems) {
            checkItem(item);
        }
    }
}
