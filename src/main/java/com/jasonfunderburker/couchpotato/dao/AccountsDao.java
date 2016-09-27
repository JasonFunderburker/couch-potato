package com.jasonfunderburker.couchpotato.dao;

import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;

public interface AccountsDao {

    void addUserInfo(TorrentUserInfo userInfo);

    TorrentUserInfo getUserInfo(Long typeId);
}
