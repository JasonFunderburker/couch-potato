package com.jasonfunderburker.couchpotato.service.check.type;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeed;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeedMessage;
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
    private final XmlMapper mapper = new XmlMapper();

    public LostFilmTypeRetriever() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public TorrentState getState(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        TorrentState result;
        if (item.getState() == null) {
            result = getInitialState(item, webClient);
        } else {
            XmlPage rssPage = webClient.getPage("http://www.lostfilm.tv/rss.xml");
            logger.trace("Rss page content={}", rssPage.asXml());
            RSSFeed rss = mapper.readValue(rssPage.asXml(), RSSFeed.class);
            Optional<RSSFeedMessage> rssItem = rss.getChannel().getEntries().stream()
                    .peek(e -> logger.trace("rss item = {}",e))
                    .filter(t -> t.getLink().contains(item.getLink()))
                    .findFirst();
            if (rssItem.isPresent()) {
                logger.debug("rssItem is found = {}", rssItem);
                result = new TorrentState();
                result.setState(rssItem.get().getLink().replace(item.getLink(), "").trim());
                result.setInfo(rssItem.get().getTitle().trim());
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
        result.setState(stateString.substring(stateString.indexOf("/season"), stateString.lastIndexOf("'"))+"/");
        logger.debug("state: {}, state as text: {}", state, result.getState());
        return result;
    }

    @Override
    public HtmlAnchor getDownloadLink(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        webClient.getOptions().setJavaScriptEnabled(true);
        HtmlPage pageWithEpisodeInfo = webClient.getPage(item.getLink()+item.getState().getState());
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
