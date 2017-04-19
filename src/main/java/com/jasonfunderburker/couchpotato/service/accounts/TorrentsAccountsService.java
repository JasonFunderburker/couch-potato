package com.jasonfunderburker.couchpotato.service.accounts;

import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;

import java.util.List;

public interface TorrentsAccountsService {

    List<TorrentType> getAllTorrentTypes();

    TorrentType getTypeById(Long id);

    void addTorrentAccount(TorrentUserInfo userInfo);
}
