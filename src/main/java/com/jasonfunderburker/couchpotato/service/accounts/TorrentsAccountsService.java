package com.jasonfunderburker.couchpotato.service.accounts;

import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;

import java.util.List;

public interface TorrentsAccountsService {

    List<TorrentType> getAllTorrentTypes();

    void addTorrentAccount(TorrentUserInfo userInfo);
}
