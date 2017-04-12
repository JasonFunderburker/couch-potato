package com.jasonfunderburker.couchpotato.service.check.type;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeed;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeedMessage;
import com.jasonfunderburker.couchpotato.exceptions.TorrentDownloadException;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by JasonFunderburker on 08.04.17.
 */
public class VoProductionTypeRetriever extends BaseTypeRetriever {
    private static final Logger logger = LoggerFactory.getLogger(VoProductionTypeRetriever.class);
    private static final String RSS_PAGE = "http://vo-production.com/rss";

    private final XmlMapper mapper = new XmlMapper();

    public VoProductionTypeRetriever() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", new Locale("en-US")));
    }

    @Override
    public TorrentState getState(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        TorrentState currentState = item.getState();
        if (currentState == null) {
            currentState = new TorrentState();
            currentState.setState("0");
            currentState.setInfo("Not checked");
        }

        XmlPage rssPage = webClient.getPage(RSS_PAGE);
        logger.trace("Rss page content={}", rssPage.asXml());
        RSSFeed rss = mapper.readValue(rssPage.asXml(), RSSFeed.class);
        Optional<RSSFeedMessage> rssItem = rss.getChannel().getEntries().stream()
                .peek(e -> logger.trace("rss item = {}",e))
                .filter(t -> t.getLink().contains(item.getLink()))
                .findFirst();

        if (!rssItem.isPresent()) {
            logger.debug("Rss feed don't contain info about {}", item.getLink());
            return currentState;
        }

        logger.debug("rssItem is found = {}", rssItem);
        TorrentState result = new TorrentState();
        result.setState(Long.toString(rssItem.get().getPubDate().getTime()));
        result.setInfo(rssItem.get().getLink().trim());

        return result;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        // No need to authorize
    }

    @Override
    public TorrentType getTorrentType() {
        return TorrentType.VO_PRODUCTION;
    }

    @Override
    public URL getDownloadLink(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        if (item.getState() == null) {
            throw new TorrentDownloadException("Trying to download torrent before checking RSS");
        }

        String seasonLink = item.getState().getInfo();
        if (seasonLink == null) {
            throw new TorrentDownloadException("Trying to download torrent before checking RSS");
        }

        HtmlPage page = webClient.getPage(item.getLink());
        List<DomElement> allLinks = page.getElementsByTagName("a");
        List<DomElement> allDownloadLinks = allLinks.stream()
                .filter(link -> link.getAttribute("href").startsWith("http://vo-production.com/informer/Torrent/"))
                .collect(Collectors.toList());

        Optional<DomElement> hdDownloadLink = allDownloadLinks.stream()
                .filter(link -> link.getAttribute("href").contains("1080p"))
                .findFirst();

        if (!hdDownloadLink.isPresent()) {
            hdDownloadLink = allDownloadLinks.stream()
                    .filter(link -> link.getAttribute("href").contains("720p") || !link.getAttribute("href").contains("rip"))
                    .findFirst();
        }

        if (!hdDownloadLink.isPresent()) {
            throw new TorrentDownloadException("Failed to find download link meeting all specified conditions");
        }

        return new URL(hdDownloadLink.get().getAttribute("href"));
    }
}
