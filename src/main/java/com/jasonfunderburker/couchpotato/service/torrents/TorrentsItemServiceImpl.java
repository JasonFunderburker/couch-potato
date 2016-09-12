package com.jasonfunderburker.couchpotato.service.torrents;

import com.jasonfunderburker.couchpotato.dao.TorrentItemDao;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.service.check.TorrentCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
@Service
public class TorrentsItemServiceImpl implements TorrentsItemService {
    private static Logger logger = LoggerFactory.getLogger(TorrentsItemServiceImpl.class);

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
        logger.debug("checkItem: {}", item);
        checkService.check(item);
        torrentItemDao.updateItem(item);
        logger.debug("changed item: {}", item);
    }

    @Override
    public void checkAllItems() {
        List<TorrentItem> allItems = getItemsList();
        allItems.forEach(this::checkItem);
    }
}
