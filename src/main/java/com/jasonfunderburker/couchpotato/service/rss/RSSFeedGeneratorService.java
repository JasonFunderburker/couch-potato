package com.jasonfunderburker.couchpotato.service.rss;

import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.rss.RSSFeed;

import java.util.List;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
public interface RSSFeedGeneratorService {

    RSSFeed generateFor(List<TorrentItem> itemList, String linkPrefix);
}
