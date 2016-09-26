package com.jasonfunderburker.couchpotato.dao.mybatis;

import com.jasonfunderburker.couchpotato.dao.AccountsDao;
import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.mapper.AccountsMapper;

public class MyBatisAccountsDao implements AccountsDao {
    AccountsMapper mapper;

    @Override
    public void addUserInfo(TorrentUserInfo userInfo) {
        mapper.addUserInfo(userInfo);
    }

    public AccountsMapper getMapper() {
        return mapper;
    }

    public void setMapper(AccountsMapper mapper) {
        this.mapper = mapper;
    }
}
