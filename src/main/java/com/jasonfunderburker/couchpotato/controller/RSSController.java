package com.jasonfunderburker.couchpotato.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentStatus;
import com.jasonfunderburker.couchpotato.entities.rss.RSSFeed;
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
import java.text.SimpleDateFormat;
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
    private XmlMapper xmlMapper = new XmlMapper();

    private final RSSFeedGeneratorService feedGeneratorService;
    private final TorrentsItemService itemService;
    private final SingleUserDetailsManager userDetails;

    @Autowired
    public RSSController(RSSFeedGeneratorService feedGeneratorService, TorrentsItemService itemService, SingleUserDetailsManager userDetails) {
        this.feedGeneratorService = feedGeneratorService;
        this.itemService = itemService;
        this.userDetails = userDetails;
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.setDateFormat(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"));
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void generateRss(HttpServletRequest request, HttpServletResponse response) throws IOException {
        generateRssContent(request, response);
    }

    @RequestMapping(value = "/generator", method = RequestMethod.GET)
    public String getGenerator() throws IOException {
        return "rssGenerator";
    }

    @RequestMapping(value = "/public", method = RequestMethod.GET)
    public String generateRssPublicUrl(HttpServletRequest request, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        String generatedString = generatePublicString();
        logger.debug("generatedString={}", generatedString);
        userDetails.saveRssPublicString(getUserName(), generatedString);
        String generatedRssUrl = request.getRequestURL() + "/" + generatedString;
        logger.debug("generatedRssUrl={}", generatedRssUrl);
        redirectAttributes.addFlashAttribute("generatedRssUrl", generatedRssUrl);
        modelMap.addAttribute("generatedRssUrl", generatedRssUrl);
        return "redirect:/itemList#rssGeneratePopup";
    }

    @RequestMapping(value = "/public/{rssUrl}", method = RequestMethod.GET)
    public void getPublicRss(HttpServletRequest request, HttpServletResponse response, @PathVariable("rssUrl") String rssUrl) throws IOException {
        if (userDetails.isCorrectRssPublicString(rssUrl)) {
            generateRssContent(request, response);
        } else response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @RequestMapping(value = "/public/invalidate", method = RequestMethod.POST)
    public String invalidatePublicRssUrl(RedirectAttributes redirectAttributes) {
        userDetails.saveRssPublicString(getUserName(), null);
        redirectAttributes.addFlashAttribute("invalidateResult", "successfully invalidated");
        return "redirect:/itemList";
    }

    private void generateRssContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TorrentItem> downloadedTorrents = itemService.findByStatus(TorrentStatus.DOWNLOADED);
        logger.debug("downloadedTorrents: {}", downloadedTorrents);
        RSSFeed rssFeed = feedGeneratorService.generateFor(downloadedTorrents, getDownloadPathPrefix(request));
        logger.debug("rssFeed: {}", rssFeed);
        String rssAsString = xmlMapper.writeValueAsString(rssFeed);
        response.setContentType("application/rss+xml; charset=UTF-8");
        IOUtils.write(rssAsString, response.getOutputStream(), "UTF-8");
        response.getOutputStream().close();
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String generatePublicString() {
        return UUID.randomUUID() + "" + UUID.randomUUID();
    }

    private String getDownloadPathPrefix(HttpServletRequest request) {
        if (DOWNLOAD_PATH_PREFIX == null) {
            logger.debug("x-forwarded-proto = {}", request.getHeader("x-forwarded-proto"));
            logger.debug("x-forwarded-port = {}", request.getHeader("x-forwarded-port"));
            logger.debug("Host header: = {}", request.getHeader("Host"));
            String schemePart;
            String portPart;
            String hostPart;
            if (request.getHeader("x-forwarded-proto") != null) {
                schemePart = request.getHeader("x-forwarded-proto");
                portPart = request.getHeader("x-forwarded-port");
                hostPart = request.getHeader("Host");
            } else {
                schemePart = request.getScheme();
                portPart = String.valueOf(request.getServerPort());
                hostPart = request.getServerName();
            }
            DOWNLOAD_PATH_PREFIX = schemePart + "://" + hostPart
                    + (portPart != null ? (":" + portPart) : "") + request.getContextPath();
            logger.debug("set DOWNLOAD_PATH_PREFIX: {}", DOWNLOAD_PATH_PREFIX);
        }
        return DOWNLOAD_PATH_PREFIX;
    }
}
