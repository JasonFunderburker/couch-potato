package com.jasonfunderburker.couchpotato.service.rss;

import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeed;

import java.util.List;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
public interface RSSFeedGeneratorService {

    RSSFeed generateFor(List<TorrentItem> itemList, String linkPrefix);
}
