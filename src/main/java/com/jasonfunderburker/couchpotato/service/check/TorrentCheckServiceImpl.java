package com.jasonfunderburker.couchpotato.service.check;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;
import com.jasonfunderburker.couchpotato.exceptions.TorrentStateRetrieveException;
import com.jasonfunderburker.couchpotato.service.check.type.LostFilmTypeStateRetriever;
import com.jasonfunderburker.couchpotato.service.check.type.StateRetrieversDictionary;
import com.jasonfunderburker.couchpotato.service.check.type.TorrentStateRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
@Service
public class TorrentCheckServiceImpl implements TorrentCheckService {
    private static Logger logger = LoggerFactory.getLogger(TorrentCheckServiceImpl.class);

    @Override
    public void check(TorrentItem item) {
        try {
            StringBuilder responseBody = new StringBuilder();
            URL url = new URL(item.getLink());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    responseBody.append(line);
                }
            }
            logger.debug("responseBody: {}", responseBody.toString());
            TorrentStateRetriever checkType = StateRetrieversDictionary.getRetrieverType(item.getType().getName());
            if (checkType != null) {
                TorrentState newState = checkType.getState(responseBody.toString());
                if (item.getState() == null) {
                    item.setStatus(TorrentStatus.NEW);
                    item.setState(newState);
                    item.setErrorText(null);
                }
                else {
                    if (!newState.equals(item.getState())) {
                        item.setStatus(TorrentStatus.REFRESHED);
                        item.setState(newState);
                        item.setErrorText(null);
                    } else {
                        item.setStatus(TorrentStatus.UNCHANGED);
                        item.setErrorText(null);
                    }
                }
            }
            else {
                item.setStatus(TorrentStatus.ERROR);
                item.setErrorText("Unsupported torrent type: "+ item.getType().getName());
            }
        }
        catch (MalformedURLException e) {
            item.setStatus(TorrentStatus.ERROR);
            item.setErrorText("Error create url from item link: "+item.getLink());
        }
        catch (IOException e) {
            item.setStatus(TorrentStatus.ERROR);
            item.setErrorText("Can't read response from url: "+item.getLink());
        }
        catch (TorrentStateRetrieveException e) {
            item.setStatus(TorrentStatus.ERROR);
            item.setErrorText(e.getMessage());
        }
    }
}

