package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentState;
import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;

import java.io.IOException;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
public interface TorrentRetriever {

    TorrentState getState(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException;

    String getName(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException;

    String download(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException;

    void login(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException;

    ProxyConfig getProxyConfig();

    TorrentType getTorrentType();
}
