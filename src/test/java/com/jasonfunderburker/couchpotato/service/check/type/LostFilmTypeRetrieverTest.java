package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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

/*    @Test
    public void testGetDownloadLink() throws Exception {
        item.setState(new TorrentState("4 сезон 9 серия"));
        HtmlAnchor anchor = retriever.getDownloadLink(item, webClientMock);
    }
    */

    private Page preparePage(String resourceString) throws Exception {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            return webClient.getPage(getClass().getResource(resourceString));
        }
    }
}
