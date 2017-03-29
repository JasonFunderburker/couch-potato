package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.exceptions.TorrentDownloadException;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

/**
 * Created by JasonFunderburker on 25.10.2016
 */
@Service
public class RutrackerTypeRetriever extends BaseTypeRetriever {
    private static final Logger logger = LoggerFactory.getLogger(RutrackerTypeRetriever.class);

    @Autowired
    private ProxyConfig proxyConfig;

    @Override
    public URL getDownloadLink(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        HtmlAnchor anchor = source.getFirstByXPath("//a[@class='dl-stub']");
        if (anchor == null) {
            throw new TorrentDownloadException("Download link not found on page="+ item.getLink());
        }
        return source.getFullyQualifiedUrl(anchor.getHrefAttribute());
    }

    @Override
    public TorrentState getState(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        logger.trace("state source: {}", source.asText());
        TorrentState result = new TorrentState();
        HtmlTableDataCell state =  source.getFirstByXPath("//table[@class='attach bordered med']//tr[@class='row1']/td[2]");
        if (state != null) {
            result.setState(state.asText().trim());
            logger.debug("state : {}", result.getState());
        }
        else throw new TorrentRetrieveException("Error parsing state from url");
        return result;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, ElementNotFoundException, IOException {
        HtmlPage page1 = webClient.getPage(item.getLink());
        logger.trace("page before login: {}", page1.asText());
        HtmlElement formElement = page1.getHtmlElementById("login-form-quick");
        if (formElement instanceof HtmlForm) {
            HtmlForm form = (HtmlForm)formElement;
            form.getInputByName("login_username").type(item.getUserInfo().getUserName());
            String password = item.getUserInfo().getPassword();
            if (password == null)
                throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
            form.getInputByName("login_password").type(password);
            Page page = form.getInputByName("login").click();
            if (!page.getWebResponse().getContentAsString().contains("logged-in-as-uname")) {
                logger.trace("page after login: {}", page.getWebResponse().getContentAsString());
                throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
            }
            logger.trace("page: {}", page instanceof HtmlPage ? ((HtmlPage) page).asText() : "");
        }
        else throw new TorrentRetrieveException("Login ERROR: page is not contains login form");
    }

    @Override
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    @Override
    public TorrentType getTorrentType() {
        return TorrentType.RUTRACKER;
    }
}
