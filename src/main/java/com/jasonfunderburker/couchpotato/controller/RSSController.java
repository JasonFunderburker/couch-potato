package com.jasonfunderburker.couchpotato.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentStatus;
import com.jasonfunderburker.couchpotato.entities.UserDO;
import com.jasonfunderburker.couchpotato.entities.UserPrincipal;
import com.jasonfunderburker.couchpotato.entities.rss.RSSFeed;
import com.jasonfunderburker.couchpotato.entities.rss.RssInfo;
import com.jasonfunderburker.couchpotato.repositories.SingleUserRepository;
import com.jasonfunderburker.couchpotato.service.rss.RSSFeedGeneratorService;
import com.jasonfunderburker.couchpotato.service.torrents.TorrentsItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static java.util.Collections.singletonMap;
import static java.util.Objects.nonNull;

/**
 * Created by JasonFunderburker on 19.10.2016
 */
@RestController
@RequestMapping("/rss")
public class RSSController {
    private static final Logger logger = LoggerFactory.getLogger(RSSController.class);
    private static String DOWNLOAD_PATH_PREFIX;
    private XmlMapper xmlMapper = new XmlMapper();

    private final RSSFeedGeneratorService feedGeneratorService;
    private final TorrentsItemService itemService;
    private final SingleUserRepository userRepository;

    @Autowired
    public RSSController(RSSFeedGeneratorService feedGeneratorService, TorrentsItemService itemService, SingleUserRepository userRepository) {
        this.feedGeneratorService = feedGeneratorService;
        this.itemService = itemService;
        this.userRepository = userRepository;
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.setDateFormat(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"));
    }


    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/rss+xml; charset=UTF-8")
    public ResponseEntity<RSSFeed> generateRss(HttpServletRequest request) throws IOException {
        RSSFeed body = generateRssContent(request);
        return ResponseEntity.ok().body(body);
    }

    @RequestMapping(value = "/public", method = RequestMethod.GET)
    public RssInfo generateRssPublicUrl(HttpServletRequest request) {
        UserDO userDO = getUser();
        String generatedString = userDO.getRssPublic();
        logger.debug("generatedString={}", generatedString);
        String generatedRssUrl = null;
        if (nonNull(generatedString) && !generatedString.isEmpty()) {
            generatedRssUrl = request.getRequestURL() + "/" + generatedString;
        }
        logger.debug("generatedRssUrl={}", generatedRssUrl);
        return new RssInfo(generatedRssUrl);
    }

    @RequestMapping(value = "/public/{rssUrl}", method = RequestMethod.GET, produces = "application/rss+xml; charset=UTF-8")
    public ResponseEntity<?> getPublicRss(HttpServletRequest request, @PathVariable("rssUrl") String rssUrl) throws IOException {
        if (userRepository.isCorrectRssString(rssUrl)) {
            RSSFeed body = generateRssContent(request);
            return ResponseEntity.ok().body(body);
        } else
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/public/invalidate", method = RequestMethod.POST)
    public void invalidatePublicRssUrl() {
        UserDO userDO = getUser();
        userDO.setRssPublic(null);
        userRepository.save(userDO);
    }

    @RequestMapping(value = "/public/generate", method = RequestMethod.POST)
    public RssInfo generatePublicRssUrl(HttpServletRequest request) {
        UserDO userDO = getUser();
        String generatedString = generatePublicString();
        userDO.setRssPublic(generatedString);
        userRepository.save(userDO);
        return new RssInfo(request.getRequestURL() + "/" + generatedString);
    }

    private RSSFeed generateRssContent(HttpServletRequest request) {
        List<TorrentItem> downloadedTorrents = itemService.findByStatus(TorrentStatus.DOWNLOADED);
        logger.debug("downloadedTorrents: {}", downloadedTorrents);
        RSSFeed rssFeed = feedGeneratorService.generateFor(downloadedTorrents, getDownloadPathPrefix(request));
        logger.debug("rssFeed: {}", rssFeed);
        //String rssAsString = xmlMapper.writeValueAsString(rssFeed);
        return rssFeed;
    }

    private UserDO getUser() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
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
