package com.jasonfunderburker.couchpotato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;
import com.jasonfunderburker.couchpotato.domain.rss.RSSFeed;
import com.jasonfunderburker.couchpotato.security.SingleUserDetailsManager;
import com.jasonfunderburker.couchpotato.service.rss.RSSFeedGeneratorService;
import com.jasonfunderburker.couchpotato.service.torrents.TorrentsItemService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
@Controller
@RequestMapping("rss")
public class RSSController {
    private static final Logger logger = LoggerFactory.getLogger(RSSController.class);
    private static String DOWNLOAD_PATH_PREFIX;
    private ObjectMapper objectMapper = new XmlMapper();

    private final RSSFeedGeneratorService feedGeneratorService;
    private final TorrentsItemService itemService;
    private final SingleUserDetailsManager userDetails;

    @Autowired
    public RSSController(RSSFeedGeneratorService feedGeneratorService, TorrentsItemService itemService, SingleUserDetailsManager userDetails) {
        this.feedGeneratorService = feedGeneratorService;
        this.itemService = itemService;
        this.userDetails = userDetails;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void generateRss(HttpServletRequest request, HttpServletResponse response) throws IOException {
        generateRssContent(request, response);
    }

    @RequestMapping(value = "/public", method = RequestMethod.GET)
    public String generateRssPublicUrl(HttpServletRequest request,ModelMap modelMap, RedirectAttributes redirectAttributes) {
        String generatedString = generatePublicString();
        logger.debug("generatedString={}", generatedString);
        userDetails.saveRssPublicString(getUserName(), generatedString);
        String generatedRssUrl = request.getRequestURL() +"/"+generatedString;
        logger.debug("generatedRssUrl={}", generatedRssUrl);
        redirectAttributes.addFlashAttribute("generatedRssUrl", generatedRssUrl);
        modelMap.addAttribute("generatedRssUrl", generatedRssUrl);
        return "redirect:/itemList#rssGeneratePopup";
    }

    @RequestMapping(value = "/public/{rssUrl}", method = RequestMethod.GET)
    public void getPublicRss(HttpServletRequest request, HttpServletResponse response, @PathVariable("rssUrl") String rssUrl) throws IOException {
        if (userDetails.isCorrectRssPublicString(rssUrl)) {
            generateRssContent(request, response);
        }
        else response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @RequestMapping(value = "/public/invalidate", method = RequestMethod.POST)
    public void invalidatePublicRssUrl() {
        userDetails.saveRssPublicString(getUserName(), null);
    }

    private void generateRssContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TorrentItem> downloadedTorrents = itemService.findByStatus(TorrentStatus.DOWNLOADED);
        logger.debug("downloadedTorrents: {}", downloadedTorrents);
        RSSFeed rssFeed = feedGeneratorService.generateFor(downloadedTorrents, getDownloadPathPrefix(request));
        logger.debug("rssFeed: {}", rssFeed);
        String rssAsString = objectMapper.writeValueAsString(rssFeed);
        response.setContentType("application/rss+xml");
        IOUtils.write(rssAsString, response.getOutputStream(), "windows-1251");
        response.getOutputStream().close();
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String generatePublicString() {
        return UUID.randomUUID() +""+ UUID.randomUUID();
    }

    private String getDownloadPathPrefix(HttpServletRequest request) {
        if (DOWNLOAD_PATH_PREFIX == null) {
            DOWNLOAD_PATH_PREFIX = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
            logger.debug("set DOWNLOAD_PATH_PREFIX: {}", DOWNLOAD_PATH_PREFIX);
        }
        return DOWNLOAD_PATH_PREFIX;
    }
}
