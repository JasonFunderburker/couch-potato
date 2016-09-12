package com.jasonfunderburker.couchpotato.service.check.type;

import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentStateRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JasonFunderburker on 07.09.2016
 */
public class LostFilmTypeStateRetriever implements TorrentStateRetriever {
    private static Logger logger = LoggerFactory.getLogger(LostFilmTypeStateRetriever.class);
    private static Pattern statePattern = Pattern.compile("<[^<>]*?t_episode_num[^<>]*?>\\s*?(\\d+)\\s*?</[^<>]*?>");

    @Override
    public TorrentState getState(String source) throws TorrentStateRetrieveException {
        TorrentState result = new TorrentState();
        Matcher stateMatcher = statePattern.matcher(source);
        if (stateMatcher.find()) {
            logger.debug("matching string: {}", stateMatcher.group());
            result.setState(stateMatcher.group(1));
        }
        else
            throw new TorrentStateRetrieveException("Error getting state from source: "+source);
        return result;
    }
}
