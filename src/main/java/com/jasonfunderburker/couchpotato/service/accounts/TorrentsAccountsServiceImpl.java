package com.jasonfunderburker.couchpotato.service.accounts;

import com.jasonfunderburker.couchpotato.dao.AccountsDao;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TorrentsAccountsServiceImpl implements TorrentsAccountsService {
    private static final Logger logger = LoggerFactory.getLogger(TorrentsAccountsServiceImpl.class);
    @Autowired
    AccountsDao accountsDao;

    @Override
    public List<TorrentType> getAllTorrentTypes() {
        return Arrays.asList(TorrentType.values());
    }

    @Override
    public void addTorrentAccount(TorrentUserInfo userInfo) {
        logger.debug("add torrent account: "+userInfo);
        accountsDao.addUserInfo(userInfo);
    }
}
