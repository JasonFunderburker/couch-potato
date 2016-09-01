package com.jasonfunderburker.couchpotato.service.check;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;
import com.jasonfunderburker.couchpotato.service.check.type.TorrentStateRetriever;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ekaterina.Bashkankova on 01.09.2016
 */
@Service
public class TorrentCheckServiceImpl implements TorrentCheckService {
    @Override
    public void check(TorrentItem item) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(item.getLink());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            TorrentStateRetriever checkType = getRetrieverType(item);
            TorrentState newState = checkType.getState(result.toString());
            if (!newState.equals(item.getState())) {
                item.setStatus(TorrentStatus.REFRESHED);
                item.setState(newState);
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
    }

    private TorrentStateRetriever getRetrieverType(TorrentItem item) {
        //todo
        return null;
    }
}

