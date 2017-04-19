package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jasonfunderburker.couchpotato.entities.TorrentItem;
import com.jasonfunderburker.couchpotato.entities.TorrentState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by JasonFunderburker on 27.09.2016
 */
@RunWith(MockitoJUnitRunner.class)
public class NNMClubTypeRetrieverTest {
    private BaseTypeRetriever retriever = new NNMClubTypeRetriever();
    private TorrentItem item = new TorrentItem();
    private static final String contextPath = "http://someLink/";

    @Mock
    private WebClient webClientMock;

    @Before
    public void before() throws Exception {
        HtmlPage checkedPage;
        HtmlPage checkedPageSpy;
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            checkedPage = webClient.getPage(getClass().getResource("/nnmClubHtmlPageSample.html"));
            checkedPageSpy = spy(checkedPage);
        }
        when(webClientMock.getPage(anyString())).thenReturn(checkedPageSpy);
        item.setLink(contextPath+"some.php");
        doReturn(new URL(item.getLink())).when(checkedPageSpy).getBaseURL();
    }


    @Test
    public void testGetState() throws Exception {
        TorrentState state = retriever.getState(item, webClientMock);

        assertEquals("11 Сен 2016 11:33:31", state.getState());
    }

    @Test
    public void testGetDownloadLink() throws Exception {
        assertEquals(new URL(contextPath+"download.php?id=843729"), retriever.getDownloadLink(item, webClientMock));
    }
}
