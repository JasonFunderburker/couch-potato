package com.jasonfunderburker.couchpotato.dao.mybatis;

import com.jasonfunderburker.couchpotato.dao.AccountsDao;
import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.mapper.AccountsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyBatisAccountsDao implements AccountsDao {
    @Autowired
    AccountsMapper mapper;

    @Override
    public void addUserInfo(TorrentUserInfo userInfo) {
        mapper.addUserInfo(userInfo);
    }

    @Override
    public TorrentUserInfo getUserInfo(Long typeId) {
        return mapper.getUserInfo(typeId);
    }

    public AccountsMapper getMapper() {
        return mapper;
    }

    public void setMapper(AccountsMapper mapper) {
        this.mapper = mapper;
    }
}
