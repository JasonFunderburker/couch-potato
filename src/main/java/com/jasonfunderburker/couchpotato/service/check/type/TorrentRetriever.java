package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;

import java.io.IOException;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
public interface TorrentRetriever {

    TorrentState getState(HtmlPage source) throws TorrentRetrieveException;

    String getName(HtmlPage source) throws TorrentRetrieveException;

    void downloadLink(TorrentItem item) throws TorrentRetrieveException, IOException;
}
