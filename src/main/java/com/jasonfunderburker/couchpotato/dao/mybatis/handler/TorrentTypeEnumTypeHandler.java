package com.jasonfunderburker.couchpotato.dao.mybatis.handler;

import com.jasonfunderburker.couchpotato.domain.TorrentType;

/**
 * Created by JasonFunderburker on 19.09.2016
 */
public class TorrentTypeEnumTypeHandler extends HasIdValueEnumTypeHandler<TorrentType> {
    public TorrentTypeEnumTypeHandler(Class<TorrentType> type) {
        super(type);
    }
}
