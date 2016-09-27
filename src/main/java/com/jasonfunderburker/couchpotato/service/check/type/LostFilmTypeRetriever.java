package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by JasonFunderburker on 07.09.2016
 */
public class LostFilmTypeRetriever extends BaseTypeRetriever {
    private static Logger logger = LoggerFactory.getLogger(LostFilmTypeRetriever.class);

    @Override
    public TorrentState getState(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        TorrentState result = new TorrentState();
        HtmlTableDataCell state =  source.getFirstByXPath("//tr/td[@class='t_episode_num'][number(text())=text()]");
        result.setState(state.asText());
        logger.debug("state: {}, state as text: {}", state, result.getState());
        return result;
    }

    @Override
    public HtmlAnchor getDownloadLink(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage pageAfterLogin = webClient.getPage(item.getLink());
        logger.debug("pageWithShowAfterLogin {}", pageAfterLogin.asText());
        HtmlTableBody tableWithLink = pageAfterLogin.getFirstByXPath("//tbody[tr/td[@class='t_episode_num' and contains(text(),'" + item.getState().getState() + "')]]");
        logger.debug("tableWithLink: {}", tableWithLink.asText());
        HtmlPage downloadPage = tableWithLink.getElementsByAttribute("td", "class", "t_episode_title").get(0).click();
        logger.debug("downloadPage: {}", downloadPage.asText());
        HtmlAnchor anchor = downloadPage.getFirstByXPath("//a[contains(text(), '1080p')]");
        if (anchor == null) throw new TorrentRetrieveException("link for '1080p' is not found");
        return anchor;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage page1 = webClient.getPage(item.getLink());
        logger.debug("page before login: {}", page1.asText());
        HtmlForm form = page1.getForms().get(0);
        form.getInputByName("login").type(item.getUserInfo().getUserName());
        form.getInputByName("password").type(item.getUserInfo().getPassword());
        Page page = form.getInputByValue(" Войти ").click();
        if (page instanceof TextPage) {
            logger.debug("page after login: {}", ((TextPage) page).getContent());
            throw new TorrentRetrieveException("Login ERROR: "+ ((TextPage) page).getContent());
        }
    }
}
