package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by JasonFunderburker on 25.10.2016
 */
public class RutrackerTypeRetriever extends BaseTypeRetriever {
    private static Logger logger = LoggerFactory.getLogger(RutrackerTypeRetriever.class);

    @Override
    public HtmlAnchor getDownloadLink(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        return source.getFirstByXPath("//a[@class='dl-stub']");
    }

    @Override
    public TorrentState getState(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        logger.debug("state source: {}", source.asText());
        TorrentState result = new TorrentState();
        HtmlSpan state =  source.getFirstByXPath("//span[@title='Когда зарегистрирован']");
        if (state != null) {
            result.setState(state.asText().trim());
            logger.debug("state : {}", result.getState());
        }
        else throw new TorrentRetrieveException("Error parsing state from url");
        return result;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage page1 = webClient.getPage(item.getLink());
        logger.debug("page before login: {}", page1.asText());
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
                logger.debug("page after login: {}", page.getWebResponse().getContentAsString());
                throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
            }
            logger.debug("page: {}", page instanceof HtmlPage ? ((HtmlPage) page).asText() : "");
        }
        else throw new TorrentRetrieveException("Login ERROR: page is not contains login form");
    }
}
