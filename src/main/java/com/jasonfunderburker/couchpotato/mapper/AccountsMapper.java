package com.jasonfunderburker.couchpotato.mapper;

import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountsMapper {

    void addUserInfo(TorrentUserInfo userInfo);

    TorrentUserInfo getUserInfo(@Param("typeId")Long typeId);

}
