package com.jasonfunderburker.couchpotato.service.check;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentStatus;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import com.jasonfunderburker.couchpotato.service.check.type.StateRetrieversDictionary;
import com.jasonfunderburker.couchpotato.service.check.type.TorrentRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by JasonFunderburker on 01.09.2016
 */
@Service
public class TorrentCheckServiceImpl implements TorrentCheckService {
    private static Logger logger = LoggerFactory.getLogger(TorrentCheckServiceImpl.class);

    @Override
    public void check(TorrentItem item) {
        logger.debug("check item: {}", item);
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setRedirectEnabled(true);
//            webClient.getOptions().getProxyConfig().setProxyAutoConfigUrl("https://antizapret.prostovpn.org/proxy.pac");
            webClient.getOptions().getProxyConfig().setProxyHost("proxy.antizapret.prostovpn.org");
            webClient.getOptions().getProxyConfig().setProxyPort(3128);
            webClient.getOptions().getProxyConfig().setSocksProxy(false);
            webClient.getCookieManager().setCookiesEnabled(true);

            TorrentRetriever torrentRetriever = StateRetrieversDictionary.getRetrieverType(item.getType());
            if (torrentRetriever != null) {
                torrentRetriever.login(item, webClient);
                TorrentState newState = torrentRetriever.getState(item, webClient);
                if (item.getState() == null) {
                    item.setName(torrentRetriever.getName(item, webClient));
                    item.setStatus(TorrentStatus.NEW);
                    item.setState(newState);
                    item.setErrorText(null);
                } else {
                    if (!newState.equals(item.getState())) {
                        item.setStatus(TorrentStatus.REFRESHED);
                        item.setState(newState);
                        item.setErrorText(null);
                        String fileName = torrentRetriever.download(item, webClient);
                        item.setFileName(fileName);
                        item.setStatus(TorrentStatus.DOWNLOADED);
                    } else {
                        item.setStatus(TorrentStatus.UNCHANGED);
                        item.setErrorText(null);
                    }
                }
            } else {
                item.setStatus(TorrentStatus.ERROR);
                item.setErrorText("Unsupported torrent type: " + item.getType().getName());
            }
        } catch (MalformedURLException e) {
            item.setStatus(TorrentStatus.ERROR);
            item.setErrorText("Error create url from item link: " + item.getLink() + ", cause: " + e.getMessage());
            logger.error("Error create url from item link: "+item.getLink(), e);
        } catch (IOException | FailingHttpStatusCodeException e) {
            item.setStatus(TorrentStatus.ERROR);
            item.setErrorText("Can't read response from url: " + item.getLink() + ", cause: " + e.getMessage());
            logger.error("Error create url from item link: "+item.getLink(), e);
        } catch (TorrentRetrieveException e) {
            item.setStatus(TorrentStatus.ERROR);
            item.setErrorText(e.getMessage());
            logger.error("Check item error",e);
        }
    }
}

