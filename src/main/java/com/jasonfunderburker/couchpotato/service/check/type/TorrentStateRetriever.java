package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.domain.TorrentState;

/**
 * Created by Ekaterina.Bashkankova on 01.09.2016
 */
public interface TorrentStateRetriever {

    TorrentState getState(String source);
}
