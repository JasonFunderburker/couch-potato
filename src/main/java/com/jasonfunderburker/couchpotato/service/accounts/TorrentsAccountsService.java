package com.jasonfunderburker.couchpotato.service.accounts;

import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;

import java.util.List;

public interface TorrentsAccountsService {

    List<TorrentType> getAllTorrentTypes();

    TorrentType getTypeById(Long id);

    List<TorrentUserInfo> getAllTorrentAccounts();

    TorrentUserInfo findByType(Long typeId);

    TorrentUserInfo addTorrentAccount(TorrentUserInfo userInfo);
}
