package com.jasonfunderburker.couchpotato.entities.converters;

import com.jasonfunderburker.couchpotato.entities.TorrentType;

/**
 * Created on 19.04.2017
 *
 * @author JasonFunderburker
 */
public class TorrentTypeConverter extends EnumWithIdConverter<TorrentType> {

    public TorrentTypeConverter() {
        super();
        setEnumType(TorrentType.class);
    }
}
