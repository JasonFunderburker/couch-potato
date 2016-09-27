package com.jasonfunderburker.couchpotato.service.check.type;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jasonfunderburker.couchpotato.domain.TorrentItem;
import com.jasonfunderburker.couchpotato.domain.TorrentState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Ekaterina.Bashkankova on 27.09.2016
 */
@RunWith(MockitoJUnitRunner.class)
public class NNMClubTypeRetrieverTest {
    private TorrentRetriever retriever = new NNMClubTypeRetriever();
    private HtmlPage checkedPage;

    @Mock
    private WebClient webClientMock;

    @Before
    public void before() throws Exception {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            checkedPage = webClient.getPage(getClass().getResource("/nnmClubHtmlPageSample.html"));
        }
    }

    /*    @Test
    public void test() {
        TorrentUserInfo userInfo = new TorrentUserInfo();
        userInfo.setUserName("SomeUser");
        userInfo.setHash("somePassword123");

        assertNotEquals("somePassword123", userInfo.getHash());
        assertEquals("somePassword123", userInfo.getPassword());
    } */

    @Test
    public void testGetState() throws Exception {
        TorrentItem item = new TorrentItem();
        item.setLink("someLink");
        when(webClientMock.getPage(anyString())).thenReturn(checkedPage);

        TorrentState state = retriever.getState(item, webClientMock);

        System.out.println(state.getState());
    }
}
