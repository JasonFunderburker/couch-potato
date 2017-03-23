package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by JasonFunderburker on 12.09.2016
 */
@RunWith(MockitoJUnitRunner.class)
public class LostFilmTypeRetrieverTest {
    private LostFilmTypeRetriever retriever = new LostFilmTypeRetriever();
    private TorrentItem item = new TorrentItem();

    @Mock
    private WebClient webClientMock;

    @Before
    public void before() throws Exception {
        item.setLink("someLink");
    }


    @Test
    public void testGetInitialState() throws Exception {
        when(webClientMock.getPage(anyString())).thenReturn(preparePage("/lostfilmHtmlPageSample.html"));
        TorrentState state = retriever.getState(item, webClientMock);

        assertEquals("/season_4/episode_9/", state.getState());
    }

    @Test
    public void testGetState() throws Exception {
        when(webClientMock.getPage(anyString())).thenReturn(preparePage("/lostFilmRssPageSample.xml"));
        TorrentState oldState = new TorrentState("someLink");
        item.setState(oldState);
        TorrentState state = retriever.getState(item, webClientMock);

        assertEquals("/season_7/episode_14/", state.getState());
        assertNotNull(state.getInfo());
    }

    @Test
    public void testGetDownloadLink() throws Exception {
        WebClient wc = new WebClient();
        WebClient wcstub = spy(wc);
        doReturn(preparePage("/lostFilmRssDDPageSample.xml")).when(wcstub).getPage(anyString());
        TorrentState state = new TorrentState();
        state.setInfo("SomeName. SomeName2. (S05E17)");
        item.setState(state);
        HtmlAnchor anchor = retriever.getDownloadLink(item, wcstub);
        assertEquals("http://link1080p", anchor.getAttribute("href"));
        wc.close();
        wcstub.close();
    }


    private Page preparePage(String resourceString) throws Exception {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            return webClient.getPage(getClass().getResource(resourceString));
        }
    }
}
