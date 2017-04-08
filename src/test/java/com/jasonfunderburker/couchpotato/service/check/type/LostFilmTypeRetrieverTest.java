package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.*;
import com.jasonfunderburker.couchpotato.HtmlUtils;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import com.jasonfunderburker.couchpotato.domain.TorrentUserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        when(webClientMock.getPage(anyString())).thenReturn(HtmlUtils.preparePage("/lostfilmHtmlPageSample.html"));
        TorrentState state = retriever.getState(item, webClientMock);

        assertEquals("/season_4/episode_9/", state.getState());
    }

    @Test
    public void testGetState() throws Exception {
        when(webClientMock.getPage(anyString())).thenReturn(HtmlUtils.preparePage("/lostFilmRssPageSample.xml"));
        TorrentState oldState = new TorrentState("someLink");
        item.setState(oldState);
        TorrentState state = retriever.getState(item, webClientMock);

        assertEquals("/season_7/episode_14/", state.getState());
        assertNotNull(state.getInfo());
    }

    @Test
    public void testGetDownloadLink() throws Exception {
        List<String> cookies = new ArrayList<>();
        doAnswer(inv -> cookies.add(inv.getArgumentAt(0, String.class))).when(webClientMock).addCookie(anyString(), any(URL.class), anyObject());
        when(webClientMock.getPage(anyString())).thenReturn(HtmlUtils.preparePage("/lostFilmRssDDPageSample.xml"));
        String uidValue = "uidValue";
        String usessValue = "usessValue";
        TorrentState state = new TorrentState();
        state.setInfo("SomeName. SomeName2. (S05E17)");
        item.setState(state);
        item.setUserInfo(new TorrentUserInfo(uidValue, usessValue));
        assertEquals(new URL("http://link1080p"), retriever.getDownloadLink(item, webClientMock));
        System.out.println(cookies);
        assertTrue(cookies.contains("uid="+uidValue));
        assertTrue(cookies.contains("usess="+usessValue));
    }
}
