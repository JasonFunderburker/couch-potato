package com.jasonfunderburker.couchpotato.service.rss;

import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.rss.RSSFeed;
import com.jasonfunderburker.couchpotato.entities.rss.RSSFeedMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
@Service
public class RSSFeedGeneratorServiceImpl implements RSSFeedGeneratorService {
    private RSSFeed rssFeed = new RSSFeed();

    @Override
    public RSSFeed generateFor(List<TorrentItem> downloadedItemsList, String linkPrefix) {
        rssFeed.getChannel().setLink(linkPrefix);
        List<RSSFeedMessage> feedMessages = new ArrayList<>();
        downloadedItemsList.forEach(item -> feedMessages.add(generateFor(item, linkPrefix)));
        rssFeed.getChannel().setEntries(feedMessages);
        return rssFeed;
    }

    private RSSFeedMessage generateFor(TorrentItem item, String linkPrefix) {
        RSSFeedMessage feedMessage = new RSSFeedMessage();
        String link = linkPrefix + "/checkResults/download/" + item.getFileName()+"/";
        feedMessage.setTitle(item.getName());
        feedMessage.setDescription(item.getName());
        feedMessage.setLink(link);
        feedMessage.setPubDate(new Date());
        feedMessage.setGuid(link);
        return feedMessage;
    }
}
