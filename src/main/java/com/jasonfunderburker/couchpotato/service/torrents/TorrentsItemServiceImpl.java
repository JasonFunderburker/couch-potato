package com.jasonfunderburker.couchpotato.service.torrents;

import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentStatus;
import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.repositories.AccountRepository;
import com.jasonfunderburker.couchpotato.repositories.TorrentRepository;
import com.jasonfunderburker.couchpotato.service.check.TorrentCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
@Service
public class TorrentsItemServiceImpl implements TorrentsItemService {
    private static Logger logger = LoggerFactory.getLogger(TorrentsItemServiceImpl.class);

    @Autowired
    TorrentCheckService checkService;

    private final AccountRepository accountRepo;
    private final TorrentRepository torrentRepo;

    private volatile Date checkStartDate = null;
    private volatile Date checkEndDate = null;

    @Autowired
    public TorrentsItemServiceImpl(AccountRepository accountRepo, TorrentRepository torrentRepo) {
        this.accountRepo = accountRepo;
        this.torrentRepo = torrentRepo;
    }

    @Override
    public Date getCheckStartDate() {
        return checkStartDate;
    }

    @Override
    public Date getCheckEndDate() {
        return checkEndDate;
    }

    @Override
    public List<TorrentItem> getItemsList() {
        return torrentRepo.findAll();
    }

    @Override
    public List<TorrentItem> findByStatus(TorrentStatus status) {
        return torrentRepo.findByStatus(status);
    }

    @Override
    public void checkItem(TorrentItem item) {
        logger.debug("checkItem: {}", item);
        checkService.check(item);
        torrentRepo.saveAndFlush(item);
        logger.debug("changed item: {}", item);
    }

    @Override
    public TorrentItem checkItem(long id) {
        logger.debug("checkItem id: {}", id);
        TorrentItem item = torrentRepo.findOne(id);
        if (item != null) checkItem(item);
        return item;
    }

    @Override
    public void checkAllItems() {
        checkStartDate = new Date();
        List<TorrentItem> allItems = getItemsList();
        allItems.forEach(this::checkItem);
        checkEndDate = new Date();
    }

    @Override
    public void addItemToList(TorrentItem item) throws IllegalArgumentException {
        logger.debug("add item: {}", item);
        item.setLink(item.getLink().replace("www.",""));
        item.setUserInfo(getUserInfoByType(getTypeNameFromLink(item.getLink())));
        checkService.check(item);
        torrentRepo.saveAndFlush(item);
    }

    @Override
    public void deleteItemFromList(long id) {
        torrentRepo.delete(id);
    }

    private TorrentType getTypeNameFromLink(String link) {
        try {
            URL url = new URL(link);
            String typeName = url.getAuthority().replaceFirst("\\.(.+)", "");
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
        TorrentUserInfo userInfo = accountRepo.findByType(type);
        if (userInfo == null) throw new IllegalArgumentException("Invalid credentials: please add user information in settings");
        return userInfo;
    }
}
