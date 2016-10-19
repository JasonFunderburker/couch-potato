package com.jasonfunderburker.couchpotato.service.rss;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeed;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeedMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
public class RSSFeedGeneratorServiceImpl implements RSSFeedGeneratorService {
    private RSSFeed feed = new RSSFeed();

    @Override
    public RSSFeed generateFor(List<TorrentItem> downloadedItemsList) {
        List<RSSFeedMessage> feedMessages = new ArrayList<>();
        downloadedItemsList.forEach(item -> feedMessages.add(generateFor(item)));
        feed.setEntries(feedMessages);
        return feed;
    }

    private RSSFeedMessage generateFor(TorrentItem item) {
        RSSFeedMessage feedMessage = new RSSFeedMessage();
        feedMessage.setTitle(item.getName());
        feedMessage.setDescription(item.getName());
        feedMessage.setLink("/checkResults/download/" + item.getFileName());
        return feedMessage;
    }
}
