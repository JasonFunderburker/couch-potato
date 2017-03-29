package com.jasonfunderburker.couchpotato.service.check;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentDownloadException;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import com.jasonfunderburker.couchpotato.service.check.type.StateRetrieversDictionary;
import com.jasonfunderburker.couchpotato.service.check.type.TorrentRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;

import static com.jasonfunderburker.couchpotato.domain.TorrentStatus.*;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
@Service
public class TorrentCheckServiceImpl implements TorrentCheckService {
    private static final Logger logger = LoggerFactory.getLogger(TorrentCheckServiceImpl.class);

    @Autowired
    private StateRetrieversDictionary stateRetrieversDictionary;

    @Override
    public void check(TorrentItem item) {
        logger.debug("check item: {}", item);
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getCookieManager().setCookiesEnabled(true);

            TorrentRetriever torrentRetriever = stateRetrieversDictionary.getRetrieverType(item.getType());
            if (torrentRetriever != null) {
                if (torrentRetriever.getProxyConfig() != null) {
                    webClient.getOptions().setProxyConfig(torrentRetriever.getProxyConfig());
                    logger.debug("using proxy config: host={}, port={}", torrentRetriever.getProxyConfig().getProxyHost(), torrentRetriever.getProxyConfig().getProxyPort());
                }
                torrentRetriever.login(item, webClient);
                TorrentState newState = torrentRetriever.getState(item, webClient);
                if (item.getState() == null) {
                    item.setName(torrentRetriever.getName(item, webClient));
                    item.setStatus(NEW);
                    item.setState(newState);
                    item.setErrorText(null);
                } else {
                    if (!newState.equals(item.getState()) || item.getStatus().equals(DOWNLOAD_ERROR)) {
                        item.setStatus(item.getStatus().equals(DOWNLOAD_ERROR) ? RELOADED : REFRESHED);
                        item.setState(newState);
                        item.setErrorText(null);
                        String fileName = torrentRetriever.download(item, webClient);
                        item.setFileName(fileName);
                        item.setStatus(DOWNLOADED);
                    } else {
                        item.setStatus(UNCHANGED);
                        item.setErrorText(null);
                    }
                }
            } else {
                item.setStatus(ERROR);
                item.setErrorText("TorrentRetriever for type=" + item.getType().getName() + " is not found");
            }
        } catch (MalformedURLException e) {
            item.setStatus(ERROR);
            item.setErrorText("Error create url from item link: " + item.getLink() + ", cause: " + e.getMessage());
            logger.error("Error create url from item link: "+item.getLink(), e);
        } catch (IOException | FailingHttpStatusCodeException e) {
            item.setStatus(ERROR);
            item.setErrorText("Can't read response from url: " + item.getLink() + ", cause: " + e.getMessage());
            logger.error("Can't read response from url: " + item.getLink(), e);
        } catch (TorrentDownloadException e) {
            item.setStatus(DOWNLOAD_ERROR);
            item.setErrorText(e.getMessage());
            logger.error("Download item error: ", e);
        } catch (TorrentRetrieveException e) {
            item.setStatus(ERROR);
            item.setErrorText(e.getMessage());
            logger.error("Check item error: ", e);
        } catch (Exception e) {
            item.setStatus(ERROR);
            item.setErrorText("Some unexpected error: "+e.getMessage());
            logger.error("Unexpected error: ", e);
        }

    }
}

