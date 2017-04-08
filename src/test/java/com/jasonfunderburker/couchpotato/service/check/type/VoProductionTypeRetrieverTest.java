package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.WebClient;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by JasonFunderburker on 08.04.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class VoProductionTypeRetrieverTest {
    private VoProductionTypeRetriever retriever = new VoProductionTypeRetriever();
    private TorrentItem item = new TorrentItem();

    @Mock
    private WebClient webClientMock;

    @Before
    public void before() throws Exception {
        item.setLink("http://vo-production.com/serials/TheSimpsons");
    }

    @Test
    public void testGetState() throws Exception {
        when(webClientMock.getPage(anyString())).thenReturn(HtmlUtils.preparePage("/voProductionRssSample.xml"));

        TorrentState oldState = new TorrentState("someLink");
        item.setState(oldState);
        TorrentState state = retriever.getState(item, webClientMock);

        assertEquals("1491235883000", state.getState());
        assertEquals("http://vo-production.com/serials/TheSimpsons/Sezon28", state.getInfo());
    }

    @Test
    public void testGetDownloadLink() throws Exception {
        when(webClientMock.getPage(anyString())).thenReturn(HtmlUtils.preparePage("/voProductionDownloadPageSample.html"));

        TorrentState state = new TorrentState();
        state.setInfo("http://stub");
        item.setState(state);

        URL downloadLink = retriever.getDownloadLink(item, webClientMock);

        assertEquals(new URL("http://vo-production.com/informer/Torrent/Plebs/Plebs_s02_(01-06)_1080p.torrent"), downloadLink);
    }
}
