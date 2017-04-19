package com.jasonfunderburker.couchpotato.entities.converters;

import com.jasonfunderburker.couchpotato.entities.TorrentStatus;

/**
 * Created on 19.04.2017
 *
 * @author JasonFunderburker
 */
public class TorrentStatusConverter extends EnumWithIdConverter<TorrentStatus> {

    public TorrentStatusConverter() {
        super();
        setEnumType(TorrentStatus.class);
    }
}
