package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by JasonFunderburker on 07.09.2016
 */
@Service
public class LostFilmTypeRetriever extends BaseTypeRetriever {
    private static final Logger logger = LoggerFactory.getLogger(LostFilmTypeRetriever.class);
    private static final String LOGIN_PAGE = "https://www.lostfilm.tv/login";

    @Override
    public TorrentState getState(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        TorrentState result;
        if (item.getState() == null) {
            result = getInitialState(item, webClient);
        } else {
            XmlPage rssPage = webClient.getPage("http://www.lostfilm.tv/rss.xml");
            logger.trace("Rss page content={}", rssPage.asXml());
            DomNodeList<DomElement> elements = rssPage.getElementsByTagName("item");
            logger.trace("elements={}", elements);
            Optional<DomElement> element = elements.stream()
                    .peek(t -> logger.debug("element={}, getAttributeNodeLinkValue={}", t, t.getAttributeNode("link").getNodeValue()))
                    .filter(e -> e.getAttributeNode("link").getNodeValue().contains(item.getLink()))
                    .findFirst();
            if (element.isPresent()) {
                result = new TorrentState();
                result.setState(element.get().getAttributeNode("link").getNodeValue().replace(item.getLink(), "").trim());
                result.setInfo(element.get().getAttributeNode("title").getNodeValue());
            } else {
                logger.debug("Rss feed don't contain info about {}", item.getLink());
                result = item.getState();
            }
            logger.debug("state: {}", result);
        }
        return result;
    }

    private TorrentState getInitialState(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        logger.debug("getInitialState");
        HtmlPage source = webClient.getPage(item.getLink());
        logger.trace("source {}", source.asText());
        TorrentState result = new TorrentState();
        HtmlTableDataCell state = source.getFirstByXPath("//table[@class='movie-parts-list']//tr[not(@class='not-available')]/td[@class='beta']");
        logger.debug("state {}", state);
        String stateString = state.getAttribute("onClick");
        result.setState(stateString.substring(stateString.indexOf("season"), stateString.lastIndexOf("'")));
        logger.debug("state: {}, state as text: {}", state, result.getState());
        return result;
    }

    @Override
    public HtmlAnchor getDownloadLink(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        webClient.getOptions().setJavaScriptEnabled(true);
        HtmlPage pageAfterLogin = webClient.getPage(item.getLink());
        logger.trace("pageWithShowAfterLogin {}", pageAfterLogin.asText());
        HtmlTableDataCell tableDataCell = pageAfterLogin.getFirstByXPath("//table[@class='movie-parts-list']//tr[not(@class='not-available')]/td[@class='beta' and contains(text(),'" + item.getState().getState() + "')]");
        logger.debug("tableDataCell: {}", tableDataCell.asText());
        HtmlPage pageWithEpisodeInfo = tableDataCell.click();
        logger.trace("pageWithEpisodeInfo: {}", pageWithEpisodeInfo.asText());
        HtmlDivision div = pageWithEpisodeInfo.getFirstByXPath("//div[@class='external-btn']");
        logger.debug("div with click: {}", div.asText());
        HtmlPage downloadPage = div.click();
        logger.trace("downloadTitle: {}", downloadPage.getTitleText());
        logger.trace("downloadPage: {}", downloadPage.asText());
        HtmlAnchor anchor = downloadPage.getFirstByXPath("//a[contains(text(), '1080p')]");
        webClient.getOptions().setJavaScriptEnabled(false);
        if (anchor == null) throw new TorrentRetrieveException("link for '1080p' is not found");
        return anchor;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage loginPage = webClient.getPage(LOGIN_PAGE);
        logger.trace("loginPage: {}", loginPage.asText());
        HtmlInput loginInput = loginPage.getElementByName("mail");
        loginInput.type(item.getUserInfo().getUserName());
        String password = item.getUserInfo().getPassword();
        if (password == null)
            throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
        HtmlInput passInput = loginPage.getElementByName("pass");
        passInput.type(password);
        HtmlInput button = loginPage.getFirstByXPath("//input[@class='primary-btn sign-in-btn' and @type='button']");
        button.click();
    }

    @Override
    public TorrentType getTorrentType() {
        return TorrentType.LOST_FILM;
    }
}
