package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by JasonFunderburker on 07.09.2016
 */
public class LostFilmTypeRetriever implements TorrentRetriever {
    private static Logger logger = LoggerFactory.getLogger(LostFilmTypeRetriever.class);

    @Override
    public TorrentState getState(HtmlPage source) throws TorrentRetrieveException {
        TorrentState result = new TorrentState();
        HtmlTableDataCell state =  source.getFirstByXPath("//tr/td[@class='t_episode_num'][number(text())=text()]");
        result.setState(state.asText());
        logger.debug("state: {}, state as text: {}", state, result.getState());
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
