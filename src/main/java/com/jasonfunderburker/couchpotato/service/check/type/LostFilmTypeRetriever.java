package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JasonFunderburker on 07.09.2016
 */
public class LostFilmTypeRetriever implements TorrentRetriever {
    private static Logger logger = LoggerFactory.getLogger(LostFilmTypeRetriever.class);
    private static Pattern statePattern = Pattern.compile("<[^<>]*?t_episode_num[^<>]*?>\\s*?(\\d+)\\s*?</[^<>]*?>");

    @Override
    public TorrentState getState(HtmlPage source) throws TorrentRetrieveException {
        TorrentState result = new TorrentState();
        HtmlTableDataCell state =  source.getFirstByXPath("//tr/td[@class='t_episode_num'][number(text())=text()]");
        result.setState(state.asText());
/*        Matcher stateMatcher = statePattern.matcher(source.asXml());
        if (stateMatcher.find()) {
            logger.debug("matching string: {}", stateMatcher.group());
            result.setState(stateMatcher.group(1));
        }
        else
            throw new TorrentRetrieveException("Error getting state from source: "+source); */
        return result;
    }

    @Override
    public String getName(HtmlPage source) throws TorrentRetrieveException {
        return source.getTitleText();
    }

    @Override
    public String getDownloadLink(HtmlPage source) throws TorrentRetrieveException {
        return null;
    }
}
