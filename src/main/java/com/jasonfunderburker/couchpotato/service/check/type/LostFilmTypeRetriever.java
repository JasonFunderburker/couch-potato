package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
/**
 * Created by JasonFunderburker on 07.09.2016
 */
public class LostFilmTypeRetriever implements TorrentRetriever {
    private static Logger logger = LoggerFactory.getLogger(LostFilmTypeRetriever.class);

    @Override
    public TorrentState getState(HtmlPage source) throws TorrentRetrieveException {
        TorrentState result = new TorrentState();
        HtmlTableDataCell state =  source.getFirstByXPath("//tr/td[@class='t_episode_num'][number(text())=text()]");
        result.setState(state.asText());
        logger.debug("state: {}, state as text: {}", state, result.getState());
        return result;
    }

    @Override
    public String getName(HtmlPage source) throws TorrentRetrieveException {
        return source.getTitleText();
    }

    @Override
    public String getDownloadLink(TorrentItem item) throws TorrentRetrieveException, IOException {
        String link = "";
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getCookieManager().setCookiesEnabled(true);
            HtmlPage page1 = webClient.getPage(item.getLink());
            HtmlForm form = page1.getForms().get(0);
            form.getInputByName("login").type(item.getType().getUserName());
            form.getInputByName("password").type(item.getType().getPassword());
            form.getInputByValue(" Войти ").click();
            page1 = webClient.getPage(item.getLink());
            HtmlTableBody tableWithLink = page1.getFirstByXPath("//tbody[tr/td[@class='t_episode_num' and contains(text(),'" + item.getState().getState() + "')]]");
            HtmlPage downloadPage = tableWithLink.getElementsByAttribute("td", "class", "t_episode_title").get(0).click();
            HtmlAnchor anchor = downloadPage.getFirstByXPath("//a[contains(text(), '1080p')]");
            link = anchor.getHrefAttribute();
        }
        return link;
    }
}
