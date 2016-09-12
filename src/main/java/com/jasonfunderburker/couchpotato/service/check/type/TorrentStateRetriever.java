package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentStateRetrieveException;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
public interface TorrentStateRetriever {

    TorrentState getState(String source) throws TorrentStateRetrieveException;
}
