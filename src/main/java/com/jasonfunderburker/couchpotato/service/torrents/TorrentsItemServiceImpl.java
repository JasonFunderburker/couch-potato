package com.jasonfunderburker.couchpotato.service.torrents;

import com.jasonfunderburker.couchpotato.dao.AccountsDao;
import com.jasonfunderburker.couchpotato.dao.TorrentItemDao;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.service.check.TorrentCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
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
    AccountsDao accountsDao;
    @Autowired
    TorrentCheckService checkService;

    @Override
    public List<TorrentItem> getItemsList() {
        return torrentItemDao.getItemsList();
    }

    @Override
    public List<TorrentItem> findByStatus(TorrentStatus status) {
        return torrentItemDao.findByStatus(status);
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

    @Override
    public void addItemToList(TorrentItem item) throws IllegalArgumentException {
        logger.debug("add item: {}", item);
        item.setType(getTypeNameFromLink(item.getLink()));
        item.setUserInfo(getUserInfoByType(item.getType()));
        checkService.check(item);
        torrentItemDao.addItemToList(item);
    }

    @Override
    public void deleteItemFromList(long id) {
        torrentItemDao.deleteItemFromList(id);
    }

    private TorrentType getTypeNameFromLink(String link) {
        try {
            URL url = new URL(link);
            String typeName = url.getAuthority().replace("www.","").replaceFirst("\\.(.+)", "");
            TorrentType resultType = null;
            for (TorrentType type :TorrentType.values()) {
                if (type.getName().equals(typeName)) {
                    resultType = type;
                    break;
                }
            }
            if (resultType == null) throw new IllegalArgumentException("Unsupported torrent type: \""+typeName+"\"");
            return resultType;
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: "+link);
        }
    }

    private TorrentUserInfo getUserInfoByType(TorrentType type) {
        TorrentUserInfo userInfo = accountsDao.getUserInfo(type.getId());
        if (userInfo == null) throw new IllegalArgumentException("Invalid credentials: please add user information in settings");
        return userInfo;
    }
}
