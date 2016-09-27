package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ekaterina.Bashkankova on 27.09.2016
 */
public class NNMClubTypeRetriever extends BaseTypeRetriever {
    private static Logger logger = LoggerFactory.getLogger(NNMClubTypeRetriever.class);
    private static final String LOGIN_PAGE = "https://nnmclub.to/forum/login.php";
    private static final Pattern statePattern = Pattern.compile("id=(\\d+)");

    @Override
    public HtmlAnchor getDownloadLink(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        return source.getFirstByXPath("//a[contains(@href, 'download')]");
    }

    @Override
    public TorrentState getState(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        logger.debug("state source: {}", source.asText());
        TorrentState result = new TorrentState();
        HtmlAnchor state =  source.getFirstByXPath("//a[contains(@href, 'download')]");
        logger.debug("state anchor: {}", state.asText());
        Matcher matcher = statePattern.matcher(state.asText());
        if (matcher.find()) {
            result.setState(matcher.group(1));
            logger.debug("state: {}, state as text: {}", state, result.getState());
        }
        else throw new TorrentRetrieveException("Error parsing state from url");
        return result;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage loginPage = webClient.getPage(LOGIN_PAGE);
        logger.debug("loginPage: {}", loginPage.asText());
        HtmlForm form = (HtmlForm)loginPage.getElementById("loginFrm");
        logger.debug("loginForm: {}", form.asText());
        logger.debug("userName: {}", item.getUserInfo().getUserName());
        form.getInputByName("username").type(item.getUserInfo().getUserName());
        logger.debug("password: {}", item.getUserInfo().getPassword());
        form.getInputByName("password").type(item.getUserInfo().getPassword());
        Page page = form.getInputByName("login").click();
        if (!page.getWebResponse().getContentAsString().contains(item.getUserInfo().getUserName())) {
            logger.debug("page after login: {}", page.getWebResponse().getContentAsString());
            throw new TorrentRetrieveException("Login ERROR: ");
        }
        logger.debug("page: {}", page instanceof HtmlPage ? ((HtmlPage) page).asText() : "");
    }
}
