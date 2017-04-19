package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.exceptions.TorrentDownloadException;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

/**
 * Created by JasonFunderburker on 27.09.2016
 */
public abstract class BaseTypeRetriever implements TorrentRetriever {
    private static final Logger logger = LoggerFactory.getLogger(BaseTypeRetriever.class);

    @Override
    public String getName(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        return source.getTitleText();
    }

    @Override
    public String download(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException {
        String fileName = "";
        URL downloadLink = getDownloadLink(item, webClient);
        logger.debug("downloadLink = {}", downloadLink);
        URL routePath = this.getClass().getClassLoader().getResource(File.separator);
        if (routePath != null) {
            File dir = new File(routePath.getPath() + File.separator + "downloads");
            logger.debug("absolute download directory path: " + dir.getAbsolutePath());
            boolean mkdirs = dir.mkdirs();
            fileName = "torrent" + item.getId() + UUID.randomUUID() + ".torrent";
            FileOutputStream outputStream = new FileOutputStream(new File(dir, fileName));
            WebResponse response = webClient.getPage(downloadLink).getWebResponse();
            if (response.getContentType().equals("application/x-bittorrent")) {
                InputStream inputStream = response.getContentAsStream();
                IOUtils.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
            } else {
                throw new TorrentDownloadException("can't download torrent file from url=" + downloadLink + " " +
                        "content-type=" + response.getContentType() + ", responseAsString=" + response.getContentAsString());
            }
        }
        return fileName;
    }

    public abstract URL getDownloadLink(TorrentItem item, final WebClient webClient) throws TorrentRetrieveException, IOException;

    @Override
    public ProxyConfig getProxyConfig() {
        return null;
    }
}
