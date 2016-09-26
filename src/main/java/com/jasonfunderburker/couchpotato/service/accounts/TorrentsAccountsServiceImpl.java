package com.jasonfunderburker.couchpotato.service.accounts;

import com.jasonfunderburker.couchpotato.dao.AccountsDao;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TorrentsAccountsServiceImpl implements TorrentsAccountsService {
    @Autowired
    AccountsDao accountsDao;

    @Override
    public List<TorrentType> getAllTorrentTypes() {
        return Arrays.asList(TorrentType.values());
    }

    @Override
    public void addTorrentAccount(TorrentUserInfo userInfo) {
        accountsDao.addUserInfo(userInfo);
    }
}
