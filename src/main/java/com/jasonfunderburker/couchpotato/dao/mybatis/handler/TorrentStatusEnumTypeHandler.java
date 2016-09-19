package com.jasonfunderburker.couchpotato.dao.mybatis.handler;

import com.jasonfunderburker.couchpotato.domain.TorrentStatus;

/**
 * Created by Ekaterina.Bashkankova on 19.09.2016
 */
public class TorrentStatusEnumTypeHandler extends HasIdValueEnumTypeHandler<TorrentStatus> {
    public TorrentStatusEnumTypeHandler(Class<TorrentStatus> type) {
        super(type);
    }
}
