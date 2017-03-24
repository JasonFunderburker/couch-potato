package com.jasonfunderburker.couchpotato.service.check.type;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by JasonFunderburker on 27.09.2016
 */
@RunWith(MockitoJUnitRunner.class)
public class RutrackerTypeRetrieverTest {
    private BaseTypeRetriever retriever = new RutrackerTypeRetriever();
    private TorrentItem item = new TorrentItem();

    @Mock
    private WebClient webClientMock;

    @Before
    public void before() throws Exception {
        HtmlPage checkedPage;
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setCssEnabled(false);
            checkedPage = webClient.getPage(getClass().getResource("/rutrackerHtmlPageSample.html"));
        }
        when(webClientMock.getPage(anyString())).thenReturn(checkedPage);
        item.setLink("someLink");
    }


    @Test
    public void testGetState() throws Exception {
        TorrentState state = retriever.getState(item, webClientMock);

        assertEquals("31-Дек-16 15:19", state.getState());
    }

    @Test
    public void testGetDownloadLink() throws Exception {
//        HtmlAnchor anchor = retriever.getDownloadLink(item, webClientMock);

//        assertEquals("dl.php?t=5295827", anchor.getHrefAttribute());
 //       assertEquals("dl.php?t=5295827", retriever.getDownloadLink(item, webClientMock));
    }
}
