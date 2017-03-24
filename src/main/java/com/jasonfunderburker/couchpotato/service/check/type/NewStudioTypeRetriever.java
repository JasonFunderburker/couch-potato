package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentType;
import com.jasonfunderburker.couchpotato.exceptions.TorrentRetrieveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by JasonFunderburker on 25.10.2016
 */
@Service
public class NewStudioTypeRetriever extends BaseTypeRetriever {
    private static final Logger logger = LoggerFactory.getLogger(NewStudioTypeRetriever.class);

    @Override
    public String getName(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        HtmlAnchor a = source.getFirstByXPath("//a[contains(@href, 'viewforum.php?f=')]");
        return a.asText();
    }

    @Override
    public String getDownloadLink(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        HtmlAnchor anchor = source.getFirstByXPath("//div[@id='"+item.getState().getState()+"']//a[contains(@href, 'download')]");
        return anchor.getHrefAttribute();
    }

    @Override
    public TorrentState getState(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage source = webClient.getPage(item.getLink());
        logger.trace("state source: {}", source.asText());
        TorrentState result = new TorrentState();
        HtmlDivision state = source.getFirstByXPath("//div[number(@id)=@id and .//a[contains(@id, 'tt') and b[contains(text(), 'Сезон') and contains(text(), 'Серия') and contains(text(), '1080p')]]]");
        if (state != null) {
            result.setState(state.getId());
        } else throw new TorrentRetrieveException("Error parsing state from url");
        return result;
    }

    @Override
    public void login(TorrentItem item, WebClient webClient) throws TorrentRetrieveException, IOException {
        HtmlPage page1 = webClient.getPage(item.getLink());
        logger.trace("page before login: {}", page1.asText());
        HtmlForm form = page1.getFirstByXPath("//form[@class='form-signin']");
        form.getInputByName("login_username").type(item.getUserInfo().getUserName());
        String password = item.getUserInfo().getPassword();
        if (password == null)
            throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
        form.getInputByName("login_password").type(password);
        Page page = form.getOneHtmlElementByAttribute("button", "type", "submit").click();
        if (page.getWebResponse().getContentAsString().contains("Ошибка")) {
            logger.trace("page after login: {}", page.getWebResponse().getContentAsString());
            throw new TorrentRetrieveException("Login ERROR: please add or refresh your credentials on setting page");
        }
        logger.trace("page: {}", page instanceof HtmlPage ? ((HtmlPage) page).asText() : "");
    }

    @Override
    public TorrentType getTorrentType() {
        return TorrentType.NEW_STUDIO;
    }
}
