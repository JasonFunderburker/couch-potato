package com.jasonfunderburker.couchpotato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeed;
import com.jasonfunderburker.couchpotato.service.rss.RSSFeedGeneratorService;
import com.jasonfunderburker.couchpotato.service.torrents.TorrentsItemService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
@Controller
@RequestMapping("rss")
public class RSSController {
    private static final Logger logger = LoggerFactory.getLogger(RSSController.class);
    private static String DOWNLOAD_PATH_PREFIX;
    private ObjectMapper objectMapper = new XmlMapper();

    @Autowired
    RSSFeedGeneratorService feedGeneratorService;
    @Autowired
    TorrentsItemService itemService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void generateRss(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TorrentItem> downloadedTorrents = itemService.findByStatus(TorrentStatus.DOWNLOADED);
        logger.debug("downloadedTorrents: {}", downloadedTorrents);
        RSSFeed rssFeed = feedGeneratorService.generateFor(downloadedTorrents, getDownloadPathPrefix(request));
        logger.debug("rssFeed: {}", rssFeed);
        String rssAsString = objectMapper.writeValueAsString(rssFeed);
        response.setContentType("application/rss+xml");
        IOUtils.write(rssAsString, response.getOutputStream(), "windows-1251");
        response.getOutputStream().close();
    }

    private String getDownloadPathPrefix(HttpServletRequest request) {
        if (DOWNLOAD_PATH_PREFIX == null) {
            DOWNLOAD_PATH_PREFIX = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
            logger.debug("set DOWNLOAD_PATH_PREFIX: {}", DOWNLOAD_PATH_PREFIX);
        }
        return DOWNLOAD_PATH_PREFIX;
    }
}
