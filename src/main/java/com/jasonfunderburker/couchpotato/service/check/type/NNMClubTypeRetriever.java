package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by JasonFunderburker on 27.09.2016
 */
@Service
public class NNMClubTypeRetriever extends BaseTypeRetriever {
    private static final Logger logger = LoggerFactory.getLogger(NNMClubTypeRetriever.class);
    private static final String LOGIN_PAGE = "https://nnmclub.to/forum/login.php";

    @Override
    public HtmlAnchor getDownloadLink(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        return source.getFirstByXPath("//a[contains(@href, 'download')]");
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
        logger.debug("userName: {}", item.getUserInfo().getUserName());
        form.getInputByName("username").type(item.getUserInfo().getUserName());
        String password = item.getUserInfo().getPassword();
        if (password == null) throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
        logger.debug("password: {}", password);
        form.getInputByName("password").type(password);
        Page page = form.getInputByName("login").click();
        if (!page.getWebResponse().getContentAsString().contains(item.getUserInfo().getUserName())) {
            logger.trace("page after login: {}", page.getWebResponse().getContentAsString());
            throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
        }
        logger.trace("page: {}", page instanceof HtmlPage ? ((HtmlPage) page).asText() : "");
    }

    @Override
    public TorrentType getTorrentType() {
        return TorrentType.NNM_CLUB;
    }
}
