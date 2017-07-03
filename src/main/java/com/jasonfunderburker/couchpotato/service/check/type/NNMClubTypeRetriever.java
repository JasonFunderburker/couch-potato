package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentState;
import com.jasonfunderburker.couchpotato.entities.TorrentType;
import com.jasonfunderburker.couchpotato.entities.TorrentUserInfo;
import com.jasonfunderburker.couchpotato.exceptions.TorrentDownloadException;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

/**
 * Created by JasonFunderburker on 27.09.2016
 */
@Service
public class NNMClubTypeRetriever extends BaseTypeRetriever {
    private static final Logger logger = LoggerFactory.getLogger(NNMClubTypeRetriever.class);
    private static final String LOGIN_PAGE = "https://nnmclub.to/forum/login.php";

    @Autowired
    private ProxyConfig proxyConfig;

    @Override
    public URL getDownloadLink(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        HtmlAnchor anchor = source.getFirstByXPath("//a[contains(@href, 'download')]");
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
        HtmlTableDataCell state =  source.getFirstByXPath("//tr[@class='row1' and td[contains(text(), 'Зарегистрирован:')]]/td[2]");
        if (state != null) {
            result.setState(state.asText().trim());
            logger.debug("state : {}", result.getState());
        }
        else throw new TorrentRetrieveException("Error parsing state from url");
        return result;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage loginPage = webClient.getPage(LOGIN_PAGE);
        logger.trace("loginPage: {}", loginPage.asText());
        HtmlForm form = (HtmlForm)loginPage.getElementById("loginFrm");
        logger.debug("loginForm: {}", form.asText());
        TorrentUserInfo userInfo = item.getUserInfo();
        if (userInfo.getUsername() == null || userInfo.getPassword() == null) {
            throw new TorrentRetrieveException("Login ERROR: please add or refresh your userId and usess value on setting page");
        }
        logger.debug("userName: {}", userInfo.getUsername());
        form.getInputByName("username").type(userInfo.getUsername());
        String password = userInfo.getPassword();
        logger.debug("password: {}", password);
        form.getInputByName("password").type(userInfo.getPassword());
        Page page = form.getInputByName("login").click();
        if (!page.getWebResponse().getContentAsString().contains(userInfo.getUsername())) {
            logger.trace("page after login: {}", page.getWebResponse().getContentAsString());
            throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
        }
        logger.trace("page: {}", page instanceof HtmlPage ? ((HtmlPage) page).asText() : "");
    }

    @Override
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    @Override
    public TorrentType getTorrentType() {
        return TorrentType.NNM_CLUB;
    }
}
